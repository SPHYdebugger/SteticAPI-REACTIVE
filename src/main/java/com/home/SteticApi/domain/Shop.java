package com.home.SteticApi.domain;


import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "shops")
public class Shop {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column
    @NotNull(message = "El nombre no puede ser nulo")
    @NotBlank(message = "El nombre no puede estar en blanco")
    private String name;
    @Column
    private String address;
    @Column
    private int employeesNumber;
    @Column
    @NotNull(message = "La ciudad no puede ser nula")
    @NotBlank(message = "La ciudad no puede estar en blanco")
    private String city;
    @Column
    private float meters;
    @Column
    @NotNull(message = "El campo solarium no puede ser nulo")
    private boolean solarium = false;
    @Column
    private String openDate;
    @Column
    private double latitude;
    @Column
    private double longitude;


    @OneToMany(mappedBy = "shop")
    private List<Employee> employees;


    public Shop(String name, String address, String city, boolean solarium, double latitude, double longitude) {
        this.name = name;
        this.address = address;
        this.city = city;
        this.solarium = solarium;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
