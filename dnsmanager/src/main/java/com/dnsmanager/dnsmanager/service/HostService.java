package com.dnsmanager.dnsmanager.service;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.List;

import org.springframework.stereotype.Service;

import com.dnsmanager.dnsmanager.repository.HostRepository;

@Service
public class HostService {

    private final HostRepository hostRepository;

    public HostService(HostRepository hostRepository) {
        this.hostRepository = hostRepository;
    }

    public void startDnsSinkhole() {
        try (DatagramSocket socket = new DatagramSocket(53)) {
            System.out.println("🚀 DNS Sinkhole is running on port 53...");

            byte[] buffer = new byte[512];
            while (true) {
                DatagramPacket request = new DatagramPacket(buffer, buffer.length);
                socket.receive(request);

                String domain = extractDomainFromRequest(request.getData());
                System.out.println("🔍 DNS Query for: " + domain);

                if (isBlocked(domain)) {
                    System.out.println("❌ BLOCKED: " + domain);
                    sendResponse(socket, request, "127.0.0.1"); // Redirect blocked sites
                } else {
                    forwardToRealDns(socket, request);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isBlocked(String domain) {
        List<String> blockedDomains = hostRepository.findAllDomains();
        return blockedDomains.contains(domain);
    }

    private String extractDomainFromRequest(byte[] requestData) {
        int offset = 12; // Skip the DNS header (12 bytes)
        StringBuilder domain = new StringBuilder();
        
        while (requestData[offset] != 0) {
            int length = requestData[offset];
            domain.append(new String(requestData, offset + 1, length)).append(".");
            offset += length + 1;
        }
        return domain.substring(0, domain.length() - 1); // Remove trailing dot
    }
    

    private void sendResponse(DatagramSocket socket, DatagramPacket request, String ip) {
        try {
            byte[] response = createDnsResponse(request.getData(), ip);
            DatagramPacket reply = new DatagramPacket(response, response.length,
                    request.getAddress(), request.getPort());
            socket.send(reply);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void forwardToRealDns(DatagramSocket socket, DatagramPacket request) {
        String[] dnsServers = {
            "1.1.1.1", // Cloudflare
            "1.0.0.1", // Cloudflare Secondary
            "8.8.8.8", // Google
            "8.0.8.0", // Google Secondary
            "208.67.222.222", // OpenDNS Home
            "208.67.220.220", // OpenDNS Home Secondary
            "94.140.14.14", // AdGuard DNS
            "94.140.15.15", // AdGuard DNS Secondary
            "76.76.19.19", // Alternate DNS
            "76.223.122.150", // Alternate DNS Secondary
        };

        for (String dns : dnsServers) {
            try {
                InetAddress dnsServer = InetAddress.getByName(dns);
                DatagramSocket forwardSocket = new DatagramSocket();
                DatagramPacket forwardPacket = new DatagramPacket(request.getData(), request.getLength(),
                        dnsServer, 53);
                forwardSocket.send(forwardPacket);

                byte[] buffer = new byte[512];
                DatagramPacket responsePacket = new DatagramPacket(buffer, buffer.length);
                forwardSocket.receive(responsePacket);

                DatagramPacket reply = new DatagramPacket(responsePacket.getData(), responsePacket.getLength(),
                        request.getAddress(), request.getPort());
                socket.send(reply);
                forwardSocket.close();
                return;  // Exit after first successful response
            } catch (Exception e) {
                System.out.println("❌ DNS " + dns + " failed, trying next...");
            }
        }

        System.out.println("❌ All DNS servers failed! Cannot resolve query.");
    }

    private byte[] createDnsResponse(byte[] requestData, String ip) {
        byte[] response = new byte[512];

        // Copy the request header to the response
        System.arraycopy(requestData, 0, response, 0, 12);

        // Set the response flags to indicate success
        response[2] = (byte) 0x81; // QR (Response), AA (Authoritative), NOERROR
        response[3] = (byte) 0x80;

        // Set the number of answers to 1 (for the IP response)
        response[6] = 0x00;
        response[7] = 0x01;

        // Copy the original domain query
        System.arraycopy(requestData, 12, response, 12, requestData.length - 12);

        // Set the answer section (to resolve the domain to the IP)
        int ipStart = requestData.length + 1;
        response[ipStart] = 0x00; // Name (pointer to query)
        response[ipStart + 1] = 0x00; // Type (A)
        response[ipStart + 2] = 0x01; // Type (A)
        response[ipStart + 3] = 0x00; // Class (IN)
        response[ipStart + 4] = 0x00;
        response[ipStart + 5] = 0x00;
        response[ipStart + 6] = 0x00;
        response[ipStart + 7] = 0x04; // TTL

        // Set the IP address (e.g., 127.0.0.1 for blocked domains)
        String[] ipParts = ip.split("\\.");
        response[ipStart + 8] = (byte) Integer.parseInt(ipParts[0]);
        response[ipStart + 9] = (byte) Integer.parseInt(ipParts[1]);
        response[ipStart + 10] = (byte) Integer.parseInt(ipParts[2]);
        response[ipStart + 11] = (byte) Integer.parseInt(ipParts[3]);

        return response;
    }

}
