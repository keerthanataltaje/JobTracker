package com.example.JobTracker.domain.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "job_application")
public class JobApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String jobTitle;

    @Column(columnDefinition = "TEXT")
    private String jobDescription;

    @Column(nullable = false)
    private LocalDate appliedDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ApplicationStatus status;

    private String source;

    private LocalDate followUpDate;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(precision=15,scale=2)
    private BigDecimal expectedSalary;

    @Column(precision = 15, scale=2)
    private BigDecimal offeredSalary;

    @Column(length=3)
    private String currency;

    @Column(nullable=false)
    private boolean referral = false;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="referred_by_id")
    private Contact referredBy;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="recruiter_id")
    private Contact recruiter;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="company_id",nullable = false)
    private Company company;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    public JobApplication(){

    }

    public JobApplication(Long id, String jobTitle, String jobDescription, LocalDate appliedDate, ApplicationStatus status, String source, LocalDate followUpDate, String notes, BigDecimal expectedSalary, BigDecimal offeredSalary, String currency, boolean referral, Contact referredBy, Contact recruiter, Company company, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.jobTitle = jobTitle;
        this.jobDescription = jobDescription;
        this.appliedDate = appliedDate;
        this.status = status;
        this.source = source;
        this.followUpDate = followUpDate;
        this.notes = notes;
        this.expectedSalary = expectedSalary;
        this.offeredSalary = offeredSalary;
        this.currency = currency;
        this.referral = referral;
        this.referredBy = referredBy;
        this.recruiter = recruiter;
        this.company = company;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public LocalDate getAppliedDate() {
        return appliedDate;
    }

    public void setAppliedDate(LocalDate appliedDate) {
        this.appliedDate = appliedDate;
    }

    public ApplicationStatus getStatus() {
        return status;
    }

    public void setStatus(ApplicationStatus status) {
        this.status = status;
    }

    public String getSource() {
        return source;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }

    public boolean isReferral() {
        return referral;
    }

    public void setReferral(boolean referral) {
        this.referral = referral;
    }

    public Contact getReferredBy() {
        return referredBy;
    }

    public void setReferredBy(Contact referredBy) {
        this.referredBy = referredBy;
    }

    public Contact getRecruiter() {
        return recruiter;
    }

    public void setRecruiter(Contact recruiter) {
        this.recruiter = recruiter;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public LocalDate getFollowUpDate() {
        return followUpDate;
    }

    public void setFollowUpDate(LocalDate followUpDate) {
        this.followUpDate = followUpDate;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    public BigDecimal getOfferedSalary() {
        return offeredSalary;
    }

    public void setOfferedSalary(BigDecimal offeredSalary) {
        this.offeredSalary = offeredSalary;
    }

    public BigDecimal getExpectedSalary() {
        return expectedSalary;
    }

    public void setExpectedSalary(BigDecimal expectedSalary) {
        this.expectedSalary = expectedSalary;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    @Override
    public String toString() {
        return "JobApplication{" +
                "id=" + id +
                ", jobTitle='" + jobTitle + '\'' +
                ", status=" + status +
                ", companyName=" + (company != null ? company.getName() : null) +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JobApplication that = (JobApplication) o;
        return id!=null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}

/*GET /apps, /apps/{id}
* POST /apps -> POST ->/apps/company /apps/contact
* PATCH -> /apps/{id}/company
* DELETE ->apps/{id}
* */

