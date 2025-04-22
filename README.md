# DNS Server

A lightweight Java implementation of a DNS server for the [CodeCrafters DNS Server Challenge](https://codecrafters.io/challenges/dns-server).

## Overview

This DNS server processes and responds to domain name lookup requests according to [RFC 1035](https://tools.ietf.org/html/rfc1035) specifications. The server listens for UDP packets on port 2053, parses DNS query messages, and generates appropriate responses with IP address information.

## Features

- ✅ UDP socket communication on port 2053
- ✅ Binary DNS protocol message parsing
- ✅ Query extraction and validation
- ✅ Response message generation
- ✅ IPv4 (A record) support

## Technical Approach

The implementation uses Java 23's features including records for immutable protocol structures and ByteBuffer for efficient binary manipulation. The DNS message format is carefully implemented according to RFC specifications, with proper header flag handling and domain name encoding/decoding.

## Limitations

This is an educational implementation with intentional limitations:

- Returns a fixed IP address (8.8.8.8) regardless of the domain queried
- Single-threaded request processing model
- No recursive resolution or forwarding capabilities
- No caching mechanism implemented
- Support limited to A records

## Quick Start

### Prerequisites

- Docker and Docker Compose
- OR Java 23 and Maven 3.6+

### Running with Docker

The easiest way to run the DNS server is using Docker Compose:

```bash
# Build and start the server
make run

# Stop the server
make stop
```

## Testing the DNS Server

You can test the DNS server using standard DNS tools like `nslookup`. The server listens on UDP port 2053 and currently responds with `8.8.8.8` for all A record queries.

### Using nslookup

On Linux/Mac:

```bash
nslookup -port=2053 example.com localhost
```

On Windows:

```bash
nslookup -port=2053 example.com 127.0.0.1
```

Expected output:

```
Server:    localhost
Address:   127.0.0.1#2053

Name:      example.com
Address:   8.8.8.8
```
