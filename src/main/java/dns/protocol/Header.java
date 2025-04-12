package dns.protocol;

import java.nio.ByteBuffer;

/**
 * Represents the header section of a DNS message.
 *
 * <p>
 * The header contains metadata about the DNS message, such as whether it is
 * a query or a response, the operation code, and counts of various record
 * types.
 * This class is implemented as a Java record, which provides an immutable data
 * structure with built-in methods for equality, hash code, and string
 * representation.
 * </p>
 *
 * @param id                    A 16-bit identifier assigned by the program that
 *                              generates
 *                              any kind of query. This identifier is copied
 *                              into the
 *                              corresponding reply and can be used by the
 *                              requester to
 *                              match responses to outstanding queries.
 * @param isResponse            Indicates whether this message is a response
 *                              (true) or a
 *                              query (false).
 * @param opCode                A 4-bit field that specifies the kind of query
 *                              in this
 *                              message. For standard queries, this is 0.
 * @param isAuthoritativeAnswer Indicates whether the responding name server is
 *                              an
 *                              authoritative server for the domain name in
 *                              question.
 * @param isTruncated           Indicates whether this message was truncated due
 *                              to
 *                              exceeding the maximum allowable size.
 * @param recursionDesired      Indicates whether the client desires recursive
 *                              query
 *                              support.
 * @param recursionAvailable    Indicates whether the server supports recursive
 *                              queries.
 * @param reserved              A 3-bit reserved field, currently unused and
 *                              must be set
 *                              to 0 in queries and responses.
 * @param responseCode          A 4-bit field that indicates the status of the
 *                              response.
 *                              Common values include 0 (No error) and 3 (Name
 *                              error).
 * @param questionCount         The number of entries in the question section of
 *                              the
 *                              message.
 * @param answerRecordCount     The number of resource records in the answer
 *                              section of
 *                              the message.
 * @param authorityRecordCount  The number of name server resource records in
 *                              the
 *                              authority section of the message.
 * @param additionalRecordCount The number of resource records in the additional
 *                              section
 *                              of the message.
 */
record Header(

        int id,
        boolean isResponse,
        short opCode,
        boolean isAuthoritativeAnswer,
        boolean isTruncated,
        boolean recursionDesired,
        boolean recursionAvailable,
        byte reserved,
        byte responseCode,
        int questionCount,
        int answerRecordCount,
        int authorityRecordCount,
        int additionalRecordCount) {

    /**
     * Creates a response header for a DNS protocol message.
     *
     * @return a {@link Header} object initialized with default values for a DNS
     *         response. The header includes a transaction ID, response flag, and
     *         other default settings for a DNS response.
     */
    static Header createResponseHeader() {
        return new Header(
                1234, true, (short) 0, false, false, false, false, (byte) 0, (byte) 0, 1, 0, 0, 0);
    }

    /**
     * Serializes the DNS header into a byte array according to the DNS protocol
     * format.
     * The header is 12 bytes long and includes fields such as ID, flags, and record
     * counts.
     *
     * @return A byte array representing the serialized DNS header.
     *
     *         The serialized format includes:
     *         - 2 bytes: Transaction ID (ID)
     *         - 2 bytes: Flags (QR, Opcode, AA, TC, RD, RA, Z, RCODE)
     *         - 2 bytes: Number of questions (QDCOUNT)
     *         - 2 bytes: Number of answer records (ANCOUNT)
     *         - 2 bytes: Number of authority records (NSCOUNT)
     *         - 2 bytes: Number of additional records (ARCOUNT)
     */
    byte[] serialize() {

        ByteBuffer buffer = ByteBuffer.allocate(12);

        buffer.putShort((short) id);

        int flags = 0;
        if (isResponse) {
            flags |= (1 << 15);
        }
        flags |= (opCode) & 0xF << 11;
        if (isAuthoritativeAnswer) {
            flags |= (1 << 10);
        }
        if (isTruncated) {
            flags |= (1 << 9);
        }
        if (recursionDesired) {
            flags |= (1 << 8);
        }
        if (recursionAvailable) {
            flags |= (1 << 7);
        }
        flags |= ((reserved & 0x7) << 4);
        flags |= (responseCode() & 0xF);
        buffer.putShort((short) flags);

        buffer.putShort((short) questionCount);
        buffer.putShort((short) answerRecordCount);
        buffer.putShort((short) authorityRecordCount);
        buffer.putShort((short) additionalRecordCount);

        return buffer.array();
    }
}
