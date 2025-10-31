package common.network;

public class Message extends NetworkPrimitive {

    public enum MessageType {
        ELECTION_DATA,
        VOTE_SUBMISSION,
        SERVER_RESPONSE,
        ERROR,
        END_ELECTION
    }

    private final MessageType type;
    private final Object data;

    public Message(MessageType type, Object data) {
        super();
        this.type = type;
        this.data = data;
    }

    @Override
    public MessageType getType() {
        return type;
    }

    public Object getData() {
        return data;
    }

    @Override
    public String toString() {
        return "Message{" +
                "type=" + type +
                ", data=" + data +
                '}';
    }
}
