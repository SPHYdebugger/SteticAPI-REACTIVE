package com.sphy.SteticApi.Reactive.service;

import com.sphy.SteticApi.Reactive.domain.Product;
import com.sphy.SteticApi.Reactive.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public Flux<Product> findAll() {
        return productRepository.findAll();
    }

    public Mono<Product> findProductById(String id) {
        return productRepository.findById(id);
    }

    public Flux<Product> findProductsByName(String name) {
        return productRepository.findByName(name);
    }

    public Flux<Product> findDangerousProducts(boolean dangerous) {
        return productRepository.findByDangerous(dangerous);
    }

    public Flux<Product> findProductsByPrice(float price) {
        return productRepository.findByPrice(price);
    }



    public Mono<Void> saveProduct(Product product) {
        product.setRegistrationDate(LocalDate.now().toString());
        return productRepository.save(product).then();
    }

    public Mono<Void> removeProduct(String productId) {
        return productRepository.deleteById(productId).then();
    }

    public Mono<Void> modifyProduct(Product newProduct, String productId) {
        return productRepository.findById(productId)
                .flatMap(existingProduct -> {
                    existingProduct.setName(newProduct.getName());
                    existingProduct.setSize(newProduct.getSize());
                    existingProduct.setDescription(newProduct.getDescription());
                    existingProduct.setPrice(newProduct.getPrice());
                    existingProduct.setDangerous(newProduct.isDangerous());
                    return productRepository.save(existingProduct);
                })
                .then();
    }
}
