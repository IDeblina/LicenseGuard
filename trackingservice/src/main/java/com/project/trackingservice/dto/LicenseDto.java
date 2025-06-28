package com.project.trackingservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class LicenseDto {
    private String softwareName;
    private String department;
    private String licenseId;
    private LocalDate purchaseDate;
    private LocalDate expiryDate;
    private Boolean renewed;
    private String email;
}
