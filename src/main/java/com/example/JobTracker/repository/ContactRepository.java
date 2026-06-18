package com.example.JobTracker.repository;

import com.example.JobTracker.domain.entity.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContactRepository extends JpaRepository <Contact, Long> {

    List<Contact> findByCompanyId(Long companyId);
    List<Contact> findByNameContainingIgnoreCase(String name);
    boolean existsByCompanyId(Long companyId);
}
