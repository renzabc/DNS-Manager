package com.dnsmanager.dnsmanager.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dnsmanager.dnsmanager.model.Host;
import com.dnsmanager.dnsmanager.repository.HostRepository;

@RestController
@RequestMapping("/api/blocklist")
public class HostController {

    private final HostRepository repository;

    public HostController(HostRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Host> getAllBlockedDomains() {
        return repository.findAll();
    }

    @PostMapping
    public Host addDomain(@RequestBody Host domain) {
        return repository.save(domain);
    }

    @DeleteMapping("/{id}")
    public void removeDomain(@PathVariable Long id) {
        repository.deleteById(id);
    }
}
