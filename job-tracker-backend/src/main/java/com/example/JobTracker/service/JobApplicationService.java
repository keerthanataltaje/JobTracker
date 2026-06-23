package com.example.JobTracker.service;

import com.example.JobTracker.domain.entity.ApplicationStatus;
import com.example.JobTracker.domain.entity.Company;
import com.example.JobTracker.domain.entity.Contact;
import com.example.JobTracker.domain.entity.JobApplication;
import com.example.JobTracker.dto.*;
import com.example.JobTracker.mapper.JobApplicationMapper;
import com.example.JobTracker.repository.JobApplicationRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class JobApplicationService {
    private final JobApplicationRepository jobApplicationRepository;
    private final CompanyService companyService;
    private final ContactService contactService;
    private final JobApplicationMapper jobApplicationMapper;

    public JobApplicationService(JobApplicationRepository jobApplicationRepository, CompanyService companyService, ContactService contactService, JobApplicationMapper jobApplicationMapper) {
        this.jobApplicationRepository = jobApplicationRepository;
        this.companyService = companyService;
        this.contactService = contactService;
        this.jobApplicationMapper = jobApplicationMapper;
    }

    @Transactional(readOnly = true)
    public List<JobApplicationResponseDto> getAllApplications() {
        return jobApplicationRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt")).stream().map(jobApplicationMapper::toDto).toList();
    }

    @Transactional(readOnly = true)
    public JobApplicationResponseDto getApplicationByIdDto(Long id) {
        return jobApplicationMapper.toDto(getApplicationById(id));
    }

    @Transactional(readOnly = true)
    public JobApplication getApplicationById(Long id) {
        return jobApplicationRepository.findById(id).orElseThrow(() -> new RuntimeException("Job Application not found with id:" + id));
    }

    @Transactional(readOnly = true)
    public List<JobApplicationResponseDto> getApplicationsByCompany(Long companyId) {
        return jobApplicationRepository.findByCompanyId(companyId).stream().map(jobApplicationMapper::toDto).toList();
    }

    @Transactional(readOnly = true)
    public List<JobApplicationResponseDto> getApplicationsByStatus(ApplicationStatus applicationStatus) {
        return jobApplicationRepository.findByStatus(applicationStatus).stream().map(jobApplicationMapper::toDto).toList();
    }

    @Transactional(readOnly = true)
    public List<JobApplicationResponseDto> getOverdueFollowups() {
        return jobApplicationRepository.findByFollowUpDateBefore(LocalDate.now()).stream().map(jobApplicationMapper::toDto).toList();
    }

    @Transactional
    public JobApplicationResponseDto createApplication(JobApplicationRequestDto application) {
        if (application.companyName() == null || application.companyName().trim().isEmpty()) {
            throw new IllegalArgumentException("Job application must be associated with a valid company");
        }

        CompanyResponseDto resolvedCompanyDto = companyService.createCompany(new CompanyRequestDto(application.companyName()));
        Company managedCompany = companyService.getCompanyById(resolvedCompanyDto.id());
        JobApplication newJobApplication = jobApplicationMapper.fromDto(application);
        newJobApplication.setCompany(managedCompany);

        if (application.referredById() != null || (application.referredByName() != null && !application.referredByName().trim().isEmpty())) {
            if (application.referredById() != null) {
                Contact existingContact = contactService.getContactById(application.referredById());
                newJobApplication.setReferredBy(existingContact);
            } else {
                ContactResponseDto newContact = contactService.createContact(new ContactRequestDto(application.referredByName(), managedCompany.getName()));
                Contact managedContact = contactService.getContactById(newContact.id());
                newJobApplication.setReferredBy(managedContact);
            }
        } else if (application.referral()) {
            throw new RuntimeException("A referral application must have a contact");
        }

        if (application.recruiterId() != null || (application.recruiterName() != null && !application.recruiterName().trim().isEmpty())) {
            if (application.recruiterId() != null) {
                Contact existingRecruiter = contactService.getContactById(application.recruiterId());
                newJobApplication.setRecruiter(existingRecruiter);
            } else {
                ContactResponseDto newRecruiter = contactService.createContact(new ContactRequestDto(application.recruiterName(), managedCompany.getName()));
                Contact recruiter = contactService.getContactById(newRecruiter.id());
                newJobApplication.setRecruiter(recruiter);
            }
        } else {
            newJobApplication.setRecruiter(null);
        }

        if (application.status() == null) {
            newJobApplication.setStatus(ApplicationStatus.APPLIED);
        }
        return jobApplicationMapper.toDto(jobApplicationRepository.save(newJobApplication));
    }

    @Transactional
    public JobApplication updateStatus(Long id, ApplicationStatus updatedStatus) {
        JobApplication application = getApplicationById(id);
        application.setStatus(updatedStatus);
        return jobApplicationRepository.save(application);
    }

    @Transactional
    public JobApplicationResponseDto updateApplication(Long id, JobApplicationRequestDto updatedApplication) {
        JobApplication existing = getApplicationById(id);
        existing.setJobTitle(updatedApplication.jobTitle());
        existing.setStatus(updatedApplication.status());
        existing.setNotes(updatedApplication.notes());
        existing.setSource(updatedApplication.source());
        existing.setFollowUpDate(updatedApplication.followUpDate());
        existing.setExpectedSalary(updatedApplication.expectedSalary());
        existing.setOfferedSalary(updatedApplication.offeredSalary());
        existing.setCurrency(updatedApplication.currency());
        existing.setReferral(updatedApplication.referral());
        if (updatedApplication.companyId() != null || (updatedApplication.companyName() != null && !updatedApplication.companyName().trim().isEmpty())) {
            CompanyResponseDto resolvedCompany = companyService.createCompany(new CompanyRequestDto(updatedApplication.companyName().trim()));
            Company managedCompany = companyService.getCompanyById(resolvedCompany.id());
            existing.setCompany(managedCompany);
        }
        Company targetCompany = existing.getCompany();
        if (updatedApplication.referredById() != null || (updatedApplication.referredByName() != null && !updatedApplication.referredByName().trim().isEmpty())) {
            if (updatedApplication.referredById() != null) {
                Contact existingContact = contactService.getContactById(updatedApplication.referredById());
                existing.setReferredBy(existingContact);
            } else {
                ContactResponseDto referrer = contactService.createContact(new ContactRequestDto(updatedApplication.referredByName(), targetCompany.getName()));
                existing.setReferredBy(contactService.getContactById(referrer.id()));
            }
        } else {
            existing.setReferredBy(null);
        }
        if (updatedApplication.recruiterId() != null || (updatedApplication.recruiterName() != null && !updatedApplication.recruiterName().trim().isEmpty())) {
            if (updatedApplication.recruiterId() != null) {
                Contact existingRecruiter = contactService.getContactById(updatedApplication.recruiterId());
                existing.setRecruiter(existingRecruiter);
            } else {
                ContactResponseDto newRecruiter = contactService.createContact(new ContactRequestDto(updatedApplication.recruiterName(), targetCompany.getName()));
                existing.setRecruiter(contactService.getContactById(newRecruiter.id()));
            }
        } else {
            existing.setRecruiter(null);
        }
        return jobApplicationMapper.toDto(jobApplicationRepository.save(existing));
    }

    @Transactional
    public void deleteApplication(Long id) {
        if (!jobApplicationRepository.existsById(id)) {
            throw new RuntimeException("Application not found with id:" + id);
        }
        jobApplicationRepository.deleteById(id);
    }
}
