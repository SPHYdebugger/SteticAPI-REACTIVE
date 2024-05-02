package com.home.SteticApi.dto;

import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderInDto {

    private boolean onlineOrder;

    private List<Long> productIds;
}
