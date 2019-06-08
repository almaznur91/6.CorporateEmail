package ru.itpark.corporateEmail.model;

import lombok.Data;

@Data
public class StatisticMessages {
    private Integer countIncomingMessage;
    private Integer countUnreadMessage;
    private Integer countSendMessage;
    private Integer countDraftMessage;
}
