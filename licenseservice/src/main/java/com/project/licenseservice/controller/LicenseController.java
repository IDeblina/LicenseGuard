package com.project.licenseservice.controller;


import com.project.licenseservice.dto.LicenseDto;
import com.project.licenseservice.service.LicenseService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/licenses")
@Slf4j //for logging
public class LicenseController {

    private LicenseService licenseService;

    @Autowired
    public LicenseController(LicenseService licenseService) {
        this.licenseService = licenseService;
    }

    //add licenses
    @PostMapping("/")
    public ResponseEntity<String> addLicense(@Valid @RequestBody LicenseDto licenseDto){
        licenseService.addLicense(licenseDto);
        log.info("license added to the db");
        return new ResponseEntity<>("License added suucessfully",HttpStatus.CREATED);
    }

    //for fetching expiring licenses
    @GetMapping("/expired")
    public ResponseEntity<List<LicenseDto>> getExpiredLiceses(){
        List<LicenseDto> expiredLicenses = licenseService.fetchExpiredLicenses();
        log.info("Fetched Expiring licenses from db");
        return ResponseEntity.ok(expiredLicenses);
    }

    //for fetching all licenses
    @GetMapping("/")
    public ResponseEntity<List<LicenseDto>> getLicenses(){
        return ResponseEntity.ok(licenseService.fetchLicenses());
    }

    //for fetching licenses by license id
    @GetMapping("/{licenseId}")
    public ResponseEntity<LicenseDto> getLicense(@PathVariable("licenseId") Long licenseId){
        LicenseDto license =licenseService.fetchLicenseById(licenseId);
        log.info("Fetched licenses from db");
        return ResponseEntity.ok(license);
    }
}
