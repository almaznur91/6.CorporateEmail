package ru.itpark.corporateEmail.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.itpark.corporateEmail.entity.MessageEntity;
import ru.itpark.corporateEmail.mapper.MessageMapper;
import ru.itpark.corporateEmail.mapper.UserMapper;
import ru.itpark.corporateEmail.model.*;
import ru.itpark.corporateEmail.repository.MessageRepository;
import ru.itpark.corporateEmail.repository.UserRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {
    private final MessageRepository messageRepository;
    private final MessageMapper messageMapper;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public Message save(Message message) {
        MessageEntity messageEntity = messageRepository.save(messageMapper.messageToMessageEntity(message));
        return messageMapper.messageEntityToMessage(messageEntity);
    }

    @Scheduled(cron = "0 0 17 * * ?")
    public void deleteAllMessageByStatusIsDeleteTrue() {
        messageRepository.deleteAllMessage();
    }

    @Override
    public List<Message> getAllIncomeMessageByStatus(User userRecipient, Integer page) {
        List<MessageEntity> messageEntities = messageRepository.getAllByMessageRecipientStatusAndUserRecipientIdAndIsSendIsTrue(
                MessageRecipientStatus.OK, userRecipient.getId(), PageRequest.of(page, 50)
        );
        return extractMessages(messageEntities);
    }

    @Override
    public List<Message> getAllUnReadMessageByStatus(User userRecipient, Integer page) {
        List<MessageEntity> messageEntities = messageRepository.getAllByMessageRecipientStatusAndUserRecipientIdAndIsSendIsTrueAndIsReadIsFalse(
                MessageRecipientStatus.OK, userRecipient.getId(), PageRequest.of(page, 50)
        );
        return extractMessages(messageEntities);
    }

    @Override
    public List<Message> getAllDraftMessage(User userSender, Integer page) {
        List<MessageEntity> messageEntities = messageRepository.getAllByMessageSenderStatusAndUserSenderIdAndIsSend(
                MessageSenderStatus.DRAFT, userSender.getId(), false, PageRequest.of(page, 50)
        );
        return extractMessages(messageEntities);
    }

    @Override
    public List<Message> getAllSendMessage(User userSender, Integer page) {
        List<MessageEntity> messageEntities = messageRepository.getAllByMessageSenderStatusAndUserSenderIdAndIsSend(
                MessageSenderStatus.OK, userSender.getId(), true, PageRequest.of(page, 50)
        );
        return extractMessages(messageEntities);
    }

    @Override
    public List<Message> getChainsMessages(Message message, Integer page) {
        List<MessageEntity> messageEntities = messageRepository.getChainsMessagesByMessageId(
                message.getId(), PageRequest.of(page, 50)
        );
        return extractMessages(messageEntities);
    }

    @Override
    public StatisticMessages getStatisticByUser(User user) {
        return messageRepository.getStatisticByUsersId(user.getId());
    }

    @Override
    public void deleteDraftMessage(User user) {
        messageRepository.updateMessageSenderStatusByUserId(user.getId(), MessageSenderStatus.BASKET);
    }

    @Override
    public void deleteIncomingMessage(User user) {
        messageRepository.updateMessageRecipientStatusByUserId(user.getId(), MessageRecipientStatus.BASKET);
    }

    @Override
    public void deleteFromBasket(User user) {
        messageRepository.updateMessageRecipientStatusByUserId(user.getId(), MessageRecipientStatus.DELETE);
        messageRepository.updateMessageSenderStatusByUserId(user.getId(), MessageSenderStatus.DELETE);
    }

    @Override
    public List<DialogStatistic> getIgnoreUsers() {
        List<CommunicationUsers> communicationUsers = messageRepository.getCommunicationUsers();
        return extractDialogStatistic(communicationUsers);
    }

    @Override
    public List<DialogStatistic> getCommunicationUsers() {
        List<CommunicationUsers> communicationUsers = messageRepository.getUnCommunicationUsers();
        return extractDialogStatistic(communicationUsers);
    }

    private List<Message> extractMessages(List<MessageEntity> messageEntities) {
        if (messageEntities.isEmpty()) {
            return Collections.emptyList();
        } else {
            return messageEntities.stream().map(messageMapper::messageEntityToMessage).collect(Collectors.toList());
        }
    }

    private List<DialogStatistic> extractDialogStatistic(List<CommunicationUsers> communicationUsers) {
        List<DialogStatistic> dialogStatistics = new ArrayList<>();

        communicationUsers.forEach(communicationUsers1 -> {
            dialogStatistics.add(DialogStatistic.builder()
                    .userRecipient(userMapper.userEntityToUser(userRepository.getOne(communicationUsers1.getUserRecipientId())))
                    .userSender(userMapper.userEntityToUser(userRepository.getOne(communicationUsers1.getUserSenderId())))
                    .countMessage(communicationUsers1.getCountMessage())
                    .build());
        });

        return dialogStatistics;
    }

}
