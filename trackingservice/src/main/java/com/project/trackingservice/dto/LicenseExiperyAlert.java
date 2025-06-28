package com.project.trackingservice.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class LicenseExiperyAlert {
    private String softwareName;
    private String department;
    private String licenceId;
    private LocalDate expiryDate;
    private String email;
    private String message;
    private LocalDateTime alertTime;
}
