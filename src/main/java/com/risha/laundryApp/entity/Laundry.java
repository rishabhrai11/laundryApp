package com.risha.laundryApp.entity;

import lombok.*;
import org.springframework.data.annotation.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Laundry {

    private String name;
    private int quantity;
    private int price;
    private String washType; // "Washing", "Ironing", "Dry Cleaning"
}

