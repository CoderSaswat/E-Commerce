package com.ecommerce.billing.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Getter
@Setter
@Document
public class Item {
    @Id
    private UUID id;
    private String name;
    private Integer price;
}
