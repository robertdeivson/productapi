package com.springboot.productapi.service;

import com.springboot.productapi.dto.ProductDTO;
import com.springboot.productapi.entity.Product;
import com.springboot.productapi.exception.ProductAlreadyRegisteredException;
import com.springboot.productapi.exception.ProductNotFoundException;
import com.springboot.productapi.mapper.ProductMapper;
import com.springboot.productapi.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class ProductService {


    private final ProductRepository productRepository;
    private final ProductMapper productMapper = ProductMapper.INSTANCE;

    public ProductDTO createrProduct(ProductDTO productDTO) throws ProductAlreadyRegisteredException {
        verifyIfIsAlreadyRegistered(productDTO.getName());
        Product product = productMapper.toModel(productDTO);
        Product savedProduct = productRepository.save(product);
        return productMapper.toDTO(savedProduct);
    }

    public ProductDTO findByName(String name) throws ProductNotFoundException {
        Product foundProduct = productRepository.findByName(name)
                .orElseThrow(() -> new ProductNotFoundException(name));
        return productMapper.toDTO(foundProduct);
    }

    public List<ProductDTO> listAll(){
        return productRepository.findAll()
                .stream()
                .map(productMapper::toDTO)
                .collect(Collectors.toList());
    }

    public void deleteById(Long id) throws ProductNotFoundException {
        verifyIfExists(id);
        productRepository.deleteById(id);
    }

    private void verifyIfIsAlreadyRegistered(String name) throws ProductAlreadyRegisteredException {
        Optional<Product> optSavedBeer = productRepository.findByName(name);
        if (optSavedBeer.isPresent()) {
            throw new ProductAlreadyRegisteredException(name);
        }
    }

    private Product verifyIfExists(Long id) throws ProductNotFoundException {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }


}
