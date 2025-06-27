package com.project.licenseservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.bind.DefaultValue;

import java.time.LocalDate;


@Data
@Getter
@Setter
public class LicenseDto {
    @NotNull(message = "Software name is mandatory")
    private String softwareName;

    @NotNull(message = "department name is mandatory")
    private String department;

    @NotNull(message = "purchase date is mandatory")
    private LocalDate purchaseDate;

    @NotNull(message = "expiry date is mandatory")
    private LocalDate expiryDate;

    @NotNull(message = "renewal status is mandatory")
    private Boolean renewed;

    @NotNull(message = "email is mandatory")
    @Email(message = "Email should be valid")
    private String email;
}
