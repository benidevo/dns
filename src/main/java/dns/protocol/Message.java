package dns.protocol;

import java.nio.ByteBuffer;

/**
 * Represents a DNS message consisting of a header, a question, and an answer
 * section.
 * <p>
 * This record encapsulates the components of a DNS message, including:
 * </p>
 * <ul>
 * <li>{@link Header} - The header section containing metadata about the DNS
 * message.</li>
 * <li>{@link Question} - The question section specifying the query
 * details.</li>
 * <li>{@link ResourceRecord} - The answer section containing the response
 * data.</li>
 * </ul>
 * <p>
 * The {@code Message} record provides methods to serialize the DNS message into
 * a byte array
 * and to create a response message for a DNS query.
 * </p>
 *
 * @param header   The header section of the DNS message.
 * @param question The question section of the DNS message.
 * @param answer   The answer section of the DNS message.
 */
public record Message(Header header, Question question, ResourceRecord answer) {
    /**
     * Serializes the DNS message into a byte array. The method combines the
     * serialized
     * representations of the header, question, and answer sections of the DNS
     * message.
     *
     * @return A byte array containing the serialized DNS message.
     */
    public byte[] serialize() {
        ByteBuffer buffer = ByteBuffer.allocate(header.size() + question.size() + answer.size());
        header.serializeInto(buffer);
        question.serializeInto(buffer);
        answer.serializeInto(buffer);

        return buffer.array();
    }

    /**
     * Creates a new response message for a DNS query.
     * <p>
     * This method constructs a DNS response message by creating a response header,
     * a question section, and an answer section. The response message is then
     * assembled using these components.
     * </p>
     *
     * @return a {@link Message} object representing the DNS response message.
     */
    public static Message createResponseMessage() {
        Header headerResponse = Header.createResponseHeader();
        Question question = Question.valueOf();
        ResourceRecord answer = ResourceRecord.valueOf();
        return new Message(headerResponse, question, answer);
    }
}
