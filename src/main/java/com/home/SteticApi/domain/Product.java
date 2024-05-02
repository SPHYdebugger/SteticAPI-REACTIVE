package com.home.SteticApi.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column
    @NotNull(message = "El nombre no puede ser nulo")
    @NotBlank(message = "El nombre no puede estar en blanco")
    private String name;
    @Column
    private int size;
    @Column
    private String description;
    @Column
    @NotNull(message = "El precio no puede ser nulo")
    @Positive(message = "El precio debe ser mayor que cero")
    private float price;
    @Column(name = "registration_date")
    private String registrationDate;
    @Column
    @NotNull(message = "El campo dangerous no puede ser nula")
    private boolean dangerous = false;

    @ManyToMany(mappedBy = "products")
    private List<Order> orders;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String  getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(String registrationDate) {
        this.registrationDate = registrationDate;
    }

    public boolean isDangerous() {
        return dangerous;
    }

    public void setDangerous(boolean dangerous) {
        this.dangerous = dangerous;
    }
}
