package com.example.JobTracker.repository;

import com.example.JobTracker.domain.entity.ApplicationStatus;
import com.example.JobTracker.domain.entity.JobApplication;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {

    List<JobApplication> findByStatus(ApplicationStatus status);
    List<JobApplication> findByCompanyId(Long companyId);
    List<JobApplication> findByReferralTrue();
    List<JobApplication> findAll(Sort sort);
    List<JobApplication> findByFollowUpDateBefore(LocalDate date);
    boolean existsByCompanyId(Long companyId);
    boolean existsByRecruiterId(Long contactId);
    boolean existsByReferredById(Long contactId);
}
