package dns.protocol;

import java.nio.ByteBuffer;

/**
 * Represents a DNS message consisting of a header, question, and answer record.
 * <p>
 * A DNS message is the fundamental unit of communication in the DNS protocol,
 * containing sections for the header, which provides metadata about the
 * message,
 * a question section that contains the query information, and an answer section
 * that contains resource records responding to a query.
 * </p>
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
     * Deserializes a DNS message from a byte array.
     *
     * @param requestBuffer The byte array containing the serialized DNS message.
     * @return A new Message object deserialized from the given byte array.
     */
    public static Message deserialize(byte[] requestBuffer) {
        ByteBuffer buffer = ByteBuffer.wrap(requestBuffer);
        Header header = Header.deserializeFrom(buffer);
        Question question = Question.deserializeFrom(buffer);
        ResourceRecord answer = ResourceRecord.deserializeFrom(buffer);

        return new Message(header, question, answer);
    }

    /**
     * Creates a new response message for a DNS query.
     * <p>
     * This method constructs a DNS response message by creating a response header,
     * a question section, and an answer section.
     *
     * @return a {@link Message} object representing the DNS response message.
     */
    public static Message toDnsResponse(Message requestMessage) {
        Header headerResponse = Header.toResponseHeader(requestMessage.header().id(), requestMessage.header().opCode(),
                requestMessage.header().recursionDesired());
        Question question = Question.toResponseQuestion(requestMessage.question().domainName());
        ResourceRecord answer = ResourceRecord.toResponseResourceRecord(requestMessage.question().domainName());
        return new Message(headerResponse, question, answer);
    }
}
