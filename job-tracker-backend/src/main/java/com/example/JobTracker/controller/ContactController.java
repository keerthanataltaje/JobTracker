package com.example.JobTracker.controller;

import com.example.JobTracker.domain.entity.Contact;
import com.example.JobTracker.dto.ContactRequestDto;
import com.example.JobTracker.dto.ContactResponseDto;
import com.example.JobTracker.service.ContactService;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/contacts")
@CrossOrigin(origins = "http://localhost:4200")
public class ContactController {
    private final ContactService contactService;

    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    @GetMapping
    public ResponseEntity<List<ContactResponseDto>> getContacts(@RequestParam(required = false) Long companyId) {
        if (companyId != null) {
            return ResponseEntity.ok(contactService.getContactsByCompany(companyId));
        }
        return ResponseEntity.ok(contactService.getAllContacts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContactResponseDto> getContactById(@PathVariable Long id) {
        return ResponseEntity.ok(contactService.getContactByIdDto(id));
    }

    @PostMapping
    public ResponseEntity<ContactResponseDto> createContact(@RequestBody ContactRequestDto contact) {
        ContactResponseDto createdContact = contactService.createContact(contact);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdContact);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ContactResponseDto> updateContact(@PathVariable Long id, @RequestBody ContactRequestDto contact) {
        ContactResponseDto updatedContact = contactService.updateContact(id, contact);
        return ResponseEntity.ok(updatedContact);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContact(@PathVariable Long id){
        contactService.deleteContact(id);
        return ResponseEntity.noContent().build();
    }
}



