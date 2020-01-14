package pisibg.ittalents.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pisibg.ittalents.exception.ProductNotFoundException;
import pisibg.ittalents.model.dto.ProductWithCurrentPriceDTO;
import pisibg.ittalents.model.dto.RegularPriceProductDTO;
import pisibg.ittalents.model.repository.ProductRepository;
import pisibg.ittalents.model.pojo.Product;
import pisibg.ittalents.model.repository.SubcategoryRepository;

import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
public class ProductController extends AbstractController {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private SubcategoryRepository subcategoryRepository;


    @GetMapping(value = "/products/all")
    public List<ProductWithCurrentPriceDTO> getAll() throws SQLException {
        List <Product> products = productRepository.findAll();
        List<ProductWithCurrentPriceDTO> productsWithPrices = new ArrayList<>();
        for (Product p : products) {
            productsWithPrices.add(new ProductWithCurrentPriceDTO(p));
        }
        return productsWithPrices;
    }

    @GetMapping(value = "/products/{id}")
    public ProductWithCurrentPriceDTO getById(@PathVariable("id") long id) throws ProductNotFoundException, SQLException {
        if (productRepository.findById(id).isPresent()) {
           Product product = productRepository.findById(id).get();
           return new ProductWithCurrentPriceDTO(product);
        } else {
            throw new ProductNotFoundException("Product not found!");
        }
    }

    @GetMapping(value = "/products/filter")
    public List<ProductWithCurrentPriceDTO> getAllByName(@RequestParam("name") String name) throws ProductNotFoundException, SQLException {
        List<Product> products = productRepository.findAllByNameLike("%" + name + "%");
        List<ProductWithCurrentPriceDTO> productsWithPrices = new ArrayList<>();
        if (!(products.isEmpty())) {
            for (Product p: products) {
                productsWithPrices.add(new ProductWithCurrentPriceDTO(p));
            }
            return productsWithPrices;
        } else {
            throw new ProductNotFoundException("Product not found!");
        }
    }

    @PostMapping(value = "/products/")
    public ProductWithCurrentPriceDTO save(@RequestBody RegularPriceProductDTO regularPriceProductDTO) throws SQLException {
        //TODO validate properties!
        regularPriceProductDTO.setSubcategory(subcategoryRepository.getOne(regularPriceProductDTO.getSubcategoryId()));
        Product product = new Product(regularPriceProductDTO);
        productRepository.save(product);
        return new ProductWithCurrentPriceDTO(product);
    }

    @DeleteMapping(value = "/products/{id}")
    public void removeProduct(@PathVariable("id") long id, HttpServletResponse resp) {
        if (!productRepository.existsById(id)) {
            resp.setStatus(404);
        } else {
            productRepository.deleteById(id);
            resp.setStatus(200);
        }
    }

    @GetMapping(value = "/products/discounted")
    public List<ProductWithCurrentPriceDTO> getAllDiscounted() throws ProductNotFoundException, SQLException {
        List<Product> products = productRepository.findAllByDiscountNotNull();
        List<ProductWithCurrentPriceDTO> productsWithPrices = new ArrayList<>();
        if (!(products.isEmpty())) {
            for (Product p: products) {
                ProductWithCurrentPriceDTO product = new ProductWithCurrentPriceDTO(p);
                if(product.getDiscount().getDate_to().isAfter(LocalDateTime.now()) &&
                        product.getDiscount().getDate_from().isBefore(LocalDateTime.now())){
                    productsWithPrices.add(new ProductWithCurrentPriceDTO(p));
                }
            }
            return productsWithPrices;
        } else {
            throw new ProductNotFoundException("Product not found!");
        }
    }



    @GetMapping(value = "/products/price")
    public List<Product> getByMinAndMaxRegularPRice(@RequestParam("minPrice") double minPrice, @RequestParam("maxPrice") double maxPrice) throws ProductNotFoundException {
        List<Product> products = productRepository.findAllByPriceBetween(minPrice, maxPrice);
        if (!(products.isEmpty())) {
            return products;
        } else {
            throw new ProductNotFoundException("Product not found!");
        }
    }

    @GetMapping(value = "/products/currentPrice")
    public List<ProductWithCurrentPriceDTO> getByMinAndMaxCurrentPrice(@RequestParam("minPrice") double minPrice, @RequestParam("maxPrice") double maxPrice) throws ProductNotFoundException, SQLException {
        List<Product> products = productRepository.findAll();
        List <ProductWithCurrentPriceDTO> productsWithPrices = new ArrayList<>();
        if (!(products.isEmpty())) {
            for (Product p : products) {
                ProductWithCurrentPriceDTO pwcp = new ProductWithCurrentPriceDTO(p);
                if(pwcp.getPrice() >= minPrice && pwcp.getPrice() <= maxPrice){
                    productsWithPrices.add(pwcp);
                }
            }
            return productsWithPrices;
        } else {
            throw new ProductNotFoundException("Products not found!");
        }
    }

}

