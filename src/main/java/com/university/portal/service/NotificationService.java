package com.university.portal.service;

import com.university.portal.model.Notification;
import java.util.List;

public interface NotificationService {
    List<Notification> getByRecipient(Long recipientId, String recipientType);
    long countUnread(Long recipientId, String recipientType);
    Notification send(Long recipientId, String recipientType, String title, String message, String type);
    void markAsRead(Long notificationId);
    void markAllAsRead(Long recipientId, String recipientType);
}
