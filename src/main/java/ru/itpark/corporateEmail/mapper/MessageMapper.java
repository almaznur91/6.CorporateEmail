package ru.itpark.corporateEmail.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.itpark.corporateEmail.entity.MessageEntity;
import ru.itpark.corporateEmail.model.Message;
import ru.itpark.corporateEmail.repository.MessageRepository;
import ru.itpark.corporateEmail.repository.UserRepository;

@Component
@RequiredArgsConstructor
public class MessageMapper {
    private final UserRepository userRepository;
    private final MessageRepository messageRepository;

    public MessageEntity messageToMessageEntity(Message message) {

        return MessageEntity.builder()
                .id(message.getId())
                .dateOfSend(message.getDateOfSend())
                .isRead(message.getIsRead())
                .replyMessage(messageRepository.findById(message.getReplyMessageId())
                        .orElseThrow(() -> new IllegalArgumentException("No Message found by id")))
                .userRecipient(userRepository.findById(message.getUserRecipientId())
                        .orElseThrow(() -> new IllegalArgumentException("No User found by id")))
                .userSender(userRepository.findById(message.getUserSenderId())
                        .orElseThrow(() -> new IllegalArgumentException("No User found by id")))
                .textMessage(message.getTextMessage())
                .build();
    }

    public Message messageEntityToMessage(MessageEntity messageEntity) {
        return Message.builder()
                .id(messageEntity.getId())
                .dateOfSend(messageEntity.getDateOfSend())
                .isRead(messageEntity.getIsRead())
                .messageSenderStatus(messageEntity.getMessageSenderStatus())
                .messageRecipientStatus(messageEntity.getMessageRecipientStatus())
                .replyMessageId(messageEntity.getReplyMessage().getId())
                .userRecipientId(messageEntity.getUserRecipient().getId())
                .userSenderId(messageEntity.getUserSender().getId())
                .textMessage(messageEntity.getTextMessage())
                .build();

    }

}
