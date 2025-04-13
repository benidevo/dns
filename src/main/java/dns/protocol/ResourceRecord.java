package dns.protocol;

import java.nio.ByteBuffer;

/**
 * Represents a DNS Resource Record, which is a fundamental data structure in
 * the DNS protocol.
 * This record contains information about a specific domain name, including its
 * type, class, TTL,
 * length, and associated data.
 *
 * @param domainName  The domain name associated with this resource record.
 * @param recordType  The type of DNS record (e.g., A, AAAA, CNAME, etc.).
 * @param recordClass The class of the DNS record (e.g., IN for Internet).
 * @param ttl         The time-to-live (TTL) value, indicating how long the
 *                    record is valid.
 * @param length      The length of the data field in bytes.
 * @param data        The data associated with the record (e.g., an IP address
 *                    for an A record).
 */
record ResourceRecord(String domainName, DnsRecord recordType, int recordClass, int ttl, int length, String data) {
    static ResourceRecord valueOf() {
        return new ResourceRecord("codecrafters.io", DnsRecord.A, 1, 60, 4, "8.8.8.8");
    }

    /**
     * Calculates the total size of the resource record in bytes.
     *
     * The size is computed as follows:
     * - For each label in the domain name, 1 byte is added for the label length
     * and the length of the label in bytes is added.
     * - 4 bytes are added for the record type and record class (2 bytes each).
     * - 12 bytes are added for the TTL (4 bytes), data length (4 bytes), and data
     * (4 bytes).
     *
     * @return the total size of the resource record in bytes.
     */
    int size() {
        int size = 0;

        String[] labels = domainName.split("\\.");
        for (String label : labels) {
            size += 1;
            size += label.getBytes().length;
        }

        size += 4; // 2 bytes for record Type and 2 bytes for recordClass
        size += 12; // 4 bytes for ttl, 4 bytes for length and 4 bytes for data

        return size;
    }

    /**
     * Serializes the resource record into the provided {@link ByteBuffer}.
     *
     * <p>
     * For an {@code A} record type, the data is expected to be an IPv4 address
     * in dot-decimal notation (e.g., "192.168.1.1"), which is converted into 4
     * bytes.
     * For other record types, the data is written as raw bytes.
     *
     * @param buffer the {@link ByteBuffer} into which the resource record will be
     *               serialized
     */
    void serializeInto(ByteBuffer buffer) {
        String[] labels = domainName.split("\\.");
        for (String label : labels) {
            buffer.put((byte) label.getBytes().length);
            buffer.put(label.getBytes());
        }
        buffer.put((byte) 0);

        buffer.putShort((short) recordType.getValue());
        buffer.putShort((short) recordClass);
        buffer.putInt(ttl);
        buffer.putShort((short) length);

        if (recordType == DnsRecord.A) {
            String[] octets = data.split("\\.");
            for (String octet : octets) {
                buffer.put((byte) Integer.parseInt(octet));
            }
        } else {
            buffer.put(data.getBytes());
        }
    }
}
