package com.shophub.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column
    private String phone;

    @Column(nullable = false)
    private String password;   // BCrypt hashed — never stored in plain text

    @Column
    private boolean newsletter = false;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    // ── Getters & Setters ─────────────────────────────────────

    public Long getId()                  { return id; }

    public String getFirstName()         { return firstName; }
    public void setFirstName(String v)   { this.firstName = v; }

    public String getLastName()          { return lastName; }
    public void setLastName(String v)    { this.lastName = v; }

    public String getEmail()             { return email; }
    public void setEmail(String v)       { this.email = v; }

    public String getPhone()             { return phone; }
    public void setPhone(String v)       { this.phone = v; }

    public String getPassword()          { return password; }
    public void setPassword(String v)    { this.password = v; }

    public boolean isNewsletter()        { return newsletter; }
    public void setNewsletter(boolean v) { this.newsletter = v; }

    public LocalDateTime getCreatedAt()  { return createdAt; }
    public void setCreatedAt(LocalDateTime v) { this.createdAt = v; }
}
