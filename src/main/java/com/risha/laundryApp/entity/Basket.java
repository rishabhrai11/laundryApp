package com.risha.laundryApp.entity;


import com.risha.laundryApp.enums.BasketStatus;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "baskets")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Basket {
    @Id
    private String id;
    private List<Laundry> items = new ArrayList<>();
    private double totalPrice;
    private BasketStatus status = BasketStatus.SUBMITTED;  // Default status
    private LocalDateTime lastUpdated = LocalDateTime.now();
}

