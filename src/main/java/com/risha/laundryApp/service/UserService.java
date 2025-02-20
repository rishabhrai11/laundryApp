package com.risha.laundryApp.service;

import com.risha.laundryApp.entity.Basket;
import com.risha.laundryApp.entity.User;
import com.risha.laundryApp.repository.BasketRepository;
import com.risha.laundryApp.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BasketRepository basketRepository;

    @Autowired
    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public void saveUser(User user) {
        try {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setRoles(Arrays.asList("USER"));
            Basket basket = new Basket();
            basketRepository.save(basket);
            user.setBasket(basket);
            userRepository.save(user);
        }
        catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public void updatePassword(String username, String newPassword) {
        try {
            User user = userRepository.findByUsername(username);
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
        }
        catch (Exception e) {
            log.error("User not found",e);
        }
    }

    public void saveAdmin(User user) {
        user.setRoles(Arrays.asList("USER","ADMIN"));
        userRepository.save(user);
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

}
