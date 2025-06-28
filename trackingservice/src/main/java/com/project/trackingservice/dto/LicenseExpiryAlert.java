package com.project.trackingservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class LicenseExpiryAlert {
    private String softwareName;
    private String department;
    private String licenseId;
    private LocalDate expiryDate;
    private String email;
    private String message;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime alertTime;
}
