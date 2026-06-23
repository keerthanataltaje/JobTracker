package com.example.JobTracker.dto;

import com.example.JobTracker.domain.entity.ApplicationStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record JobApplicationResponseDto(Long id, String jobTitle, String jobDescription, LocalDate appliedDate,
                                        ApplicationStatus status, String source, LocalDate followUpDate, String notes,
                                        BigDecimal expectedSalary, BigDecimal offeredSalary, String currency,
                                        Boolean referral, LocalDateTime createdAt, Long companyId, String companyName,
                                        Long recruiterId, String recruiterName, Long referredById,
                                        String referredByName) {
}
