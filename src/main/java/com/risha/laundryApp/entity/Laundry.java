package com.risha.laundryApp.entity;

import com.risha.laundryApp.enums.WashType;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Laundry {

    private String name;
    private int quantity;
    private WashType washType; // "Washing", "Ironing", "Dry Cleaning"
}

