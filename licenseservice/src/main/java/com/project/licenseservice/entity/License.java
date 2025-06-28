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

    @Column(name = "Licenseid",unique = true, nullable = false, length = 11)
    private String licenseId; // 10-digit unique ID

    @Column(name = "Softwarename", nullable = false)
    private String softwareName;

    @Column(name = "Department", nullable = false)
    private String department;

    @Column(name = "Purchasedate", nullable = false)
    private LocalDate purchaseDate;

    @Column(name = "Expirydate", nullable = false)
    private LocalDate expiryDate;

    @Column(name = "Renewed", nullable = false)
    private Boolean renewed;

    @Column(name = "Email", nullable = false)
    private String email;
}
