package dns.protocol;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
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
     * Serializes the DNS question into a byte array.
     * The serialization includes the domain name, record type, and question class.
     *
     * @return A byte array representing the serialized DNS question.
     * @throws RuntimeException If an I/O error occurs during serialization.
     */
    byte[] serialize() {

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            String[] labels = domainName.split("\\.");
            for (String label : labels) {
                outputStream.write(label.length());
                outputStream.write(label.getBytes());
            }
            outputStream.write(0);

            ByteBuffer buffer = ByteBuffer.allocate(4);
            buffer.putShort((short) recordType.getValue());
            buffer.putShort((short) questionClass);

            outputStream.write(buffer.array());

            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Failed to Serialize DNS question", e);
        }
    }
}
