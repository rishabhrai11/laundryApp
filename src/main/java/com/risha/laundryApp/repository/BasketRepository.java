package com.risha.laundryApp.repository;

import com.risha.laundryApp.entity.Basket;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BasketRepository extends MongoRepository<Basket, String> {
}
