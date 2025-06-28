package com.project.licenseservice.service;


import com.project.licenseservice.exception.ResourceAlreadyExistsException;
import com.project.licenseservice.mapper.LicenseMapper;
import com.project.licenseservice.dto.LicenseDto;
import com.project.licenseservice.entity.License;
import com.project.licenseservice.exception.ResourceNotFoundException;
import com.project.licenseservice.repository.LicenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LicenseServiceImpl implements LicenseService{

    private LicenseRepository licenseRepository;

    private LicenseMapper mapper;

    @Autowired
    public LicenseServiceImpl(LicenseRepository licenseRepository, LicenseMapper mapper) {
        this.licenseRepository = licenseRepository;
        this.mapper = mapper;

    }

    @Override
    public void addLicense(LicenseDto licenseDto) {
        boolean exists = licenseRepository.existsBySoftwareName(licenseDto.getSoftwareName());
        if (exists) {
            throw new ResourceAlreadyExistsException("Software with name " + licenseDto.getSoftwareName() + " already exists.");
        }
        String letters = "";
        for (int i = 0; i < 3; i++) {
            letters += (char) ('A' + (int)(Math.random() * 26));
        }
        String numbers = String.format("%07d", (long)(Math.random() * 10_000_000L));
        String licenseId = letters + "-" + numbers;
        licenseDto.setLicenseId(licenseId);

        License license = mapper.licenseDtoToLicense(licenseDto);
        // Set the purchase date to today if not provided
        licenseRepository.save(license);
    }

    /**
     * Fetches a license by its ID.
     *
     * @param id the ID of the license to fetch
     * @return the LicenseDto object
     * @throws ResourceNotFoundException if the license is not found
     * @implNote This method logs the ID in the exception message.
     * Note: Make sure the ID exists in the database.
     */
    @Override
    public LicenseDto fetchLicenseById(Long id) {
        License license = licenseRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("license not found with id "+ id));
        return mapper.licenseToLicenseDto(license);
    }

    @Override
    public List<LicenseDto> fetchLicenses() {

        List<License> licenses = licenseRepository.findAll();

        return licenses.stream()
                .map(license->mapper.licenseToLicenseDto(license))
                .collect(Collectors.toList());
    }


    //very important method
    @Override
    public List<LicenseDto> fetchExpiredLicenses() {
        //fetch all the list of liceses frm db
        List<License> licenses = licenseRepository.findAll();


        //filter this list and find the expires list of licenses
        LocalDate today = LocalDate.now();

        List<License> expiringList = licenses.stream().filter(license -> {
            LocalDate expiryDate = license.getExpiryDate();
            long daysBetween = expiryDate.toEpochDay() - today.toEpochDay();
            return daysBetween == 7;
        }).collect(Collectors.toList());

        //return list of expired licenses
        List<LicenseDto> expiredLicenses =  expiringList.stream()
                .map(expired->mapper.licenseToLicenseDto(expired))
                .collect(Collectors.toList());

        return expiredLicenses;
    }
}
