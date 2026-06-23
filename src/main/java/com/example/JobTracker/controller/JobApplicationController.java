package com.example.JobTracker.controller;

import com.example.JobTracker.domain.entity.ApplicationStatus;
import com.example.JobTracker.domain.entity.JobApplication;
import com.example.JobTracker.dto.JobApplicationRequestDto;
import com.example.JobTracker.dto.JobApplicationResponseDto;
import com.example.JobTracker.service.JobApplicationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/jobapplications")
@CrossOrigin("http://localhost:4200")
public class JobApplicationController {
    private final JobApplicationService jobApplicationService;
    public JobApplicationController(JobApplicationService jobApplicationService){
        this.jobApplicationService = jobApplicationService;
    }

    @GetMapping
    public ResponseEntity<List<JobApplicationResponseDto>> getApplications(@RequestParam(name="status",required = false)ApplicationStatus applicationStatus, @RequestParam(required = false) Long companyId){
        if(applicationStatus!=null){
            return ResponseEntity.ok(jobApplicationService.getApplicationsByStatus(applicationStatus));
        }
        if(companyId!=null){
            return ResponseEntity.ok(jobApplicationService.getApplicationsByCompany(companyId));
        }
        return ResponseEntity.ok(jobApplicationService.getAllApplications());
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobApplicationResponseDto> getApplicationsById(@PathVariable Long id){
        return ResponseEntity.ok(jobApplicationService.getApplicationByIdDto(id));
    }

    @PostMapping
    public ResponseEntity<JobApplicationResponseDto> createApplication(@RequestBody JobApplicationRequestDto application){
        JobApplicationResponseDto createdJobApplication = jobApplicationService.createApplication(application);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdJobApplication);
    }

    @PutMapping("/{id}")
    public ResponseEntity<JobApplicationResponseDto> updateApplication(@PathVariable Long id, @RequestBody JobApplicationRequestDto updatedJobApplication){
        return ResponseEntity.ok(jobApplicationService.updateApplication(id,updatedJobApplication));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteApplication(@PathVariable Long id){
        jobApplicationService.deleteApplication(id);
        return ResponseEntity.noContent().build();
    }
}
