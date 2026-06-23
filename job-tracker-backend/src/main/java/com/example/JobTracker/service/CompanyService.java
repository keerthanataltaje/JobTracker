package com.example.JobTracker.service;

import com.example.JobTracker.domain.entity.Company;
import com.example.JobTracker.dto.CompanyRequestDto;
import com.example.JobTracker.dto.CompanyResponseDto;
import com.example.JobTracker.mapper.CompanyMapper;
import com.example.JobTracker.repository.CompanyRepository;
import com.example.JobTracker.repository.ContactRepository;
import com.example.JobTracker.repository.JobApplicationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CompanyService {
    private final CompanyRepository companyRepository;
    private final ContactRepository contactRepository;
    private final JobApplicationRepository jobApplicationRepository;
    private final CompanyMapper companyMapper;

    public CompanyService(CompanyRepository companyRepository, ContactRepository contactRepository, JobApplicationRepository jobApplicationRepository, CompanyMapper companyMapper) {
        this.companyRepository = companyRepository;
        this.contactRepository = contactRepository;
        this.jobApplicationRepository = jobApplicationRepository;
        this.companyMapper = companyMapper;
    }

    @Transactional(readOnly = true)
    public List<CompanyResponseDto> getAllCompanies() {
        return companyRepository.findAll().stream().map(companyMapper::toDto).toList();
    }

    @Transactional(readOnly = true)
    public CompanyResponseDto getCompanyByIdDto(Long id) {
        return companyMapper.toDto(getCompanyById(id));
    }

    @Transactional(readOnly = true)
    public Company getCompanyById(Long id) {
        return companyRepository.findById(id).orElseThrow(() -> new RuntimeException("Company not found with id: " + id));
    }

    @Transactional
    public CompanyResponseDto createCompany(CompanyRequestDto company) {
        if (company == null || company.name() == null || company.name().trim().isEmpty()) {
            throw new IllegalArgumentException("Company name cannot be blank");
        }

        return companyMapper.toDto(companyRepository.findByNameIgnoreCase(company.name()).orElseGet(() -> {
            return companyRepository.save(companyMapper.fromDto(company));
        }));

    }

    @Transactional
    public CompanyResponseDto updateCompany(Long id, CompanyRequestDto company) {
        if (company == null || company.name() == null || company.name().trim().isEmpty()) {
            throw new IllegalArgumentException("Company name cannot be null");
        }
        Company existing = getCompanyById(id);
        existing.setName(company.name().trim());
        return companyMapper.toDto(companyRepository.save(existing));
    }

    @Transactional
    public void deleteCompany(Long id) {
        if (!companyRepository.existsById(id)) {
            throw new RuntimeException("Company not found with id: " + id);
        }
        if (jobApplicationRepository.existsByCompanyId(id)) {
            throw new RuntimeException("Cannot delete company. It is linked to active job applications");
        }
        if (contactRepository.existsByCompanyId(id)) {
            throw new RuntimeException("Cannot delete company. It is linked to active contacts");
        }
        companyRepository.deleteById(id);
    }
}
