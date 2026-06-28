package com.university.portal.controller;

import com.university.portal.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired private NotificationService notificationService;

    @GetMapping
    public ResponseEntity<?> getMyNotifications(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        String role  = (String) session.getAttribute("role");
        if (userId == null) return ResponseEntity.status(401).build();
        String recipientType = "FACULTY".equals(role) ? "FACULTY" : "STUDENT";
        return ResponseEntity.ok(notificationService.getByRecipient(userId, recipientType));
    }

    @GetMapping("/unread-count")
    public ResponseEntity<?> getUnreadCount(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        String role  = (String) session.getAttribute("role");
        if (userId == null) return ResponseEntity.status(401).build();
        String recipientType = "FACULTY".equals(role) ? "FACULTY" : "STUDENT";
        long count = notificationService.countUnread(userId, recipientType);
        return ResponseEntity.ok(Map.of("count", count));
    }

    @PutMapping("/{id}/read")
    public ResponseEntity<?> markRead(@PathVariable Long id, HttpSession session) {
        if (session.getAttribute("userId") == null) return ResponseEntity.status(401).build();
        notificationService.markAsRead(id);
        return ResponseEntity.ok(Map.of("success", true));
    }

    @PutMapping("/read-all")
    public ResponseEntity<?> markAllRead(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        String role  = (String) session.getAttribute("role");
        if (userId == null) return ResponseEntity.status(401).build();
        String recipientType = "FACULTY".equals(role) ? "FACULTY" : "STUDENT";
        notificationService.markAllAsRead(userId, recipientType);
        return ResponseEntity.ok(Map.of("success", true));
    }
}
