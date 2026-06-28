package com.university.portal.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "NOTIFICATIONS")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "notif_gen")
    @SequenceGenerator(name = "notif_gen", sequenceName = "NOTIF_SEQ", allocationSize = 1)
    @Column(name = "NOTIFICATION_ID")
    private Long notificationId;

    @Column(name = "TITLE", nullable = false, length = 100)
    private String title;

    @Column(name = "MESSAGE", nullable = false, length = 1000)
    private String message;

    @Column(name = "RECIPIENT_ID", nullable = false)
    private Long recipientId;

    @Column(name = "RECIPIENT_TYPE", nullable = false, length = 10)
    private String recipientType;

    @Column(name = "IS_READ")
    private Boolean isRead = false;

    @Column(name = "NOTIFICATION_TYPE", length = 30)
    private String notificationType;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATED_AT")
    private Date createdAt;

    @PrePersist
    protected void onCreate() { createdAt = new Date(); }

    // Getters and Setters
    public Long getNotificationId() { return notificationId; }
    public void setNotificationId(Long notificationId) { this.notificationId = notificationId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public Long getRecipientId() { return recipientId; }
    public void setRecipientId(Long recipientId) { this.recipientId = recipientId; }
    public String getRecipientType() { return recipientType; }
    public void setRecipientType(String recipientType) { this.recipientType = recipientType; }
    public Boolean getIsRead() { return isRead; }
    public void setIsRead(Boolean isRead) { this.isRead = isRead; }
    public String getNotificationType() { return notificationType; }
    public void setNotificationType(String notificationType) { this.notificationType = notificationType; }
    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
}
