package common.network;

import java.io.Serializable;
import java.time.LocalDateTime;


public abstract class NetworkPrimitive implements Serializable {

    private static final long serialVersionUID = 1L;
    private final LocalDateTime timestamp;

    public NetworkPrimitive() {
        this.timestamp = LocalDateTime.now();
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }


    public abstract Enum<?> getType();

    @Override
    public String toString() {
        return "NetworkPrimitive{" +
                "timestamp=" + timestamp +
                '}';
    }
}
