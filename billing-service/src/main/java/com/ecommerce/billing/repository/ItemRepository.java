package com.ecommerce.billing.repository;

import com.ecommerce.billing.entity.Item;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ItemRepository extends MongoRepository<Item, UUID> {

}
