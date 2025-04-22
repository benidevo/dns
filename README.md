# DNS Server

A lightweight Java implementation of a DNS server for the [CodeCrafters DNS Server Challenge](https://codecrafters.io/challenges/dns-server).

## Overview

This DNS server processes and responds to domain name lookup requests according to RFC 1035 specifications. The server listens for UDP packets on port 2053, parses DNS query messages, and generates appropriate responses with IP address information.

## Features

✅ UDP socket communication on port 2053
✅ Binary DNS protocol message parsing
✅ Query extraction and validation
✅ Response message generation
✅ IPv4 (A record) support

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

- Java 23
- Maven 3.6+

### Running

```bash
# Build and run
make run
```

## References

- [RFC 1035: Domain Names - Implementation and Specification](https://tools.ietf.org/html/rfc1035)
- [CodeCrafters DNS Challenge](https://codecrafters.io/challenges/dns-server)
