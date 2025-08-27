package com.risha.laundryApp.service;

import com.risha.laundryApp.entity.Basket;
import com.risha.laundryApp.entity.User;
import com.risha.laundryApp.enums.BasketStatus;
import com.risha.laundryApp.model.StatusChangeData;
import com.risha.laundryApp.repository.BasketRepository;
import com.risha.laundryApp.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@Slf4j
public class BasketService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private KafkaTemplate kafkaTemplate;

    @Autowired
    private BasketRepository basketRepository;
    public ResponseEntity<?> addItem(User user, Basket basketItem){
        Basket basket = user.getBasket();
        if(basket == null){
            user.setBasket(new Basket());
        }
        user.getBasket().getItems().addAll(basketItem.getItems());
        if(basket!=null)
            basket.setTotalPrice(
                    basket.getItems().stream()
                            .mapToDouble(laundry -> laundry.getWashType().getPrice() * laundry.getQuantity()) // Extract WashType price
                            .sum()
            );
        basket = user.getBasket();
        basketRepository.save(basket);
        userRepository.save(user);
        return new ResponseEntity<>("Added to your bucket\nTotal bucket price: "+basket.getTotalPrice(), HttpStatus.OK);
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

    @Scheduled(fixedRate = 3600000) // Runs every hour
    public void autoUpdateBasketStatus() {
        List<User> users = userRepository.findAll();
        LocalDateTime now = LocalDateTime.now();
        for (User user : users) {
            Basket basket = user.getBasket();
            long hoursPassed = ChronoUnit.HOURS.between(basket.getLastUpdated(), now);

            if (basket.getStatus() == BasketStatus.SUBMITTED && hoursPassed >= 1) {
                basket.setStatus(BasketStatus.PROCESSING);
                basket.setLastUpdated(now);
            } else if (basket.getStatus() == BasketStatus.PROCESSING && hoursPassed >= 2) {
                basket.setStatus(BasketStatus.IN_CLEANING);
                basket.setLastUpdated(now);
            } else if (basket.getStatus() == BasketStatus.IN_CLEANING && hoursPassed >= 3) {
                basket.setStatus(BasketStatus.READY_FOR_PICKUP);
                basket.setLastUpdated(now);
            } else if (basket.getStatus() == BasketStatus.READY_FOR_PICKUP && hoursPassed >= 5) {
                basket.setStatus(BasketStatus.COMPLETED);
                basket.setLastUpdated(now);
            }
            basketRepository.save(basket);
            userRepository.save(user);
            StatusChangeData statusChangeData = StatusChangeData.builder().
                    email(user.getEmail()).
                    basketStatus(user.getBasket().getStatus()).
                    build();
            try{
                kafkaTemplate.send("basket-status-change", user.getEmail(), statusChangeData);
            }
            catch(Exception e){
                log.error(e.getMessage());
            }
        }
    }

    public void updateBasketStatus(String username, BasketStatus newStatus) throws Exception {
        User user = userRepository.findByUsername(username);
        if(user == null){
            throw new Exception("User not found");
        }
        Basket basket = user.getBasket();
        if(basket != null) {
            basket.setStatus(newStatus);
            basket.setLastUpdated(LocalDateTime.now());
            basketRepository.save(basket);
            userRepository.save(user);
            StatusChangeData statusChangeData = StatusChangeData.builder()
                    .email(user.getEmail())
                    .basketStatus(basket.getStatus())
                    .build();
            kafkaTemplate.send("basket-status-change", user.getEmail(), statusChangeData);
        }
        else{
            throw new Exception("Basket not found");
        }
    }
}
