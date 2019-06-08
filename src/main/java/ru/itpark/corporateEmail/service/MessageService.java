package ru.itpark.corporateEmail.service;

import ru.itpark.corporateEmail.model.*;

import java.util.List;

public interface MessageService {

    List<Message> getAllIncomeMessageByStatus(User userRecipient, Integer page);

    List<Message> getAllUnReadMessageByStatus(User userRecipient, Integer page);

    List<Message> getAllDraftMessage(User userSender, Integer page);

    List<Message> getAllSendMessage(User userSender, Integer page);

    List<Message> getChainsMessages(Message message, Integer page);

    StatisticMessages getStatisticByUser(User user);

    void deleteDraftMessage(User user);

    void deleteIncomingMessage(User user);

    void deleteFromBasket(User user);

    List<DialogStatistic> getIgnoreUsers();

    List<DialogStatistic> getCommunicationUsers();

    Message save(Message message);


}
