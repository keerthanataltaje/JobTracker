package com.example.JobTracker.service;

import com.example.JobTracker.domain.entity.ApplicationStatus;
import com.example.JobTracker.domain.entity.Company;
import com.example.JobTracker.domain.entity.Contact;
import com.example.JobTracker.domain.entity.JobApplication;
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

    public JobApplicationService(JobApplicationRepository jobApplicationRepository, CompanyService companyService, ContactService contactService) {
        this.jobApplicationRepository = jobApplicationRepository;
        this.companyService = companyService;
        this.contactService = contactService;
    }

    @Transactional(readOnly = true)
    public List<JobApplication> getAllApplications() {
        return jobApplicationRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
    }

    @Transactional(readOnly = true)
    public JobApplication getApplicationById(Long id) {
        return jobApplicationRepository.findById(id).orElseThrow(() -> new RuntimeException("Job Application not found with id:" + id));
    }

    @Transactional(readOnly = true)
    public List<JobApplication> getApplicationsByCompany(Long companyId){
        return jobApplicationRepository.findByCompanyId(companyId);
    }

    @Transactional(readOnly = true)
    public List<JobApplication> getApplicationsByStatus(ApplicationStatus applicationStatus) {
        return jobApplicationRepository.findByStatus(applicationStatus);
    }

    @Transactional(readOnly = true)
    public List<JobApplication> getOverdueFollowups() {
        return jobApplicationRepository.findByFollowUpDateBefore(LocalDate.now());
    }

    @Transactional
    public JobApplication createApplication(JobApplication application) {
        if (application.getCompany() == null || application.getCompany().getName() == null || application.getCompany().getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Job application must be associated with a valid company");
        }

        Company resolvedCompany = companyService.createCompany(application.getCompany());
        application.setCompany(resolvedCompany);

        if (application.getReferredBy() != null) {
            Contact referralContact = application.getReferredBy();
            if (referralContact.getId() != null) {
                Contact existingContact = contactService.getContactById(referralContact.getId());
                application.setReferredBy(existingContact);
            } else if (referralContact.getName() != null && !referralContact.getName().trim().isEmpty()) {
                referralContact.setCompany(resolvedCompany);
                Contact newContact = contactService.createContact(referralContact);
                application.setReferredBy(newContact);
            }
        } else if (application.isReferral()) {
            throw new RuntimeException("A referral application must have a contact");
        }

        if (application.getRecruiter() != null) {
            Contact recruiterDetails = application.getRecruiter();
            if (recruiterDetails.getId() != null) {
                Contact existingRecruiter = contactService.getContactById(recruiterDetails.getId());
                application.setRecruiter(existingRecruiter);
            } else if (recruiterDetails.getName() != null && !recruiterDetails.getName().trim().isEmpty()) {
                recruiterDetails.setCompany(resolvedCompany);
                Contact newRecruiter = contactService.createContact(recruiterDetails);
                application.setRecruiter(newRecruiter);
            }
        } else {
            application.setRecruiter(null);
        }

        if (application.getStatus() == null) {
            application.setStatus(ApplicationStatus.APPLIED);
        }
        return jobApplicationRepository.save(application);
    }

    @Transactional
    public JobApplication updateStatus(Long id, ApplicationStatus updatedStatus) {
        JobApplication application = getApplicationById(id);
        application.setStatus(updatedStatus);
        return jobApplicationRepository.save(application);
    }

    @Transactional
    public JobApplication updateApplication(Long id, JobApplication updatedApplication) {
        JobApplication existing = getApplicationById(id);
        existing.setJobTitle(updatedApplication.getJobTitle());
        existing.setStatus(updatedApplication.getStatus());
        existing.setNotes(updatedApplication.getNotes());
        existing.setSource(updatedApplication.getSource());
        existing.setFollowUpDate(updatedApplication.getFollowUpDate());
        existing.setExpectedSalary(updatedApplication.getExpectedSalary());
        existing.setOfferedSalary(updatedApplication.getOfferedSalary());
        existing.setCurrency(updatedApplication.getCurrency());
        existing.setReferral(updatedApplication.isReferral());
        if (updatedApplication.getCompany() != null && updatedApplication.getCompany().getName() != null && !updatedApplication.getCompany().getName().trim().isEmpty()) {
            Company resolvedCompany = companyService.createCompany(updatedApplication.getCompany());
            existing.setCompany(resolvedCompany);
        }
        Company targetCompany = existing.getCompany();
        if (updatedApplication.getReferredBy() != null) {
            Contact referrerDetails = updatedApplication.getReferredBy();
            if (referrerDetails.getId() != null) {
                Contact existingContact = contactService.getContactById(referrerDetails.getId());
                existing.setReferredBy(existingContact);
            } else if (referrerDetails.getName() != null && !referrerDetails.getName().trim().isEmpty()) {
                referrerDetails.setCompany(targetCompany);
                existing.setReferredBy(contactService.createContact(referrerDetails));
            }
        } else {
            existing.setReferredBy(null);
        }
        if (updatedApplication.getRecruiter() != null) {
            Contact recruiterDetails = updatedApplication.getRecruiter();
            if (recruiterDetails.getId() != null) {
                Contact existingRecruiter = contactService.getContactById(recruiterDetails.getId());
                existing.setRecruiter(existingRecruiter);
            } else if (recruiterDetails.getName() != null && !recruiterDetails.getName().trim().isEmpty()) {
                recruiterDetails.setCompany(targetCompany);
                Contact newRecruiter = contactService.createContact(recruiterDetails);
                existing.setRecruiter(newRecruiter);
            }
        } else {
            existing.setRecruiter(null);
        }
        return jobApplicationRepository.save(existing);
    }

    @Transactional
    public void deleteApplication(Long id) {
        if (!jobApplicationRepository.existsById(id)) {
            throw new RuntimeException("Application not found with id:" + id);
        }
        jobApplicationRepository.deleteById(id);
    }
}
