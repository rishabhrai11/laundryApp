package com.risha.laundryApp.controllers;

import com.risha.laundryApp.entity.User;
import com.risha.laundryApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @GetMapping("/all-users")
    public ResponseEntity<?> getAll(){
        List<User> all  = userService.getAll();
        if(all != null && !all.isEmpty()){
            return new ResponseEntity<>(all, HttpStatus.OK);
        }
        return new ResponseEntity<>(all,HttpStatus.NO_CONTENT);
    }

    @PostMapping("/create-admin-user")
    public ResponseEntity<?> createAdminUser(@RequestBody User user){
        userService.saveAdmin(user);
        return new ResponseEntity<>(user,HttpStatus.CREATED);
    }


}

