package com.university.portal.service.impl;

import com.university.portal.dao.NotificationDAO;
import com.university.portal.model.Notification;
import com.university.portal.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class NotificationServiceImpl implements NotificationService {

    @Autowired private NotificationDAO notificationDAO;

    @Override
    @Transactional(readOnly = true)
    public List<Notification> getByRecipient(Long recipientId, String recipientType) {
        return notificationDAO.findByRecipient(recipientId, recipientType);
    }

    @Override
    @Transactional(readOnly = true)
    public long countUnread(Long recipientId, String recipientType) {
        return notificationDAO.countUnread(recipientId, recipientType);
    }

    @Override
    public Notification send(Long recipientId, String recipientType, String title, String message, String type) {
        Notification n = new Notification();
        n.setRecipientId(recipientId);
        n.setRecipientType(recipientType);
        n.setTitle(title);
        n.setMessage(message);
        n.setNotificationType(type);
        return notificationDAO.save(n);
    }

    @Override
    public void markAsRead(Long notificationId) {
        notificationDAO.markAsRead(notificationId);
    }

    @Override
    public void markAllAsRead(Long recipientId, String recipientType) {
        notificationDAO.markAllAsRead(recipientId, recipientType);
    }
}
