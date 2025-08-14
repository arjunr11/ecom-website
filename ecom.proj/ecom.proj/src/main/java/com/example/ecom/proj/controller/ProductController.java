package com.example.ecom.proj.controller;

import com.example.ecom.proj.model.Product;
import com.example.ecom.proj.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class ProductController {

    @Autowired
    ProductService productService;

    @GetMapping ("/products")
    public ResponseEntity<List<Product>> getAllProducts(){
       return new ResponseEntity<>(productService.getAllProducts(), HttpStatus.OK);
    }

    @GetMapping ("/product/{prodId}")
    public ResponseEntity<Product> getAllProduct(@PathVariable int prodId){
        Product product = productService.getProductById(prodId);
        if(product != null){
            return new ResponseEntity<>(product,HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    @PostMapping("/product")
    public ResponseEntity<?> addProduct(@RequestPart Product product,
                                        @RequestPart MultipartFile imageFile){
    try {
        Product prod = productService.addProduct(product,imageFile);
        return new ResponseEntity<>(prod,HttpStatus.CREATED);
    }catch (Exception e){
        return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);

    }

    }


    @GetMapping("/product/{productId}/image")
    public ResponseEntity<byte[]> getIamgeByProductId(@PathVariable int productId){

        Product product = productService.getProductById(productId);
        byte[] imageFile = product.getImageDate();
        return ResponseEntity.ok().contentType(MediaType.valueOf(product.getImageType()))
                .body(imageFile);

    }

    @PutMapping("/product/{id}")
    public ResponseEntity<String> updateProduct(@PathVariable int id, @RequestPart Product product,
                                                @RequestPart MultipartFile imageFile) {
        Product prod = null;
        try {
            prod = productService.updateProduct(id,product, imageFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if(prod != null){
                return new ResponseEntity<>("Updated", HttpStatus.OK);
            }else{
                return new ResponseEntity<>("Failed to update", HttpStatus.BAD_REQUEST);
            }

    }


    @DeleteMapping("/product/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable int id){
        Product product = productService.getProductById(id);
        if(product!=null){
            productService.deleteProduct(id);
            return new ResponseEntity<>("Deleted",HttpStatus.OK);
        }else {
            return new ResponseEntity<>("Not Found",HttpStatus.NOT_FOUND);
        }
    }



    @GetMapping("/products/search")
    public ResponseEntity<List<Product>> deleteProduct(@RequestParam String keyword){
        List<Product> products = productService.searchProducts(keyword);
        return new ResponseEntity<>(products,HttpStatus.OK);
    }

}
