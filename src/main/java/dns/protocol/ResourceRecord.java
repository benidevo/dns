package dns.protocol;

import java.nio.ByteBuffer;

import dns.utils.Constants;

/**
 * Represents a DNS Resource Record as defined in RFC 1035.
 *
 * A Resource Record contains information about a domain name including its
 * type,
 * class, time-to-live and data specific to the record type. Common record types
 * include A (IPv4 address), AAAA (IPv6 address), MX (mail exchange), etc.
 *
 * This record implementation supports serialization to and deserialization from
 * DNS protocol wire format via ByteBuffer.
 *
 * @param domainName  the domain name this record applies to
 * @param recordType  the type of DNS record (e.g., A, AAAA, MX)
 * @param recordClass the class of the record (typically IN for Internet)
 * @param ttl         time-to-live in seconds, indicating how long the record
 *                    can be cached
 * @param length      the length of the record's data field in bytes
 * @param data        the record's data, format depends on record type (e.g., IP
 *                    address for A records)
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

    /**
     * Deserializes a Resource Record from the provided ByteBuffer.
     *
     * This method parses a DNS Resource Record according to the DNS protocol
     * format:
     * - First reads the domain name using the dot notation format
     * - Then extracts the record type, class, TTL, and data length
     * - Finally parses the data section based on the record type
     * - For 'A' records (IPv4 addresses), formats the 4 bytes as a dot-notation IP
     * address
     * - For other record types, returns the data as a string
     *
     * @param buffer The ByteBuffer containing the DNS message data positioned at
     *               the start of a Resource Record
     * @return A new ResourceRecord instance populated with the parsed data
     */
    static ResourceRecord deserializeFrom(ByteBuffer buffer) {
        StringBuilder domainName = new StringBuilder();
        byte labelLength;

        while ((labelLength = buffer.get()) > 0) {
            byte[] labelBytes = new byte[labelLength];
            buffer.get(labelBytes);
            String label = String.valueOf(labelBytes);

            if (domainName.length() > 0) {
                domainName.append(".");
            }
            domainName.append(label);
        }

        DnsRecord dnsRecord = DnsRecord.fromValue(buffer.getShort());
        int recordClass = buffer.getInt() & Constants.MAX_UNSIGNED_SHORT;
        int ttl = buffer.getInt();
        int length = buffer.getShort() & Constants.MAX_UNSIGNED_SHORT;

        String data;
        if (dnsRecord == DnsRecord.A && length == 4) {
            StringBuilder ipAddress = new StringBuilder();
            for (int i = 0; i < length; i++) {
                if (i > 0) {
                    ipAddress.append(".");
                }
                ipAddress.append(buffer.get() & Constants.MAX_UNSIGNED_BYTE);
            }
            data = ipAddress.toString();
        } else {
            byte[] dataBytes = new byte[length];
            buffer.get(dataBytes);
            data = new String(dataBytes);
        }

        return new ResourceRecord(domainName.toString(), dnsRecord, recordClass, ttl, length, data);
    }
}
