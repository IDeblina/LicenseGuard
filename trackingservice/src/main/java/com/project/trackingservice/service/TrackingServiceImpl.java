package com.project.trackingservice.service;

import com.project.trackingservice.client.LicenseServiceFeignClient;
import com.project.trackingservice.constants.TrackingServiceConstants;
import com.project.trackingservice.dto.LicenseDto;
import com.project.trackingservice.dto.LicenseExiperyAlert;
import com.project.trackingservice.exceptions.ClientException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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
    public List<LicenseDto> getTrackingStatus() {
        try {
            ResponseEntity<List<LicenseDto>> response = licenseServiceFeignClient.getExpiredLicenses();
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                log.info("Successfully fetched tracking status from License Service");
                return response.getBody();
            } else {
                log.warn("Failed to fetch tracking status: {}", response.getStatusCode());
                throw new ClientException("Failed to fetch tracking status from License Service");
            }
        } catch (Exception e) {
            log.error("Exception while fetching tracking status: {}", e.getMessage(), e);
            throw new ClientException("Exception while fetching tracking status from License Service");
        }
    }

    @Scheduled(cron = "0 0 22 * * ?") // Runs every day at 12:05 AM
    public void scheduledTrackingStatusCheck() {
        try {
            List<LicenseDto> expiredLicenses = getTrackingStatus();
            if (expiredLicenses.isEmpty()) {
                log.info("No expired licenses found.");
                return;
            }

            for (LicenseDto expiredLicense : expiredLicenses){
                LicenseExiperyAlert alert = new LicenseExiperyAlert();
                alert.setSoftwareName(expiredLicense.getSoftwareName());
                alert.setLicenceId(expiredLicense.getLicenceId());
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
}
