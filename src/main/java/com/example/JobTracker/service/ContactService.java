package com.example.JobTracker.service;

import com.example.JobTracker.domain.entity.Company;
import com.example.JobTracker.domain.entity.Contact;
import com.example.JobTracker.dto.CompanyRequestDto;
import com.example.JobTracker.dto.CompanyResponseDto;
import com.example.JobTracker.dto.ContactRequestDto;
import com.example.JobTracker.dto.ContactResponseDto;
import com.example.JobTracker.mapper.ContactMapper;
import com.example.JobTracker.repository.ContactRepository;
import com.example.JobTracker.repository.JobApplicationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ContactService {
    private final ContactRepository contactRepository;
    private final CompanyService companyService;
    private final JobApplicationRepository jobApplicationRepository;
    private final ContactMapper contactMapper;

    public ContactService(ContactRepository contactRepository, CompanyService companyService, JobApplicationRepository jobApplicationRepository, ContactMapper contactMapper) {
        this.contactRepository = contactRepository;
        this.companyService = companyService;
        this.jobApplicationRepository = jobApplicationRepository;
        this.contactMapper = contactMapper;
    }

    @Transactional(readOnly = true)
    public List<ContactResponseDto> getAllContacts() {
        return contactRepository.findAll().stream().map(contactMapper::toDto).toList();
    }

    @Transactional(readOnly = true)
    public List<ContactResponseDto> getContactsByCompany(Long companyId) {
        companyService.getCompanyByIdDto(companyId);
        return contactRepository.findByCompanyId(companyId).stream().map(contactMapper::toDto).toList();
    }

    @Transactional(readOnly = true)
    public ContactResponseDto getContactByIdDto(Long id) {
        return contactMapper.toDto(getContactById(id));
    }

    @Transactional
    public Contact getContactById(Long id) {
        return contactRepository.findById(id).orElseThrow(() -> new RuntimeException("Contact not found with this id: " + id));
    }

    @Transactional
    public ContactResponseDto createContact(ContactRequestDto contact) {
        if (contact.companyName() == null) {
            throw new IllegalArgumentException("A contact must be assigned to a company");
        }
        CompanyResponseDto resolvedCompanyDto = companyService.createCompany(new CompanyRequestDto(contact.companyName()));
        Company managedCompany = companyService.getCompanyById(resolvedCompanyDto.id());
        Contact newContact = contactMapper.fromDto(contact);
        newContact.setCompany(managedCompany);
        return contactMapper.toDto(contactRepository.save(newContact));
    }

    @Transactional
    public ContactResponseDto updateContact(Long id, ContactRequestDto updatedContact) {
        Contact existing = getContactById(id);
        existing.setName(updatedContact.name());
        existing.setRole(updatedContact.role());
        existing.setEmail(updatedContact.email());
        existing.setLinkedin(updatedContact.linkedin());
        existing.setNotes(updatedContact.notes());
        existing.setPhoneNumber(updatedContact.phoneNumber());
        if (updatedContact.companyName() != null && !updatedContact.companyName().trim().isEmpty()) {
            CompanyResponseDto resolvedCompanyDto = companyService.createCompany(new CompanyRequestDto(updatedContact.companyName()));
            Company managedCompany = companyService.getCompanyById(resolvedCompanyDto.id());
            existing.setCompany(managedCompany);
        }
        return contactMapper.toDto(contactRepository.save(existing));
    }

    @Transactional
    public void deleteContact(Long id) {
        if (!contactRepository.existsById(id)) {
            throw new RuntimeException("Contact not found with id: " + id);
        }
        if (jobApplicationRepository.existsByRecruiterId(id)) {
            throw new RuntimeException("Cannot delete contact. This person is a recruiter for an active job application");
        }
        if (jobApplicationRepository.existsByReferredById(id)) {
            throw new RuntimeException("Cannot delete contact. This person is a referral source for an active job application");
        }
        contactRepository.deleteById(id);
    }
}
