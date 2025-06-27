package com.project.trackingservice.service;


import com.project.trackingservice.dto.LicenseDto;

import java.util.List;

public interface TrackingService {

    public List<LicenseDto> getTrackingStatus();
}
