package ru.itpark.corporateEmail.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.itpark.corporateEmail.entity.MessageEntity;
import ru.itpark.corporateEmail.model.CommunicationUsers;
import ru.itpark.corporateEmail.model.MessageRecipientStatus;
import ru.itpark.corporateEmail.model.MessageSenderStatus;
import ru.itpark.corporateEmail.model.StatisticMessages;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<MessageEntity, Integer> {


    /**
     * Просмотр входящий сообщений у пользователя, согласно статусу сообщения {@link MessageRecipientStatus}
     */
    List<MessageEntity> getAllByMessageRecipientStatusAndUserRecipientIdAndIsSendIsTrue(
            MessageRecipientStatus messageRecipientStatus, Integer userRecipientId, Pageable pageable
    );

    /**
     * Просмотр непрочитанных входящий сообщений у пользователя, согласно статусу сообщения {@link MessageRecipientStatus}
     */
    List<MessageEntity> getAllByMessageRecipientStatusAndUserRecipientIdAndIsSendIsTrueAndIsReadIsFalse(
            MessageRecipientStatus messageRecipientStatus, Integer userRecipientId, Pageable pageable
    );

    /**
     * Просмотр отправленных/черновых сообщений у пользователя, согласно статусу сообщения {@link MessageSenderStatus}
     */
    List<MessageEntity> getAllByMessageSenderStatusAndUserSenderIdAndIsSend(
            MessageSenderStatus messageSenderStatus, Integer userSenderId, Boolean isSend, Pageable pageable
    );


    /**
     * Просмотр цепочки сообщений
     */
    @Query(value = "WITH RECURSIVE r AS (\n" +
            "  SELECT id, send_dt, is_read, message_recipient_status, message_sender_status, text_msg, reply_message_id, user_recipient_id, user_sender_id, is_send\n" +
            "  FROM messages\n" +
            "  WHERE messages.id = :messageId\n" +
            "\n" +
            "  UNION ALL\n" +
            "\n" +
            "  SELECT m.id, m.send_dt, m.is_read, m.message_recipient_status, m.message_sender_status, m.text_msg, m.reply_message_id, m.user_recipient_id, m.user_sender_id, m.is_send\n" +
            "  FROM messages m\n" +
            "         JOIN r ON m.reply_message_id= r.id\n" +
            ")\n" +
            "SELECT * FROM r  ORDER BY ?#{#pageable}",
            countQuery = "WITH RECURSIVE r AS (\n" +
                    "  SELECT id, send_dt, is_read, message_recipient_status, message_sender_status, text_msg, reply_message_id, user_recipient_id, user_sender_id, is_send\n" +
                    "  FROM messages\n" +
                    "  WHERE messages.id = :messageId\n" +
                    "\n" +
                    "  UNION ALL\n" +
                    "\n" +
                    "  SELECT m.id, m.send_dt, m.is_read, m.message_recipient_status, m.message_sender_status, m.text_msg, m.reply_message_id, m.user_recipient_id, m.user_sender_id, m.is_send\n" +
                    "  FROM messages m\n" +
                    "         JOIN r ON m.reply_message_id= r.id\n" +
                    ")\n" +
                    "SELECT count(1) FROM r", nativeQuery = true)
    List<MessageEntity> getChainsMessagesByMessageId(Integer messageId, Pageable pageable);


    @Query(value = "update messages set message_sender_status = :status where user_sender_id = :userSenderId", nativeQuery = true)
    void updateMessageSenderStatusByUserId(@Param("userSenderId") Integer userSenderId, @Param("status") MessageSenderStatus status);

    @Query(value = "update messages set message_sender_status = :status where user_recipient_id = :userRecipientId", nativeQuery = true)
    void updateMessageRecipientStatusByUserId(@Param("userRecipientId") Integer userRecipientId, @Param("status") MessageRecipientStatus status);

    @Query(value = "delete from messages where user_sender_id = 'DELETE' true and user_recipient_id = 'DELETE'", nativeQuery = true)
    void deleteAllMessage();

    /**
     * Общая статистика по ящику (в одном запросе):
     */
    @Query(value = "select (select count(1) AS countDraftMessage\n" +
            "        FROM messages\n" +
            "        WHERE user_sender_id = :userId\n" +
            "          and is_send is true\n" +
            "          and message_sender_status = 'DRAFT'\n" +
            "        limit 1),\n" +
            "       (select count(1) as countSendMessage\n" +
            "        FROM messages\n" +
            "        WHERE user_sender_id = :userId\n" +
            "          and is_send is true\n" +
            "          and message_sender_status = 'OK'\n" +
            "        limit 1),\n" +
            "       (select count(1) AS countUnreadMessage\n" +
            "        FROM messages\n" +
            "        WHERE user_recipient_id = :userId\n" +
            "          and is_send is true\n" +
            "          and is_read is false\n" +
            "          and message_recipient_status = 'OK'\n" +
            "        limit 1),\n" +
            "       (select count(1) as countIncomingMessage\n" +
            "        FROM messages\n" +
            "        WHERE user_recipient_id = :userId\n" +
            "          and is_send is true\n" +
            "          and is_read is true\n" +
            "          and message_recipient_status = 'OK'\n" +
            "        limit 1)\n" +
            "from messageslimit\n" +
            "     1;", nativeQuery = true)
    StatisticMessages getStatisticByUsersId(@Param("userId") Integer userId);

    /**
     * Кто с кем переписывается
     */
    @Query(value = "select user_sender_id,user_recipient_id, count(user_recipient_id)\n" +
            "from messages where reply_message_id notnull \n" +
            "group by user_sender_id,user_recipient_id\n" +
            "having count(1) > 1;", nativeQuery = true)
    List<CommunicationUsers> getCommunicationUsers();

    /**
     * Кто кого игнорирует
     */
    @Query(value = "select user_sender_id,user_recipient_id, count(user_recipient_id)\n" +
            "from messages where reply_message_id is null \n" +
            "group by user_sender_id,user_recipient_id\n" +
            "having count(1) > 1;", nativeQuery = true)
    List<CommunicationUsers> getUnCommunicationUsers();


}

