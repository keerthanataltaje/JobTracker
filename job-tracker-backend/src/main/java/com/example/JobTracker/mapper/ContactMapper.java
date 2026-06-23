package com.example.JobTracker.mapper;

import com.example.JobTracker.domain.entity.Company;
import com.example.JobTracker.domain.entity.Contact;
import com.example.JobTracker.dto.ContactRequestDto;
import com.example.JobTracker.dto.ContactResponseDto;
import org.springframework.stereotype.Component;

@Component
public class ContactMapper {
    public ContactResponseDto toDto(Contact contact) {
        if (contact == null) {
            return null;
        }
        return new ContactResponseDto(contact.getId(), contact.getName(), contact.getRole(), contact.getEmail(), contact.getPhoneNumber(), contact.getLinkedin(), contact.getNotes(), contact.getCompany() != null ? contact.getCompany().getId() : null, contact.getCompany() != null ? contact.getCompany().getName() : null);
    }

    public Contact fromDto(ContactRequestDto contactRequestDto){
        Contact newContact = new Contact();
        newContact.setName(contactRequestDto.name());
        newContact.setRole(contactRequestDto.role());
        newContact.setPhoneNumber(contactRequestDto.phoneNumber());
        newContact.setLinkedin(contactRequestDto.linkedin());
        newContact.setNotes(contactRequestDto.notes());
        newContact.setEmail(contactRequestDto.email());
        if(contactRequestDto.companyId()!=null || (contactRequestDto.companyName()!=null && !contactRequestDto.companyName().trim().isEmpty())){
            Company company = new Company(contactRequestDto.companyId(), contactRequestDto.companyName());
            newContact.setCompany(company);
        }
        return newContact;
    }
}
