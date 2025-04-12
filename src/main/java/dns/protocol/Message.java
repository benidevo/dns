package dns.protocol;

import java.nio.ByteBuffer;

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
     * Serializes the DNS message into a byte array.
     * <p>
     * This method combines the serialized representations of the header and
     * question
     * sections of the DNS message into a single byte array. The size of the buffer
     * is determined by the combined sizes of the header and question sections.
     * </p>
     *
     * @return A byte array containing the serialized DNS message.
     */
    public byte[] serialize() {
        ByteBuffer buffer = ByteBuffer.allocate(header.size() + question.size());
        header.serializeInto(buffer);
        question.serializeInto(buffer);

        return buffer.array();
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
