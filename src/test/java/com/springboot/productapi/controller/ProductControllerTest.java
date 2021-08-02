package com.springboot.productapi.controller;

import com.springboot.productapi.builder.ProductDTOBuilder;
import com.springboot.productapi.dto.ProductDTO;
import com.springboot.productapi.exception.ProductNotFoundException;
import com.springboot.productapi.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import java.util.Collections;


import static com.springboot.productapi.utils.JsonConvertionUtils.asJsonString;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.mock.http.server.reactive.MockServerHttpRequest.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class ProductControllerTest {

    private static final String PRODUCT_API_URL_PATH = "/api/v1/products";
    private static final long VALID_PRODUCT_ID = 1L;
    private static final long INVALID_PRODUCT_ID = 2L;

    private MockMvc mockMvc;

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(productController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setViewResolvers((s, locale) -> new MappingJackson2JsonView())
                .build();
    }

    @Test
    void whenPOSTIsCalledThenAProductIsCreated() throws Exception {

        ProductDTO productDTO = ProductDTOBuilder.builder().build().toProductDTO();


        when(productService.createrProduct(productDTO)).thenReturn(productDTO);


        mockMvc.perform(post(PRODUCT_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(productDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is(productDTO.getName())))
                .andExpect(jsonPath("$.quantity", is(productDTO.getQuantity())))
                .andExpect(jsonPath("$.amount", is(productDTO.getAmount())));
    }
    @Test
    void whenPOSTIsCalledWithoutRequiredFieldThenAnErrorIsReturned() throws Exception {

        ProductDTO productDTO = ProductDTOBuilder.builder().build().toProductDTO();
        productDTO.setName(null);


        mockMvc.perform(post(PRODUCT_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(productDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenGETIsCalledWithValidNameThenOkStatusIsReturned() throws Exception {

        ProductDTO productDTO = ProductDTOBuilder.builder().build().toProductDTO();


        when(productService.findByName(productDTO.getName())).thenReturn(productDTO);


        mockMvc.perform(MockMvcRequestBuilders.get(PRODUCT_API_URL_PATH + "/" + productDTO.getName())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(productDTO.getName())))
                .andExpect(jsonPath("$.quantity", is(productDTO.getQuantity())))
                .andExpect(jsonPath("$.amount", is(productDTO.getAmount())));
    }

    @Test
    void whenGETIsCalledWithoutRegisteredNameThenNotFoundStatusIsReturned() throws Exception {

        ProductDTO productDTO = ProductDTOBuilder.builder().build().toProductDTO();


        when(productService.findByName(productDTO.getName())).thenThrow(ProductNotFoundException.class);


        mockMvc.perform(MockMvcRequestBuilders.get(PRODUCT_API_URL_PATH + "/" + productDTO.getName())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenGETListWithProductsIsCalledThenOkStatusIsReturned() throws Exception {

        ProductDTO productDTO = ProductDTOBuilder.builder().build().toProductDTO();


        when(productService.listAll()).thenReturn(Collections.singletonList(productDTO));


        mockMvc.perform(MockMvcRequestBuilders.get(PRODUCT_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", is(productDTO.getName())))
                .andExpect(jsonPath("$[0].quantity", is(productDTO.getQuantity())))
                .andExpect(jsonPath("$[0].amount", is(productDTO.getAmount())));
    }

    @Test
    void whenGETListWithoutProductsIsCalledThenOkStatusIsReturned() throws Exception {

        ProductDTO productDTO = ProductDTOBuilder.builder().build().toProductDTO();


        when(productService.listAll()).thenReturn(Collections.singletonList(productDTO));


        mockMvc.perform(MockMvcRequestBuilders.get(PRODUCT_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void whenDELETEIsCalledWithValidIdThenNoContentStatusIsReturned() throws Exception {

        ProductDTO productDTO = ProductDTOBuilder.builder().build().toProductDTO();


        doNothing().when(productService).deleteById(productDTO.getId());


        mockMvc.perform(MockMvcRequestBuilders.delete(PRODUCT_API_URL_PATH + "/" + productDTO.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void whenDELETEIsCalledWithInvalidIdThenNotFoundStatusIsReturned() throws Exception {

        doThrow(ProductNotFoundException.class).when(productService).deleteById(INVALID_PRODUCT_ID);


        mockMvc.perform(MockMvcRequestBuilders.delete(PRODUCT_API_URL_PATH + "/" + INVALID_PRODUCT_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

}
