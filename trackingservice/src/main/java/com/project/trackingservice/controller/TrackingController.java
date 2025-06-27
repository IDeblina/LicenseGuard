package com.project.trackingservice.controller;


import com.project.trackingservice.dto.LicenseDto;
import com.project.trackingservice.service.TrackingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/tracking")
public class TrackingController {

    private TrackingService trackingService;

    @Autowired
    public TrackingController(TrackingService trackingService) {
        this.trackingService = trackingService;
    }


    @GetMapping("/status")
    public ResponseEntity<List<LicenseDto>> getTrackingStatus() {
        List<LicenseDto> expiredLicenses = trackingService.getTrackingStatus();
        return ResponseEntity.ok(expiredLicenses);
    }
}
