package pisibg.ittalents.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pisibg.ittalents.dao.DiscountDAO;
import pisibg.ittalents.dao.UserDAO;
import pisibg.ittalents.exception.AuthorizationException;
import pisibg.ittalents.exception.BadRequestException;
import pisibg.ittalents.exception.NotFoundException;
import pisibg.ittalents.model.dto.DiscountDTO;
import pisibg.ittalents.model.dto.ProductWithCurrentPriceDTO;
import pisibg.ittalents.model.dto.RegularPriceProductDTO;
import pisibg.ittalents.model.pojo.Discount;
import pisibg.ittalents.model.pojo.Product;
import pisibg.ittalents.model.pojo.User;
import pisibg.ittalents.model.repository.*;
import utils.SessionManager;

import javax.activation.MimetypesFileTypeMap;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@RestController
public class AdminPanel extends AbstractController {
    @Autowired
    private DiscountDAO discountDAO;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    AddressRepository addressRepository;
    @Autowired
    DiscountRepository discountRepository;
    @Autowired
    SubcategoryRepository subcategoryRepository;
    @Autowired
    ProductRepository productRepository;

    @PostMapping(value = "/products/")
    public ProductWithCurrentPriceDTO save(@RequestBody RegularPriceProductDTO regularPriceProductDTO, HttpSession session) throws SQLException {
        User user = (User) session.getAttribute(SessionManager.USER__LOGGED);
        if (user == null) {
            throw new AuthorizationException("You have to log in");
        }
        if (!findUserById(user.getId()).isAdmin()) {
            throw new AuthorizationException("You are not authorized");
        }
        //TODO validate properties!
        regularPriceProductDTO.setSubcategory(subcategoryRepository.getOne(regularPriceProductDTO.getSubcategoryId()));
        Product product = new Product(regularPriceProductDTO);
        if(product.getPrice() <= 0 ){
            throw new BadRequestException("Price should not be negative or null. ");
        }
        if(!(subcategoryRepository.existsById(product.getSubcategory().getId()))){
            throw new BadRequestException("You should select an existing category.");
        }
        if(product.getName().isEmpty() || product.getName() == null){
            throw new BadRequestException("You should write a name for the product");
        }
        if(product.getQuantity() < 0 ){
            throw new BadRequestException("Quantity should not have a negative value.");
        }
        if(product.getDescription().isEmpty() || product.getDescription() == null){
            throw new BadRequestException("The description should not be empty.");
        }
        productRepository.save(product);
        return new ProductWithCurrentPriceDTO(product);
    }

    @DeleteMapping(value = "/products/{id}")
    public void removeProduct(@PathVariable("id") long id, HttpSession session) {
        User user = (User) session.getAttribute(SessionManager.USER__LOGGED);
        if (user == null) {
            throw new AuthorizationException("You have to log in");
        }
        if (!findUserById(user.getId()).isAdmin()) {
            throw new AuthorizationException("You are not authorized");
        }
        if (!productRepository.existsById(id)) {
            throw new NotFoundException("The product doesn't exist.");
        } else {
            productRepository.deleteById(id);
        }
    }

    @GetMapping(value = "/users/all")
    public List<User> getAllUsers(HttpSession session) throws SQLException {
        User user = (User) session.getAttribute(SessionManager.USER__LOGGED);
        if (user == null) {
            throw new AuthorizationException("You have to log in");
        }
        if (user.isAdmin()) {
            return userRepository.findAll();
        } else {
            throw new AuthorizationException("You are not authorized");
        }
    }

