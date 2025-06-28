package com.project.licenseservice.repository;

import com.project.licenseservice.entity.License;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LicenseRepository extends JpaRepository<License,Long>{
    boolean existsBySoftwareName(String softwareName);
}
