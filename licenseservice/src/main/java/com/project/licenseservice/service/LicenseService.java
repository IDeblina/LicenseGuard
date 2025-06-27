package com.project.licenseservice.service;

import com.project.licenseservice.dto.LicenseDto;

import java.util.List;

public interface LicenseService {

    void addLicense(LicenseDto licenseDto);
    LicenseDto fetchLicenseById(Long id);
    List<LicenseDto> fetchLicenses();


    //custom
    List<LicenseDto> fetchExpiredLicenses();

}
