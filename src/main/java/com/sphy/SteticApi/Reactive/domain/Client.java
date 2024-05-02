package com.sphy.SteticApi.Reactive.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(value = "clients")
public class Client {

    @Id
    private String id;
    @Field
    @NotNull(message = "El nombre no puede ser nulo")
    @NotBlank(message = "El nombre no puede estar en blanco")
    private String firstname;
    @Field
    private String lastname;
    @Field
    @NotNull(message = "El DNI no puede ser nulo")
    @NotBlank(message = "El DNI no puede estar en blanco")
    private String dni;
    @Field
    private String city;
    @Field
    private String street;
    @Field
    private int numHouse;
    @Field
    private int age;
    @Field
    private float height;
    @Field
    private String birthday;
    @Field
    @NotNull(message = "El campo VIP no puede ser nulo")
    private boolean VIP;


    @OneToMany(mappedBy = "client")
    private List<Order> orders;
}
