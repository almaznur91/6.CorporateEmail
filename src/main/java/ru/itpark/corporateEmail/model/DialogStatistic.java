package ru.itpark.corporateEmail.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DialogStatistic {
    User userSender;
    User userRecipient;
    Integer countMessage;
}
