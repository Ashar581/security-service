package com.security.service.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class MailServiceImpl implements MailService{
    @Autowired
    JavaMailSender javaMailSender;
    @Override
    public void sendPlainEmail(String to, String subject, String body){
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setFrom("ashar.shahab@digitalsherpa.ai");
        mail.setTo(to);
        mail.setSubject(subject);
        mail.setText(body);
        javaMailSender.send(mail);
    }
    @Override
    @Async
    public void sendHtmlEmail(String to, String subject, String htmlBody) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper sender = new MimeMessageHelper(message,"utf-8");
        sender.setTo(to);
        sender.setText(htmlBody,true);
        sender.setSubject(subject);

        javaMailSender.send(message);
    }
}
