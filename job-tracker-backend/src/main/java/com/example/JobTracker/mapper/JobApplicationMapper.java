package com.example.JobTracker.mapper;

import com.example.JobTracker.domain.entity.Company;
import com.example.JobTracker.domain.entity.Contact;
import com.example.JobTracker.domain.entity.JobApplication;
import com.example.JobTracker.dto.JobApplicationRequestDto;
import com.example.JobTracker.dto.JobApplicationResponseDto;
import org.springframework.stereotype.Component;

@Component
public class JobApplicationMapper {
    public JobApplicationResponseDto toDto(JobApplication jobApplication) {
        if (jobApplication == null) {
            return null;
        }
        return new JobApplicationResponseDto(jobApplication.getId(), jobApplication.getJobTitle(), jobApplication.getJobDescription(), jobApplication.getAppliedDate(), jobApplication.getStatus(), jobApplication.getSource(), jobApplication.getFollowUpDate(), jobApplication.getNotes(), jobApplication.getExpectedSalary(), jobApplication.getOfferedSalary(), jobApplication.getCurrency(), jobApplication.isReferral(), jobApplication.getCreatedAt(), jobApplication.getCompany() != null ? jobApplication.getCompany().getId() : null, jobApplication.getCompany() != null ? jobApplication.getCompany().getName() : null, jobApplication.getRecruiter() != null ? jobApplication.getRecruiter().getId() : null, jobApplication.getRecruiter() != null ? jobApplication.getRecruiter().getName() : null, jobApplication.getReferredBy() != null ? jobApplication.getReferredBy().getId() : null, jobApplication.getReferredBy() != null ? jobApplication.getReferredBy().getName() : null);
    }

    public JobApplication fromDto(JobApplicationRequestDto jobApplicationRequestDto) {
        JobApplication jobApplication = new JobApplication();
        jobApplication.setJobTitle(jobApplicationRequestDto.jobTitle());
        jobApplication.setJobDescription(jobApplicationRequestDto.jobDescription());
        jobApplication.setAppliedDate(jobApplicationRequestDto.appliedDate());
        jobApplication.setStatus(jobApplicationRequestDto.status());
        jobApplication.setSource(jobApplicationRequestDto.source());
        jobApplication.setFollowUpDate(jobApplicationRequestDto.followUpDate());
        jobApplication.setNotes(jobApplicationRequestDto.notes());
        jobApplication.setExpectedSalary(jobApplicationRequestDto.expectedSalary());
        jobApplication.setOfferedSalary(jobApplicationRequestDto.offeredSalary());
        jobApplication.setCurrency(jobApplicationRequestDto.currency());
        jobApplication.setReferral(jobApplicationRequestDto.referral());
        if (jobApplicationRequestDto.companyId() != null || (jobApplicationRequestDto.companyName() != null && !jobApplicationRequestDto.companyName().trim().isEmpty())) {
            Company company = new Company(jobApplicationRequestDto.companyId(), jobApplicationRequestDto.companyName());
            jobApplication.setCompany(company);

        }

        if (jobApplicationRequestDto.recruiterId() != null || (jobApplicationRequestDto.recruiterName() != null && !jobApplicationRequestDto.recruiterName().trim().isEmpty())) {
            Contact recruiter = new Contact();
            recruiter.setId(jobApplicationRequestDto.recruiterId());
            recruiter.setName(jobApplicationRequestDto.recruiterName());
            jobApplication.setRecruiter(recruiter);
        }

        if (jobApplicationRequestDto.referredById() != null || (jobApplicationRequestDto.referredByName() != null && !jobApplicationRequestDto.referredByName().trim().isEmpty())) {
            Contact referrer = new Contact();
            referrer.setId(jobApplicationRequestDto.referredById());
            referrer.setName(jobApplicationRequestDto.referredByName());
            jobApplication.setReferredBy(referrer);
        }
        return jobApplication;
    }
}
