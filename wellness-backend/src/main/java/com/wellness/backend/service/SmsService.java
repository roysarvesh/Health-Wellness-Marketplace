package com.wellness.backend.service;
import org.springframework.stereotype.Service;
@Service
public class SmsService {
    public void sendSms(String phone, String msg) {
        System.out.println("SMS to " + phone + ": " + msg);
    }
}
