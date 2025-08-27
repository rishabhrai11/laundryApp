package com.risha.laundryApp.controllers;

import com.risha.laundryApp.entity.Basket;
import com.risha.laundryApp.entity.User;
import com.risha.laundryApp.enums.BasketStatus;
import com.risha.laundryApp.service.BasketService;
import com.risha.laundryApp.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/basket")
@Slf4j
public class BasketController {
    @Autowired
    private BasketService basketService;

    @Autowired
    private UserService userService;

    @PostMapping("/add")
    public ResponseEntity<?> addToBasket(@RequestBody Basket basket) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            User user = userService.findByUsername(username);
            return basketService.addItem(user, basket);
        }
        catch(Exception e) {
            log.error("Something went wrong", e);
            return new ResponseEntity<>("Something went wrong", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/get-basket")
    public ResponseEntity<?> getUserBasket() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            User user = userService.findByUsername(username);
            Basket basket = user.getBasket();
            return new ResponseEntity<>(basket, HttpStatus.OK);
        }
        catch(Exception e) {
            log.error("Something went wrong", e);
            return new ResponseEntity<>("Could not get basket", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/checkout")
    public ResponseEntity<?> checkoutBasket() {
        try {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            User user = userService.findByUsername(username);
            basketService.checkout(user);
            return new ResponseEntity<>("Checkout successful!", HttpStatus.OK);
        } catch (Exception e) {
            log.error("Something went wrong", e);
            return new ResponseEntity<>("Checkout unsuccessful, something went wrong", HttpStatus.BAD_REQUEST);
        }

    }

    @PutMapping("/update-status/{username}")
    public ResponseEntity<?> updateBasketStatus(@PathVariable("username") String username, @RequestParam BasketStatus newStatus) {
        try {
            basketService.updateBasketStatus(username, newStatus);
            return new ResponseEntity<>("Update successful!", HttpStatus.OK);
        }
        catch (Exception e){
            log.error("Something went wrong", e);
            return new ResponseEntity<>("Something unexpected occurred", HttpStatus.BAD_REQUEST);
        }
    }


}
