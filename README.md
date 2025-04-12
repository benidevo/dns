# DNS Server

A DNS server implementation written in Java.

## Overview

This project implements a Domain Name System (DNS) server that handles DNS queries according to [RFC 1035 specifications](https://tools.ietf.org/html/rfc1035). Built with Java 23 and managed with Maven, the implementation follows a multi-layered architecture.

## Architecture

```
┌───────────────────┐
│    DNS Client     │
└─────────┬─────────┘
          │ UDP
          ▼
┌───────────────────┐      ┌───────────────────┐
│  Server Layer     │      │   Protocol Layer  │
│  (UDP handling)   │◄────►│   (DNS Messages)  │
└─────────┬─────────┘      └─────────┬─────────┘
          │                          │
          ▼                          ▼
┌───────────────────┐      ┌───────────────────┐
│  Service Layer    │      │  Domain Objects   │
│ (DNS Resolution)  │◄────►│ (Records, Queries)│
└─────────┬─────────┘      └───────────────────┘
          │
          ▼
┌───────────────────┐
│ Repository Layer  │
│ (Domain Storage)  │
└─────────┬─────────┘
          │
          ▼
┌───────────────────┐
│   Store Layer     │
│ (Implementation)  │
└───────────────────┘
```

### Key Components

1. **Server Layer**
   - Handles UDP socket communication
   - Receives binary DNS query packets
   - Sends binary DNS response packets
   - Delegates processing to the Service layer

2. **Protocol Layer**
   - Defines DNS message structure (headers, queries, records)
   - Handles serialization/deserialization between binary and domain objects
   - Maintains protocol compliance

3. **Service Layer**
   - Contains the DNS resolution logic
   - Processes queries against the repository
   - Builds appropriate responses

4. **Repository Layer**
   - Abstracts the storage mechanism
   - Provides interface for domain name resolution

5. **Store Layer**
   - Implements the repository interface
   - Stores mappings between domain names and IP addresses

## Current Implementation Status

The server is currently in development with the following components implemented:

- [x] Basic UDP socket server listening on port 2053
- [x] DNS message and header structures with serialization
- [x] Simple response generation infrastructure
- [ ] Full DNS request parsing
- [ ] Domain name storage
- [ ] Multiple record type support (A, AAAA, MX, etc.)
- [ ] Caching mechanism

## Getting Started

### Prerequisites

- Java 23 or higher
- Maven 3.6 or higher

### Building the Project

```bash
# Clone the repository
git clone https://github.com/benidevo/dns
cd dns

# Build the project
mvn package
```

### Running the Server

```bash
# Using the provided run script
./run.sh

# Or manually
java -jar target/codecrafters-dns-server.jar
```

## References

- [RFC 1035 - Domain Names - Implementation and Specification](https://tools.ietf.org/html/rfc1035)
- [RFC 3596 - DNS Extensions to Support IP Version 6](https://tools.ietf.org/html/rfc3596)
- [Java DatagramSocket API](https://docs.oracle.com/en/java/javase/23/docs/api/java.base/java/net/DatagramSocket.html)
