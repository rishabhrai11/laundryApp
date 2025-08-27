package com.risha.laundryApp.repository;

import com.risha.laundryApp.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    User findByUsername(String username);
}



