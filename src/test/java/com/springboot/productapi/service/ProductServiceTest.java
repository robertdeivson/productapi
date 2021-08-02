package com.springboot.productapi.service;

import com.springboot.productapi.builder.ProductDTOBuilder;
import com.springboot.productapi.dto.ProductDTO;
import com.springboot.productapi.entity.Product;
import com.springboot.productapi.exception.ProductAlreadyRegisteredException;
import com.springboot.productapi.exception.ProductNotFoundException;
import com.springboot.productapi.mapper.ProductMapper;
import com.springboot.productapi.repository.ProductRepository;
import net.bytebuddy.matcher.ElementMatcher;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static net.bytebuddy.matcher.ElementMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;


@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    private static final long INVALID_PRODUCT_ID = 1L;

    @Mock
    private ProductRepository productRepository;

    private ProductMapper productMapper = ProductMapper.INSTANCE;

    @InjectMocks
    private ProductService productService;

    @Test
    void whenProductInformedThenItShouldBeCreated() throws ProductAlreadyRegisteredException {
        ProductDTO expectedProductDTO = ProductDTOBuilder.builder().build().toProductDTO();
        Product expectedSavedProduct = productMapper.toModel(expectedProductDTO);

        Mockito.when(productRepository.findByName(expectedProductDTO.getName())).thenReturn(Optional.empty());
        Mockito.when(productRepository.save(expectedSavedProduct)).thenReturn(expectedSavedProduct);

        ProductDTO createdProductDTO = productService.createrProduct(expectedProductDTO);

        assertThat(createdProductDTO.getId(), Matchers.is(equalTo(expectedProductDTO.getId())));
        assertThat(createdProductDTO.getName(), Matchers.is(equalTo(expectedProductDTO.getName())));

    }

    @Test
    void whenValidProductNameIsGivenThenReturnAProduct() throws ProductNotFoundException {
        ProductDTO expectedFoundProductDTO = ProductDTOBuilder.builder().build().toProductDTO();
        Product expectedFoundProduct = productMapper.toModel(expectedFoundProductDTO);

        Mockito.when(productRepository.findByName(expectedFoundProduct.getName())).thenReturn(Optional.of(expectedFoundProduct));

        ProductDTO foundProductDTO = productService.findByName(expectedFoundProductDTO.getName());

        assertThat(foundProductDTO, Matchers.is(equalTo(expectedFoundProductDTO)));
    }

    @Test
    void whenNotRegisteredProductNameIsGivenThenThrowAnException() {

        ProductDTO expectedFoundProductDTO = ProductDTOBuilder.builder().build().toProductDTO();


        when(productRepository.findByName(expectedFoundProductDTO.getName())).thenReturn(Optional.empty());


        assertThrows(ProductNotFoundException.class, () -> productService.findByName(expectedFoundProductDTO.getName()));
    }

    @Test
    void whenListProductIsCalledThenReturnAListOfProducts() {

        ProductDTO expectedFoundProductDTO = ProductDTOBuilder.builder().build().toProductDTO();
        Product expectedFoundProduct = productMapper.toModel(expectedFoundProductDTO);


        when(productRepository.findAll()).thenReturn(Collections.singletonList(expectedFoundProduct));


        List<ProductDTO> foundListProductDTO = productService.listAll();

        assertThat(foundListProductDTO, Matchers.is(not(empty())));
        assertThat(foundListProductDTO.get(0), Matchers.is(equalTo(expectedFoundProductDTO)));
    }

    @Test
    void whenListProductIsCalledThenReturnAnEmptyListOfProducts() {

        when(productRepository.findAll()).thenReturn(Collections.EMPTY_LIST);


        List<ProductDTO> foundListProductsDTO = productService.listAll();

        assertThat(foundListProductsDTO, Matchers.is(empty()));
    }

    @Test
    void whenExclusionIsCalledWithValidIdThenAProductShouldBeDeleted() throws ProductNotFoundException {

        ProductDTO expectedDeletedProductDTO = ProductDTOBuilder.builder().build().toProductDTO();
        Product expectedDeletedProduct = productMapper.toModel(expectedDeletedProductDTO);


        when(productRepository.findById(expectedDeletedProductDTO.getId())).thenReturn(Optional.of(expectedDeletedProduct));
        doNothing().when(productRepository).deleteById(expectedDeletedProductDTO.getId());


        productService.deleteById(expectedDeletedProductDTO.getId());

        verify(productRepository, times(1)).findById(expectedDeletedProductDTO.getId());
        verify(productRepository, times(1)).deleteById(expectedDeletedProductDTO.getId());
    }





}
