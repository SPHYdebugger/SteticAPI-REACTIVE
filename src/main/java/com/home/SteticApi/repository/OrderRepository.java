package com.home.SteticApi.repository;

import com.home.SteticApi.domain.Client;
import com.home.SteticApi.domain.Order;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.home.SteticApi.domain.Product;
import com.home.SteticApi.dto.OrderOutDto;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends CrudRepository<Order, Long> {

    //Query Methods
    List<Order> findAll();
    Optional<Order> findById(long id);

    List<Order> findByCreationDate(String date);

    //JPQL
    @Query("FROM orders o WHERE o.client.id = :clientId")
    List<Order> findByClientId(@Param("clientId") Long clientId);

    @Query("FROM orders o WHERE o.onlineOrder = :onlineOrder")
    List<Order> findByOnlineOrder(@Param("onlineOrder") boolean onlineOrder);

    @Query("FROM orders o WHERE o.number = :number")
    Order findByNumber(@Param("number") String number);

    List<Order> findByProducts(Product product);

    //esto es una prueba2
}
