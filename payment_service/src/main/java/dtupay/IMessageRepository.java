package dtupay;

import dto.TokenDTO;
import models.Message;

import java.util.UUID;

/**
 * @author Mikkel Rosenfeldt Anderson & Laura & Benjamin
 */
public interface IMessageRepository {

    UUID saveMessageObject(Message message);

    Message getMessageObject(UUID requestId);

    void removeMessageObject(UUID requestId);

}
