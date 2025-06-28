package com.project.emailservice.service;

import com.project.emailservice.dto.LicenseExiperyAlert;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @KafkaListener(topics = "license-expiry-alerts", groupId = "email-service-group")
    public void consumeExpiryAlert(LicenseExiperyAlert alert) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(alert.getEmail());
        message.setSubject("License Expiry Notice: " + alert.getSoftwareName());
        message.setText("Hello,\n\nYour license for " + alert.getSoftwareName() +
                " in " + alert.getDepartment() + " is expiring on " + alert.getExpiryDate() + ".\n\nRegards,\nLicenseGuard");
        mailSender.send(message);
        log.info("📧 Email sent to: " + alert.getEmail());
    }
}
