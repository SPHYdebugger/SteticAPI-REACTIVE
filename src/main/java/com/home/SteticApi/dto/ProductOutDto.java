package com.home.SteticApi.dto;

import java.time.LocalDate;
import java.util.List;

import com.home.SteticApi.domain.Client;
import com.home.SteticApi.domain.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductOutDto {

    private long id;
    private String name;
    private String description;
    private int size;
    private float price;
    private String  registrationDate;
    private boolean dangerous;



}
