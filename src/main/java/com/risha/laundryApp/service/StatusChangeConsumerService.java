package com.risha.laundryApp.service;

import com.risha.laundryApp.model.StatusChangeData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class StatusChangeConsumerService {
    @Autowired
    private EmailService emailService;

    @KafkaListener(topics = "basket-status-change", groupId = "basket-status-change-group")
    public void consume(StatusChangeData statusChangeData) {
        sendEmail(statusChangeData);
    }

    private void sendEmail(StatusChangeData statusChangeData) {
        emailService.sendEmail(statusChangeData.getEmail(),"Your laundry status is ", String.valueOf(statusChangeData.getBasketStatus()));
    }
}
