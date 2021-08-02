package com.springboot.productapi.exception;

public class ProductAlreadyRegisteredException extends Exception {

    public ProductAlreadyRegisteredException(String productName){
        super(String.format("Product with name %s already registered in the system.", productName));
    }
}
