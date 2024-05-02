package com.home.SteticApi.repository;

import com.home.SteticApi.domain.Product;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends CrudRepository<Product, Long> {
    List<Product> findAll();
    List<Product> findByName(String name);
    List<Product> findByDangerous(boolean dangerous);

    @Override
    Optional<Product> findById(Long id);
    List<Product> findByPrice(float price);

    Optional<Product> deleteById(long productId);


    List<Product> findByIdAndNameAndPrice(int id, String name, float price);
}
