package com.dnsmanager.dnsmanager.model;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "blocked_domains")
public class Host {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String domain;

    public Host() {}

    public Host(String domain) {
        this.domain = domain;
    }

    public Long getId() { return id; }
    public String getDomain() { return domain; }
    public void setDomain(String domain) { this.domain = domain; }
}
