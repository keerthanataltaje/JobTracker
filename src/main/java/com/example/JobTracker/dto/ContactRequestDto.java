package com.example.JobTracker.dto;

public record ContactRequestDto(String name, String role, String email, String phoneNumber, String linkedin,
                                String notes, Long companyId, String companyName) {

    public ContactRequestDto(String name, String companyName) {
        this(name, null, null, null, null, null, null, companyName);
    }
}