    public User findUserById(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            return user.get();
        } else {
            throw new NotFoundException("User not found");
        }
    }

    //TODO fix
    @DeleteMapping("/users/delete")
    public ResponseEntity<String> deleteUser(HttpSession session, Long id) {
        User user = (User) session.getAttribute(SessionManager.USER__LOGGED);
        if (user == null) {
            throw new AuthorizationException("You need to log in first");
        }
        if (SessionManager.isLogged(session)) {
            if (!findUserById(user.getId()).isAdmin()) { //TODO check
                throw new AuthorizationException("You are not authorized");
            }
            userRepository.deleteById(id);
            session.invalidate();
        }
        return new ResponseEntity<>("User deleted successfully!", HttpStatus.OK);
    }

    //TODO find all subscribed, add/ delete product
    //TODO validation for start and end date and DTO or Pojo
    @PostMapping("/discounts/add")
    public ResponseEntity<String> addDiscount(@RequestBody DiscountDTO discountDTO,
                                              HttpSession session) {
        User user = (User) session.getAttribute(SessionManager.USER__LOGGED);
        if (user == null) {
            throw new AuthorizationException("You need to log in first");
        }

        if (SessionManager.isLogged(session)) {
            if (!findUserById(user.getId()).isAdmin()) { //TODO check
                throw new AuthorizationException("You are not authorized");
            }

            Discount discount = new Discount();
            discount.setName(discountDTO.getName());
            discount.setAmount(discountDTO.getAmount());
            discount.setDate_from(discountDTO.getDate_from());
            discount.setDate_to(discountDTO.getDate_to());
            discountRepository.save(discount);
        }
        return new ResponseEntity<>("Discount added", HttpStatus.CREATED);
    }

    //TODO delete discount
    //todo CHECK LONG, return DTO
    @PostMapping("applydiscount")
    public ResponseEntity<String> applytoSubcategory(@RequestParam("discount_id") long discountId,
                                                     @RequestParam("sucategory_id") long subcategoryId,
                                                     HttpSession session) throws SQLException {
        User user = (User) session.getAttribute(SessionManager.USER__LOGGED);
        if (user == null) {
            throw new AuthorizationException("You need to log in first");
        }

        if (SessionManager.isLogged(session)) {
            if (!findUserById(user.getId()).isAdmin()) { //TODO check
                throw new AuthorizationException("You are not authorized");
            }
            // get discount id and add it to products which are from certain category
            discountDAO.applyDiscount(discountId,subcategoryId);
        }
        return new ResponseEntity<>("Discount added", HttpStatus.CREATED);
    }

    @PostMapping("/product/{id}/pictures")
    public ProductWithCurrentPriceDTO addPicture(@RequestPart(value = "picture") MultipartFile multipartFile, @PathVariable("id") long id,
                                                 HttpSession session) throws IOException, SQLException {
        if (!SessionManager.isLogged(session)) {
            throw new AuthorizationException("You have to log in first");}
        User user = (User) session.getAttribute(SessionManager.USER__LOGGED);
        if (!findUserById(user.getId()).isAdmin()) {
            throw new AuthorizationException("You are not authorized");
        }
        Product product = productRepository.getOne(id);
        String path = "C://Users//User//NewRepo//PetShop//pictures//";
        String pictureName = getNameForUpload(multipartFile.getOriginalFilename(), product);
        File picture = new File(path + pictureName);
        FileOutputStream fos = new FileOutputStream(picture);
        fos.write(multipartFile.getBytes());
        fos.close();
        String mimeType = new MimetypesFileTypeMap().getContentType(picture);
        if(!mimeType.substring(0, 5).equalsIgnoreCase("image")){
            picture.delete();
            throw new BadRequestException("Only pictures allowed.");
        }
        product.setImage(pictureName);
        productRepository.save(product);
        return new ProductWithCurrentPriceDTO(product);
    }

    private static String getNameForUpload(String name, Product product){
        String[] all = name.split("\\.", 2);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yy-MM-dd-hh-mm-ss");
        LocalDateTime localDateTime = LocalDateTime.now();
        String parse = localDateTime.format(dateTimeFormatter);
        String nameWithoutId = all[0];
        String formatForPicture = all[1];
        String nameWithId = nameWithoutId + "_" + parse + "_" + product.getId() +  "." + formatForPicture;
        System.out.println(name);
        return nameWithId;
    }
}
