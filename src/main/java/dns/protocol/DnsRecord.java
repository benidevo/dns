package dns.protocol;

/**
 * Represents standard DNS record types as defined in RFC 1035 and subsequent
 * RFCs.
 * Each record type has a specific numeric value used in DNS protocol messages.
 */
enum DnsRecord {
    /**
     * Reserved record type.
     * Value: 0
     */
    RESERVED(0),
    /**
     * Address record that maps a hostname to an IPv4 address.
     * Value: 1
     */
    A(1),

    /**
     * Name Server record that delegates a DNS zone to an authoritative name server.
     * Value: 2
     */
    NS(2),

    /**
     * Canonical Name record that creates an alias from one domain to another.
     * Value: 5
     */
    CNAME(5),

    /**
     * Text record that holds arbitrary text data associated with a domain.
     * Value: 16
     */
    TXT(16),

    /**
     * Address record that maps a hostname to an IPv6 address.
     * Value: 28
     */
    AAAA(28),

    /**
     * Pointer record that maps an IP address to a hostname (reverse DNS lookup).
     * Value: 12
     */
    PTR(12),

    /**
     * Start of Authority record that provides information about the DNS zone,
     * including the primary name server and zone administrator email.
     * Value: 6
     */
    SOA(6);

    private final int value;

    DnsRecord(int value) {
        this.value = value;
    }

    /**
     * Retrieves the value associated with this DNS record.
     *
     * @return the integer value of the DNS record
     */
    int getValue() {
        return value;
    }

    /**
     * Converts an integer value to its corresponding {@code DnsRecord} enum
     * constant.
     *
     * @param value the integer value representing a DNS record type.
     * @return the {@code DnsRecord} enum constant corresponding to the given value.
     * @throws IllegalArgumentException if the value does not correspond to any
     *                                  known DNS record type.
     */
    static DnsRecord fromValue(int value) {
        for (DnsRecord record : values()) {
            if (record.getValue() == value) {
                return record;
            }
        }
        throw new IllegalArgumentException("Unknown DNS record type: " + value);
    }

}
