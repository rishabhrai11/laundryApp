package com.risha.laundryApp.repository;

import com.risha.laundryApp.entity.Laundry;
import com.risha.laundryApp.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface LaundryRepository extends MongoRepository<Laundry, String> {
}
