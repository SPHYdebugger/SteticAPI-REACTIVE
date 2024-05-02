package com.sphy.SteticApi.Reactive.repository;


import com.sphy.SteticApi.Reactive.domain.Product;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface ProductRepository extends ReactiveMongoRepository<Product,String> {
    Flux<Product> findAll();
    Flux<Product> findByName(String name);
    Flux<Product> findByDangerous(boolean dangerous);

    @Override
    Mono<Product> findById(String id);
    Flux<Product> findByPrice(float price);

    Mono<Void> deleteById(String productId);



}
