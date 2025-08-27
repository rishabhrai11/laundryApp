package com.risha.laundryApp.model;

import com.risha.laundryApp.enums.BasketStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StatusChangeData {
    private BasketStatus basketStatus;
    private String email;
}
