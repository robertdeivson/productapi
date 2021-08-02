package com.springboot.productapi.builder;

import com.springboot.productapi.dto.ProductDTO;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public class ProductDTOBuilder {

    @Builder.Default
    private Long id = 1L;

    @Builder.Default
    private String name = "Chocolate";

    @Builder.Default
    private int quantity = 10;

    @Builder.Default
    private Double amount = 30.0;

    public ProductDTO toProductDTO(){
        return new ProductDTO(id,
                name,
                quantity,
                amount);
    }
}
