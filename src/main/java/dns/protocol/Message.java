package dns.protocol;

/**
 * Represents a DNS protocol message consisting of a header.
 *
 * <p>
 * This record encapsulates the structure of a DNS message, providing
 * methods for serialization and creating response messages.
 * </p>
 *
 * @param header The header of the DNS message, containing metadata and control
 *               information.
 */
public record Message(Header header) {
    /**
     * Serializes the current message into a byte array.
     * This method converts the header of the message into its byte representation
     * and returns it.
     *
     * @return a byte array containing the serialized representation of the message
     *         header.
     */
    public byte[] serialize() {
        byte[] headerBytes = header.serialize();

        return headerBytes;
    }

    /**
     * Creates a new response message.
     * <p>
     * This method generates a response message by creating a response header
     * and constructing a {@link Message} object with it.
     * </p>
     *
     * @return a {@link Message} instance representing the response message.
     */
    public static Message createResponseMessage() {
        Header headerResponse = Header.createResponseHeader();
        return new Message(headerResponse);
    }
}
