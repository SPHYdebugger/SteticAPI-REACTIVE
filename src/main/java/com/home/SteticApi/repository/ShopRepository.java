package com.home.SteticApi.repository;


import com.home.SteticApi.domain.Shop;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShopRepository extends CrudRepository<Shop, Long> {
    List<Shop> findAll();

    List<Shop> findByName(String name);

    Optional<Shop> findById(long id);
    List<Shop> findByCity(String city);
    List<Shop> findBySolarium(boolean solarium);

    void deleteById(long id);
}
