package com.project.trackingservice.service;

import com.project.trackingservice.client.LicenseServiceFeignClient;
import com.project.trackingservice.constants.TrackingServiceConstants;
import com.project.trackingservice.dto.LicenseDto;
import com.project.trackingservice.dto.LicenseExpiryAlert;
import com.project.trackingservice.exceptions.ClientException;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class TrackingServiceImpl implements TrackingService{


    private LicenseServiceFeignClient licenseServiceFeignClient;
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    public TrackingServiceImpl(LicenseServiceFeignClient licenseServiceFeignClient,  KafkaTemplate<String, Object> template) {
        this.licenseServiceFeignClient = licenseServiceFeignClient;
        this.kafkaTemplate = template;
    }

    @Override
    // Retry and CircuitBreaker annotations with fallback method
    @Retry(name="LICENSE-MICROSERVICE", fallbackMethod = "fallbackLicenseMicroservice")
    public List<LicenseDto> getTrackingStatus() {
        ResponseEntity<List<LicenseDto>> response = licenseServiceFeignClient.getExpiredLicenses();
        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            log.info("Successfully fetched tracking status from License Service");
            return response.getBody();
        } else {
            log.warn("Failed to fetch tracking status: {}", response.getStatusCode());
            throw new ClientException("Failed to fetch tracking status from License Service");
        }
    }

    @Scheduled(cron = "0 25 2 * * ?") // Runs every day at 12:05 AM
    public void scheduledTrackingStatusCheck() {
        try {
            List<LicenseDto> expiredLicenses = getTrackingStatus();
            if (expiredLicenses.isEmpty()) {
                log.info("No expired licenses found.");
                return;
            }

            for (LicenseDto expiredLicense : expiredLicenses){
                LicenseExpiryAlert alert = new LicenseExpiryAlert();
                alert.setSoftwareName(expiredLicense.getSoftwareName());
                alert.setLicenseId(expiredLicense.getLicenseId());
                alert.setDepartment(expiredLicense.getDepartment());
                alert.setExpiryDate(expiredLicense.getExpiryDate());
                alert.setEmail(expiredLicense.getEmail());
                alert.setAlertTime(LocalDateTime.now());
                alert.setMessage("License will be expired in 7 day!");
                kafkaTemplate.send(TrackingServiceConstants.LICENSE_EXPIRY_ALERT, alert);
            }

            log.info("Expired licenses sent to Kafka topic: {}", TrackingServiceConstants.LICENSE_EXPIRY_ALERT);
        } catch (Exception e) {
            log.error("Error during scheduled tracking status check: {}", e.getMessage());
        }
    }

    // Fallback method when retries or circuit breaker is triggered
    public List<LicenseDto> fallbackLicenseMicroservice(Throwable t) {
        log.info("License Service is currently unavailable. Please try again later.");
        return List.of();
    }

}
