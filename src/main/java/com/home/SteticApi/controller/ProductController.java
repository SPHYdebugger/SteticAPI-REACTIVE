package com.home.SteticApi.controller;

import com.home.SteticApi.domain.Client;
import com.home.SteticApi.domain.ErrorResponse;
import com.home.SteticApi.domain.Order;
import com.home.SteticApi.domain.Product;
import com.home.SteticApi.exception.ClientException.ClientNotFoundException;
import com.home.SteticApi.exception.ProductException.ProductNotFoundException;
import com.home.SteticApi.service.ProductService;
import jakarta.validation.Valid;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

@RestController
public class ProductController {

    @Autowired
    private ProductService productService;
    private Logger logger = LoggerFactory.getLogger(ProductController.class);


    @GetMapping("/products")
    public ResponseEntity<List<Product>> findAll(
            @RequestParam(defaultValue = "") String name,
            @RequestParam(defaultValue = "0") long productId,
            @RequestParam(defaultValue = "") String dangerous
    ) throws ProductNotFoundException {
        if (!name.isEmpty() && productId == 0) {
            List<Product> products = productService.findProductsByName(name);
            if (products.isEmpty()){
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } else {
                return new ResponseEntity<>(products, HttpStatus.OK);
            }
        } else if (name.isEmpty() && productId != 0) {
            Optional<Product> optionalProduct = productService.findProductById(productId);
            Product product = optionalProduct.orElseThrow(() -> new ProductNotFoundException(productId));
            return new ResponseEntity<>(Collections.singletonList(product), HttpStatus.OK);
        } else if (dangerous.equals("false") && productId == 0) {
            List<Product> dangerousProducts = productService.findDangerousProducts(false);
            return new ResponseEntity<>(dangerousProducts, HttpStatus.OK);
        } else if (dangerous.equals("true") && productId == 0) {
            List<Product> dangerousProducts = productService.findDangerousProducts(true);
            return new ResponseEntity<>(dangerousProducts, HttpStatus.OK);
        }
        List<Product> allProducts = productService.findAll();
        return new ResponseEntity<>(allProducts, HttpStatus.OK);
    }

    // Añadir un nuevo producto
    @PostMapping(value = "/products")
    public ResponseEntity<Product> addProduct(@Valid @RequestBody Product product) {
        productService.saveProduct(product);
        return new ResponseEntity<>(product, HttpStatus.CREATED);
    }


    // Obtener un producto por la ID
    @GetMapping("/product/{productId}")
    public ResponseEntity<Product> findById(@PathVariable long productId) throws ProductNotFoundException {
        Optional<Product> optionalProduct = productService.findProductById(productId);

        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();
            return new ResponseEntity<>(product, HttpStatus.OK);
        } else {
            throw new ProductNotFoundException(productId);
        }
    }


    // Eliminar un producto
    @DeleteMapping("/product/{productId}")
    public ResponseEntity<?> removeProduct(@PathVariable long productId) throws ProductNotFoundException {
        Optional<Product> optionalProduct = productService.findProductById(productId);
        if (optionalProduct.isPresent()) {
            productService.removeProduct(productId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            throw new ProductNotFoundException(productId);
        }
    }

    // Modificar un producto
    @PutMapping("/product/{productId}")
    public ResponseEntity<Product> modifyProduct(@Valid @RequestBody Product product, @PathVariable Long productId) throws ProductNotFoundException {
        Optional<Product> optionalProduct = productService.findProductById(productId);
        if (optionalProduct.isPresent()) {
            productService.modifyProduct(product, productId);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            throw new ProductNotFoundException(productId);
        }
    }






    // Controlar las excepciones
    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ErrorResponse> productNotFoundException(ProductNotFoundException pnfe) {
        ErrorResponse errorResponse = ErrorResponse.generalError(404, pnfe.getMessage());
        logger.error(pnfe.getMessage(), pnfe);
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleException(MethodArgumentNotValidException manve) {
        Map<String, String> errors = new HashMap<>();
        manve.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            errors.put(fieldName, message);
        });

        return ResponseEntity.badRequest().body(ErrorResponse.validationError(errors));
    }
}
