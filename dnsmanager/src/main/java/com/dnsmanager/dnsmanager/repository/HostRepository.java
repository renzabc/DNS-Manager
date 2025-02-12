package com.dnsmanager.dnsmanager.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.dnsmanager.dnsmanager.model.Host;


@Repository
public interface HostRepository extends JpaRepository<Host, Long> {
    @Query("SELECT b.domain FROM Host b")
    List<String> findAllDomains();
}
