package com.ecommerce.order.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Document
public class Order {
    @Id
    private UUID id;
    private String name;
    private Integer noOfItems;
    private LocalDateTime orderPlacedAt;
    private Boolean isPaid;
}
