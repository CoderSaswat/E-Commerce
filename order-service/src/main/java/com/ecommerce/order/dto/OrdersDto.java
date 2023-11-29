package com.ecommerce.order.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrdersDto {
    private UUID id;
    private String name;
    private Integer price;
    private Integer noOfItems;
    private LocalDateTime orderPlacedAt;
    private Boolean isPaid;
}
