package com.sphy.SteticApi.Reactive.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(value = "orders")
public class Order {

    @Id
    private String  id;
    @Field
    private String number;
    @Field
    @NotNull(message = "Indica si es una compra OnLine o presencial")
    private boolean onlineOrder= false;
    @Field(name = "creation_date")
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
