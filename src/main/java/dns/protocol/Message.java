package dns.protocol;

/**
 * Represents a DNS message consisting of a header and a question.
 * <p>
 * This record encapsulates the structure of a DNS message, which includes
 * a {@link Header} and a {@link Question}. It provides methods for serializing
 * the message into a byte array and creating a response message.
 * </p>
 *
 * @param header   the header of the DNS message, containing metadata and flags
 * @param question the question section of the DNS message, representing the
 *                 query
 */
public record Message(Header header, Question question) {
    /**
     * Serializes the current message into a byte array by combining the serialized
     * representations of the header and the question sections.
     *
     * @return A byte array containing the serialized data of the message, which
     *         includes the header followed by the question section.
     */
    public byte[] serialize() {
        byte[] headerBytes = header.serialize();
        byte[] questionBytes = question.serialize();

        byte[] result = new byte[headerBytes.length + questionBytes.length];
        System.arraycopy(headerBytes, 0, result, 0, headerBytes.length);
        System.arraycopy(questionBytes, 0, result, headerBytes.length, questionBytes.length);

        return result;
    }

    /**
     * Creates a new response message for a DNS protocol.
     * <p>
     * This method generates a response message by creating a response header
     * and a corresponding question section. The resulting message is constructed
     * using these components.
     * </p>
     *
     * @return a {@link Message} instance representing the DNS response message.
     */
    public static Message createResponseMessage() {
        Header headerResponse = Header.createResponseHeader();
        Question question = Question.valueOf();
        return new Message(headerResponse, question);
    }
}
