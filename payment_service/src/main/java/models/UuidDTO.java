package models;
import io.cucumber.messages.IdGenerator;

import java.util.UUID;

public class UuidDTO extends Payload {
    private UUID uuid;

    public UuidDTO(UUID uuid) {
        this.uuid = uuid;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }
}
