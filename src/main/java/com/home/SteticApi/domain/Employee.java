package com.home.SteticApi.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "employees")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column
    @NotNull(message = "El nombre no puede ser nulo")
    @NotBlank(message = "El nombre no puede estar en blanco")
    private String name;
    @Column
    @NotNull(message = "El DNI no puede ser nulo")
    @NotBlank(message = "El DNI no puede estar en blanco")
    private String dni;
    @Column
    private int age;
    @Column
    private float height;
    @Column
    @NotNull(message = "El campo AcademicTitle no puede ser nulo")
    private boolean academicTitle = false;
    @Column(name = "register_date")
    private String registerDate;

    @JsonBackReference("employee_shop")
    @ManyToOne
    @JoinColumn(name = "shop_id")
    private Shop shop;


}
