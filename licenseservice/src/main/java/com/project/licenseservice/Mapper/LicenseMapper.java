package com.project.licenseservice.Mapper;

import com.project.licenseservice.dto.LicenseDto;
import com.project.licenseservice.entity.License;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface LicenseMapper {
    License licenseDtoToLicense(LicenseDto licenseDto);
    LicenseDto licsenseToLicenseDto(License license);
}
