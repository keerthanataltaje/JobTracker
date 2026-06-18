package com.example.JobTracker.service;

import com.example.JobTracker.domain.entity.Company;
import com.example.JobTracker.repository.CompanyRepository;
import com.example.JobTracker.repository.ContactRepository;
import com.example.JobTracker.repository.JobApplicationRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyService {
    private final CompanyRepository companyRepository;
    private final ContactRepository contactRepository;
    private final JobApplicationRepository jobApplicationRepository;

    public CompanyService(CompanyRepository companyRepository, ContactRepository contactRepository, JobApplicationRepository jobApplicationRepository) {
        this.companyRepository = companyRepository;
        this.contactRepository = contactRepository;
        this.jobApplicationRepository = jobApplicationRepository;
    }

    @Transactional(readOnly = true)
    public List<Company> getAllCompanies() {
        return companyRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Company getCompanyById(Long id) {
        return companyRepository.findById(id).orElseThrow(() -> new RuntimeException("Company not found with id: " + id));
    }

    @Transactional
    public Company createCompany(Company company) {
        if (company == null || company.getName() == null || company.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Company name cannot be blank");
        }

        String sanitizedName = company.getName().trim();
        return companyRepository.findByNameIgnoreCase(sanitizedName).orElseGet(() -> {
            company.setName(sanitizedName);
            return companyRepository.save(company);
        });

    }

    @Transactional
    public Company updateCompany(Long id, Company company) {
        if(company==null || company.getName()==null|| company.getName().trim().isEmpty()){
            throw new IllegalArgumentException("Company name cannot be null");
        }
        Company existing = getCompanyById(id);
        existing.setName(company.getName().trim());
        return companyRepository.save(existing);
    }

    @Transactional
    public void deleteCompany(Long id) {
        if (!companyRepository.existsById(id)) {
            throw new RuntimeException("Company not found with id: " + id);
        }
        if(jobApplicationRepository.existsByCompanyId(id)){
            throw new RuntimeException("Cannot delete company. It is linked to active job applications");
        }
        if(contactRepository.existsByCompanyId(id)){
            throw new RuntimeException("Cannot delete company. It is linked to active contacts");
        }
        companyRepository.deleteById(id);
    }
}
