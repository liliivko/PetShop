package pisibg.ittalents.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pisibg.ittalents.exception.ProductNotFoundException;
import pisibg.ittalents.model.dto.ProductWithCategoryDTO;
import pisibg.ittalents.model.dto.RegularPriceProductDTO;
import pisibg.ittalents.model.repository.ProductRepository;
import pisibg.ittalents.model.pojo.Product;
import pisibg.ittalents.model.repository.SubcategoryRepository;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private SubcategoryRepository subcategoryRepository;

    @GetMapping(value = "/products/all")
    public List<Product> getAll(){
        return productRepository.findAll();
    }

    @GetMapping(value = "/products/{id}")
    public Product getById(@PathVariable ("id") long id) throws ProductNotFoundException {
        if(productRepository.findById(id).isPresent()){
        return productRepository.findById(id).get();}
        else{
            throw new ProductNotFoundException("Product not found!");
        }
    }

    @GetMapping(value = "/products/filter")
    public List<Product> getAllByName(@RequestParam ("name")String name) throws ProductNotFoundException {
        List<Product> products = productRepository.findAllByNameLike("%"+name+"%");
        if(!(products.isEmpty())){
            return productRepository.findAllByNameLike("%"+name+"%");}
        else{
            throw new ProductNotFoundException("Product not found!");
        }
    }

    @PostMapping(value = "/products/")
    public ProductWithCategoryDTO save(@RequestBody RegularPriceProductDTO regularPriceProductDTO){
        //TODO validate properties!
        regularPriceProductDTO.setSubcategory(subcategoryRepository.getOne(regularPriceProductDTO.getSubcategoryId()));
        Product product = new Product(regularPriceProductDTO);
        productRepository.save(product);
        return new ProductWithCategoryDTO(product);
    }

    @DeleteMapping (value = "/products/{id}")
    public void removeProduct(@PathVariable ("id") long id, HttpServletResponse resp){
        if(!productRepository.existsById(id)){
            resp.setStatus(404);
        }
        else{
            productRepository.deleteById(id);
            resp.setStatus(200);
        }
    }


}
