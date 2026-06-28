package com.university.portal.dao.impl;

import com.university.portal.dao.NotificationDAO;
import com.university.portal.model.Notification;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class NotificationDAOImpl implements NotificationDAO {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public Notification findById(Long notificationId) {
        return sessionFactory.getCurrentSession().get(Notification.class, notificationId);
    }

    @Override
    public List<Notification> findByRecipient(Long recipientId, String recipientType) {
        Query<Notification> q = sessionFactory.getCurrentSession().createQuery(
            "FROM Notification n WHERE n.recipientId = :id AND n.recipientType = :type ORDER BY n.createdAt DESC",
            Notification.class);
        q.setParameter("id", recipientId);
        q.setParameter("type", recipientType);
        return q.list();
    }

    @Override
    public List<Notification> findUnreadByRecipient(Long recipientId, String recipientType) {
        Query<Notification> q = sessionFactory.getCurrentSession().createQuery(
            "FROM Notification n WHERE n.recipientId = :id AND n.recipientType = :type AND n.isRead = false ORDER BY n.createdAt DESC",
            Notification.class);
        q.setParameter("id", recipientId);
        q.setParameter("type", recipientType);
        return q.list();
    }

    @Override
    public long countUnread(Long recipientId, String recipientType) {
        Query q = sessionFactory.getCurrentSession().createQuery(
            "SELECT COUNT(n) FROM Notification n WHERE n.recipientId = :id AND n.recipientType = :type AND n.isRead = false");
        q.setParameter("id", recipientId);
        q.setParameter("type", recipientType);
        return (Long) q.uniqueResult();
    }

    @Override
    public Notification save(Notification notification) {
        sessionFactory.getCurrentSession().save(notification);
        return notification;
    }

    @Override
    public void markAsRead(Long notificationId) {
        Notification n = findById(notificationId);
        if (n != null) { n.setIsRead(true); sessionFactory.getCurrentSession().update(n); }
    }

    @Override
    public void markAllAsRead(Long recipientId, String recipientType) {
        sessionFactory.getCurrentSession().createQuery(
            "UPDATE Notification SET isRead = true WHERE recipientId = :id AND recipientType = :type")
            .setParameter("id", recipientId)
            .setParameter("type", recipientType)
            .executeUpdate();
    }
}
