package dns.protocol;

import java.nio.ByteBuffer;

/**
 * Represents a DNS question, which is a query sent to a DNS server.
 * A DNS question consists of a domain name, a record type, and a question
 * class.
 *
 * @param domainName    The domain name being queried (e.g., "example.com").
 * @param recordType    The type of DNS record being requested (e.g., A, AAAA,
 *                      MX).
 * @param questionClass The class of the DNS query, typically 1 for Internet
 *                      (IN).
 */
record Question(String domainName, DnsRecord recordType, int questionClass) {
    static Question valueOf() {
        return new Question("codecrafters.io", DnsRecord.A, 1);
    }

    /**
     * Calculates the size of the DNS question in bytes.
     *
     * The size is determined by:
     * <ul>
     * <li>The length of each label in the domain name, including 1 byte for each
     * label's length.</li>
     * <li>1 byte for the terminating zero that marks the end of the domain
     * name.</li>
     * <li>4 bytes for the record type (2 bytes) and record class (2 bytes).</li>
     * </ul>
     *
     * @return The total size of the DNS question in bytes.
     */
    int size() {

        int size = 0;

        String[] labels = domainName.split("\\.");
        for (String label : labels) {
            size += 1;
            size += label.getBytes().length;
        }

        size += 1; // 1 byte for the terminating zero
        size += 4; // 2 bytes for the record type and 2 bytes for record class

        return size;
    }

    /**
     * Serializes the DNS question into the provided ByteBuffer.
     *
     * This method converts the domain name into a sequence of labels, each prefixed
     * by its length, and appends them to the buffer. It also appends the record
     * type
     * and question class in their respective binary formats.
     *
     * @param buffer the ByteBuffer into which the DNS question will be serialized.
     *               The buffer must have sufficient space to accommodate the
     *               serialized data.
     * @throws BufferOverflowException if the buffer does not have enough space to
     *                                 hold the serialized data.
     */
    void serializeInto(ByteBuffer buffer) {

        String[] labels = domainName.split("\\.");
        for (String label : labels) {
            buffer.put((byte) label.length());
            buffer.put(label.getBytes());
        }
        buffer.put((byte) 0);

        buffer.putShort((short) recordType.getValue());
        buffer.putShort((short) questionClass);
    }
}
