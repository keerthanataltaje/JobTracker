package com.example.JobTracker.domain.entity;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name="contact")
public class Contact {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String role;

    private String email;

    private String phoneNumber;

    private String linkedin;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @ManyToOne
    @JoinColumn(name="company_id", nullable=false)
    private Company company;

    public Contact(){}

    public Contact(Long id, String name, String role, String email, String linkedin, String notes, Company company, String phoneNumber) {
        this.id = id;
        this.name = name;
        this.role = role;
        this.email = email;
        this.linkedin = linkedin;
        this.notes = notes;
        this.company = company;
        this.phoneNumber = phoneNumber;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLinkedin() {
        return linkedin;
    }

    public void setLinkedin(String linkedin) {
        this.linkedin = linkedin;
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String toString() {
        return "Contact{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", role='" + role + '\'' +
                ", email='" + email + '\'' +
                ", linkedin='" + linkedin + '\'' +
                ", notes='" + notes + '\'' +
                ", company=" + (company != null ? company.getName() : null) +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Contact)) return false;
        Contact contact = (Contact) o;
        return id != null && id.equals(contact.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}

/*
* GET /contacts /contacts/{id}, POST /contacts /contacts/company, PUT /contacts/{id} /contacts/{id}/company, DELETE /contact/{id}
* */
