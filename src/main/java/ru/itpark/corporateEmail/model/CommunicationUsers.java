package ru.itpark.corporateEmail.model;

import lombok.Data;

@Data
public class CommunicationUsers {
    private Integer userRecipientId;
    private Integer userSenderId;
    private Integer countMessage;
}
