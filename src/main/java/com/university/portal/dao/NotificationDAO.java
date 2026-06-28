package com.university.portal.dao;

import com.university.portal.model.Notification;
import java.util.List;

public interface NotificationDAO {
    Notification findById(Long notificationId);
    List<Notification> findByRecipient(Long recipientId, String recipientType);
    List<Notification> findUnreadByRecipient(Long recipientId, String recipientType);
    long countUnread(Long recipientId, String recipientType);
    Notification save(Notification notification);
    void markAsRead(Long notificationId);
    void markAllAsRead(Long recipientId, String recipientType);
}
