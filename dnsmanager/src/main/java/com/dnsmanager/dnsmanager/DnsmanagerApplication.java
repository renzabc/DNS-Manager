package com.dnsmanager.dnsmanager;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

import com.dnsmanager.dnsmanager.service.HostService;

@SpringBootApplication
@ComponentScan(basePackages = {"com.dnsmanager.dnsmanager.configuration", "com.dnsmanager.dnsmanager.controller", "com.dnsmanager.dnsmanager.model", "com.dnsmanager.dnsmanager.service", "com.dnsmanager.dnsmanager.repository"})
@EntityScan(basePackages = {"com.dnsmanager.dnsmanager.model"})
public class DnsmanagerApplication implements CommandLineRunner{

	private final HostService hostService;

    public DnsmanagerApplication(HostService hostService){
        this.hostService = hostService;
    }

	public static void main(String[] args) {
		SpringApplication.run(DnsmanagerApplication.class, args);
	}

	@Override
    public void run(String... args) {
        System.out.println("starting DNS Manager");
        hostService.startDnsSinkhole(); // Start DNS server on app startup
    }

}
