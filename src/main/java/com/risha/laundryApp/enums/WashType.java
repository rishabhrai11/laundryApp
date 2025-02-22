package com.risha.laundryApp.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum WashType {
    WASHING(10.0),
    IRONING(5.0),
    DRY_CLEANING(15.0);

    private double price;

}
