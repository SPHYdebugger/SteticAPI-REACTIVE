package com.sphy.SteticApi.Reactive.controller;


import com.sphy.SteticApi.Reactive.domain.ErrorResponse;
import com.sphy.SteticApi.Reactive.domain.Product;
import com.sphy.SteticApi.Reactive.exception.ProductException.ProductNotFoundException;
import com.sphy.SteticApi.Reactive.service.ProductService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class ProductController {

    @Autowired
    private ProductService productService;
    private Logger logger = LoggerFactory.getLogger(ProductController.class);


    @GetMapping("/products")
    public ResponseEntity<List<Product>> findAll(
            @RequestParam(defaultValue = "") String name,
            @RequestParam(defaultValue = "0") String productId,
            @RequestParam(defaultValue = "") String dangerous
    ) throws ProductNotFoundException {
        if (!name.isEmpty() && productId.equals("0")) {
            Flux<Product> products = productService.findProductsByName(name);
            List<Product> productList = products.collectList().block();
            if (productList.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } else {
                return new ResponseEntity<>(productList, HttpStatus.OK);
            }
        } else if (name.isEmpty() && !productId.equals("0")) {
            Mono<Product> productMono = productService.findProductById(productId);
            Product product = productMono.blockOptional().orElseThrow(() -> new ProductNotFoundException(productId));
            return new ResponseEntity<>(Collections.singletonList(product), HttpStatus.OK);
        } else if (dangerous.equals("false") && productId.equals("0")) {
            Flux<Product> dangerousProducts = productService.findDangerousProducts(false);
            List<Product> productList = dangerousProducts.collectList().block();
            return new ResponseEntity<>(productList, HttpStatus.OK);
        } else if (dangerous.equals("true") && productId.equals("0")) {
            Flux<Product> dangerousProducts = productService.findDangerousProducts(true);
            List<Product> productList = dangerousProducts.collectList().block();
            return new ResponseEntity<>(productList, HttpStatus.OK);
        }
        Flux<Product> allProducts = productService.findAll();
        List<Product> productList = allProducts.collectList().block();
        return new ResponseEntity<>(productList, HttpStatus.OK);
    }


    // Añadir un nuevo producto
    @PostMapping(value = "/products")
    public ResponseEntity<Product> addProduct(@Valid @RequestBody Product product) {
        productService.saveProduct(product);
        return new ResponseEntity<>(product, HttpStatus.CREATED);
    }


    // Obtener un producto por la ID
    @GetMapping("/product/{productId}")
    public ResponseEntity<Product> findById(@PathVariable String productId) throws ProductNotFoundException {
        Mono<Product> productMono = productService.findProductById(productId);
        Product product = productMono.blockOptional().orElseThrow(() -> new ProductNotFoundException(productId));
        return new ResponseEntity<>(product, HttpStatus.OK);
    }



    /// Eliminar un producto
    @DeleteMapping("/product/{productId}")
    public ResponseEntity<?> removeProduct(@PathVariable String productId) throws ProductNotFoundException {
        Mono<Product> productMono = productService.findProductById(productId);
        Product product = productMono.blockOptional().orElseThrow(() -> new ProductNotFoundException(productId));

        productService.removeProduct(productId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    // Modificar un producto
    @PutMapping("/product/{productId}")
    public ResponseEntity<Product> modifyProduct(@Valid @RequestBody Product product, @PathVariable String productId) throws ProductNotFoundException {
        Mono<Product> productMono = productService.findProductById(productId);
        Product existingProduct = productMono.blockOptional().orElseThrow(() -> new ProductNotFoundException(productId));

        productService.modifyProduct(product, productId);

        return new ResponseEntity<>(HttpStatus.OK);
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
