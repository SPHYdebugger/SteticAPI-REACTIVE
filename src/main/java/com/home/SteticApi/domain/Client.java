package com.home.SteticApi.domain;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "clients")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column
    @NotNull(message = "El nombre no puede ser nulo")
    @NotBlank(message = "El nombre no puede estar en blanco")
    private String firstname;
    @Column
    private String lastname;
    @Column
    @NotNull(message = "El DNI no puede ser nulo")
    @NotBlank(message = "El DNI no puede estar en blanco")
    private String dni;
    @Column
    private String city;
    @Column
    private String street;
    @Column
    private int numHouse;
    @Column
    private int age;
    @Column
    private float height;
    @Column
    private String birthday;
    @Column
    @NotNull(message = "El campo VIP no puede ser nulo")
    private boolean VIP;


    @OneToMany(mappedBy = "client")
    private List<Order> orders;
}
