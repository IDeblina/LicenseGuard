package com.project.licenseservice.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "licenses")
public class License {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String softwareName;
    private String department;
    private LocalDate purchaseDate;
    private LocalDate expiryDate;
    private Boolean renewed;
    private String email;
}
