package com.risha.laundryApp.service;

import com.risha.laundryApp.entity.Basket;
import com.risha.laundryApp.entity.User;
import com.risha.laundryApp.repository.BasketRepository;
import com.risha.laundryApp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class BasketService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BasketRepository basketRepository;
    public void addItem(User user, Basket basketItem){
        Basket basket = user.getBasket();
        if(basket == null){
            user.setBasket(new Basket());
        }
        user.getBasket().getItems().addAll(basketItem.getItems());
        basket = user.getBasket();
        basketRepository.save(basket);
        userRepository.save(user);
    }

    public Basket getBasket(User user){
        Basket basket = user.getBasket();
        if(basket != null){
            return basket;
        }
        else
            return null;
    }

    public void checkout(User user) {
        if (user.getBasket() != null) {
            Basket basket = user.getBasket();
            basket.getItems().clear();
            basketRepository.save(basket);
            userRepository.save(user);
        }
    }
}
