package com.project.trackingservice.client;


import com.project.trackingservice.dto.LicenseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name="LICENSE-MICROSERVICE", url = "http://localhost:8081")
public interface LicenseServiceFeignClient {

    @GetMapping("/api/licenses/expired")
    ResponseEntity<List<LicenseDto>> getExpiredLicenses();
}
