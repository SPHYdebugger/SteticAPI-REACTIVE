package com.home.SteticApi.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column
    private String number;
    @Column
    @NotNull(message = "Indica si es una compra OnLine o presencial")
    private boolean onlineOrder= false;
    @Column(name = "creation_date")
    @NotNull(message = "La fecha no puede ser nula")
    private String creationDate;
    @JsonBackReference("order_client")
    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @JsonBackReference("order_product")
    @ManyToMany
    @JoinTable(name = "products_orders",
        joinColumns = @JoinColumn(name = "product_id"),
        inverseJoinColumns = @JoinColumn(name = "order_id"))
    private List<Product> products;


}
