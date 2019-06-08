package ru.itpark.corporateEmail.entity;

import lombok.Builder;
import lombok.Data;
import ru.itpark.corporateEmail.model.MessageRecipientStatus;
import ru.itpark.corporateEmail.model.MessageSenderStatus;

import javax.persistence.*;
import java.util.Date;


@Entity
@Data
@Builder
@Table(name = "messages")
public class MessageEntity {
    @Id
    @GeneratedValue
    private Integer id;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "reply_message_id")
    private MessageEntity replyMessage;
    
    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
    @JoinColumn(name = "user_recipient_id")
    private UserEntity userRecipient;

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
    @JoinColumn(name = "user_sender_id")
    private UserEntity userSender;

    @Column(name = "text_msg")
    private String textMessage;

    @Column(name = "send_dt")
    private Date dateOfSend;

    @Column(name = "is_read")
    private Boolean isRead;

    @Column(name = "is_send")
    private Boolean isSend;

    @Enumerated(EnumType.STRING)
    @Column(name = "message_sender_status")
    private MessageSenderStatus messageSenderStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "message_recipient_status")
    private MessageRecipientStatus messageRecipientStatus;


}
