package com.example.JobTracker.service;

import com.example.JobTracker.domain.entity.Company;
import com.example.JobTracker.domain.entity.Contact;
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

    public ContactService(ContactRepository contactRepository, CompanyService companyService, JobApplicationRepository jobApplicationRepository) {
        this.contactRepository = contactRepository;
        this.companyService = companyService;
        this.jobApplicationRepository = jobApplicationRepository;
    }

    @Transactional(readOnly = true)
    public List<Contact> getAllContacts() {
        return contactRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Contact> getContactsByCompany(Long companyId) {
        companyService.getCompanyById(companyId);
        return contactRepository.findByCompanyId(companyId);
    }

    @Transactional(readOnly = true)
    public Contact getContactById(Long id) {
        return contactRepository.findById(id).orElseThrow(() -> new RuntimeException("Contact not found with this id: " + id));
    }

    @Transactional
    public Contact createContact(Contact contact) {
        if (contact.getCompany() == null) {
            throw new IllegalArgumentException("A contact must be assigned to a company");
        }
        Company resolvedCompany = companyService.createCompany(contact.getCompany());
        contact.setCompany(resolvedCompany);
        return contactRepository.save(contact);
    }

    @Transactional
    public Contact updateContact(Long id, Contact updatedContact) {
        Contact existing = getContactById(id);
        existing.setName(updatedContact.getName());
        existing.setRole(updatedContact.getRole());
        existing.setEmail(updatedContact.getEmail());
        existing.setLinkedin(updatedContact.getLinkedin());
        existing.setNotes(updatedContact.getNotes());
        existing.setPhoneNumber(updatedContact.getPhoneNumber());
        if (updatedContact.getCompany() != null && updatedContact.getCompany().getName() != null && !updatedContact.getCompany().getName().trim().isEmpty()) {
            Company resolvedCompany = companyService.createCompany(updatedContact.getCompany());
            existing.setCompany(resolvedCompany);
        }
        return contactRepository.save(existing);
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
