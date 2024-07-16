package com.security.service.Service;


import jakarta.mail.MessagingException;

public interface MailService {
    void sendPlainEmail(String to, String subject, String body);
    public void sendHtmlEmail(String to, String subject, String htmlBody) throws MessagingException;
}
