package com.example.JobTracker.mapper;

import com.example.JobTracker.domain.entity.Company;
import com.example.JobTracker.dto.CompanyRequestDto;
import com.example.JobTracker.dto.CompanyResponseDto;
import org.springframework.stereotype.Component;

@Component
public class CompanyMapper {
    public CompanyResponseDto toDto(Company company) {
        if (company == null) {
            return null;
        }
        return new CompanyResponseDto(company.getId(), company.getName());
    }

    public Company fromDto(CompanyRequestDto companyRequestDto) {
        Company company = new Company();
        company.setName(companyRequestDto.name());
        return company;
    }
}
