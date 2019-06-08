package ru.itpark.corporateEmail.model;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class Message {

    private Integer id;
    private Integer replyMessageId;
    private Integer userRecipientId;
    private Integer userSenderId;
    private String textMessage;
    private Date dateOfSend;
    private Boolean isRead;
    private MessageSenderStatus messageSenderStatus;
    private MessageRecipientStatus messageRecipientStatus;

}
