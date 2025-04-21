package dns.protocol;

import java.nio.ByteBuffer;

import dns.utils.Constants;

/**
 * Represents a DNS header as defined in RFC 1035.
 *
 * <p>
 * A DNS header contains information about the query or response including
 * identification, flags, and counts of different sections in the DNS message.
 * </p>
 *
 * <p>
 * The header is a 12-byte structure containing:
 * <ul>
 * <li>ID: A 16-bit identifier for the query/response</li>
 * <li>Various flag bits (QR, OPCODE, AA, TC, RD, RA, Z, RCODE)</li>
 * <li>Count fields for questions and resource records</li>
 * </ul>
 * </p>
 *
 * <p>
 * This implementation provides methods to create response headers,
 * serialize headers to byte buffers, and deserialize headers from byte buffers.
 * </p>
 *
 * @see <a href="https://tools.ietf.org/html/rfc1035">RFC 1035</a>
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

    private final static int HEADER_SIZE = 12;

    /**
     * Creates a DNS response header with specified parameters.
     *
     * @param id               The ID of the corresponding request
     * @param opCode           The operation code from the request
     * @param recursionDesired Whether recursion is desired, copied from the request
     * @return A Header object configured as a response
     */
    static Header toResponseHeader(int id, int opCode, boolean recursionDesired) {
        int responseCode = opCode == 0 ? 0 : 4;
        return new Header(
                id, true, (short) opCode, false, false, recursionDesired, false, (byte) 0, (byte) responseCode, 1, 1, 0,
                0);
    }

    /**
     * Returns the size of the header.
     *
     * @return the size of the header in bytes.
     */
    int size() {
        return HEADER_SIZE;
    }

    /**
     * Serializes the DNS header into the provided ByteBuffer according to RFC 1035.
     * Includes ID, flags, and section counts in the standard DNS header format.
     *
     * @param buffer The ByteBuffer to write the header data into
     */
    void serializeInto(ByteBuffer buffer) {
        buffer.putShort((short) id);

        int flags = 0;
        if (isResponse) {
            flags |= (1 << 15);
        }
        flags |= ((opCode & Constants.NIBBLE_MASK) << 11);
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
        flags |= ((reserved & Constants.BYTE_HIGH_BITS_MASK) << 4);
        flags |= (responseCode() & Constants.NIBBLE_MASK);
        buffer.putShort((short) flags);

        buffer.putShort((short) questionCount);
        buffer.putShort((short) answerRecordCount);
        buffer.putShort((short) authorityRecordCount);
        buffer.putShort((short) additionalRecordCount);
    }

    /**
     * Deserializes a DNS header from a byte buffer.
     *
     * <p>
     * This method reads the DNS header fields from the given ByteBuffer according
     * to
     * the standard DNS protocol format (RFC 1035). The header is 12 bytes long and
     * contains
     * information about the query or response.
     * </p>
     *
     * @param buffer The ByteBuffer containing the DNS header data to deserialize
     * @return A new Header object with the deserialized values
     */
    static Header deserializeFrom(ByteBuffer buffer) {
        short id = buffer.getShort();

        int flags = buffer.getShort() & Constants.MAX_UNSIGNED_SHORT;
        boolean isResponse = (flags & (1 << 15)) != 0;
        short opCode = (short) ((flags >> 11) & Constants.NIBBLE_MASK);
        boolean isAuthoritativeAnswer = (flags & (1 << 10)) != 0;
        boolean isTruncated = (flags & (1 << 9)) != 0;
        boolean recursionDesired = (flags & (1 << 8)) != 0;
        boolean recursionAvailable = (flags & (1 << 7)) != 0;
        byte reserved = (byte) ((flags >> 4) & Constants.BYTE_HIGH_BITS_MASK);
        byte responseCode = (byte) (flags & Constants.NIBBLE_MASK);

        int questionCount = buffer.getShort() & Constants.MAX_UNSIGNED_SHORT;
        int answerRecordCount = buffer.getShort() & Constants.MAX_UNSIGNED_SHORT;
        int authorityRecordCount = buffer.getShort() & Constants.MAX_UNSIGNED_SHORT;
        int additionalRecordCount = buffer.getShort() & Constants.MAX_UNSIGNED_SHORT;

        return new Header(
                id,
                isResponse,
                opCode,
                isAuthoritativeAnswer,
                isTruncated,
                recursionDesired,
                recursionAvailable,
                reserved,
                responseCode,
                questionCount,
                answerRecordCount,
                authorityRecordCount,
                additionalRecordCount);
    }
}
