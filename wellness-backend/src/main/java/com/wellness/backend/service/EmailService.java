package com.wellness.backend.service;

import com.wellness.backend.model.Order;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    public void sendEmail(String to, String subject, String body) {

        System.out.println("Sending email to: " + to);
        System.out.println("Subject: " + subject);
        System.out.println("Body: " + body);
    }


    public void sendOrderStatusEmail(String to, Order order) {

        String subject = "Order Update: #" + order.getId();
        String body = """
                Hello,

                Your order status has been updated.

                Order ID: %d
                Status: %s
                Total Amount: %s

                Thank you for choosing Wellness Platform.
                """
                .formatted(
                        order.getId(),
                        order.getStatus(),
                        order.getTotalAmount()
                );

        sendEmail(to, subject, body);
    }
}
