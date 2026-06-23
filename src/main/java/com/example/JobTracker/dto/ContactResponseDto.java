package com.example.JobTracker.dto;

public record ContactResponseDto(Long id, String name, String role, String email, String phoneNumber, String linkedin,
                                 String notes, Long companyId, String companyName) {
}
