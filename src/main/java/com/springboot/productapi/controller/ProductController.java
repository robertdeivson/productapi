package com.springboot.productapi.controller;

import com.springboot.productapi.dto.ProductDTO;
import com.springboot.productapi.exception.ProductAlreadyRegisteredException;
import com.springboot.productapi.exception.ProductNotFoundException;
import com.springboot.productapi.service.ProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@AllArgsConstructor(onConstructor = @__(@Autowired))

public class ProductController {

    private final ProductService productService;


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductDTO createProduct(@RequestBody @Valid ProductDTO productDTO) throws ProductAlreadyRegisteredException {
        return productService.createrProduct(productDTO);
    }


    @GetMapping("/{name}")
    public ProductDTO findByName(@PathVariable String name) throws ProductNotFoundException {
        return productService.findByName(name);
    }


    @GetMapping
    public List<ProductDTO> listProducts(){
        return productService.listAll();
    }


    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) throws ProductNotFoundException {
        productService.deleteById(id);
    }
}
