package common.network;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Message implements Serializable {

    public enum MessageType {
        ELECTION_DATA,
        VOTE_SUBMISSION,
        SERVER_RESPONSE,
        ERROR,
        END_ELECTION
    }

    private final MessageType type;
    private final Object data;
    private final LocalDateTime timestamp;

    public Message(MessageType type, Object data) {
        this.type = type;
        this.data = data;
        this.timestamp = LocalDateTime.now();
    }

    public MessageType getType() {
        return type;
    }

    public Object getData() {
        return data;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "Message{" +
                "type=" + type +
                ", data=" + data +
                ", timestamp=" + timestamp +
                '}';
    }
}
