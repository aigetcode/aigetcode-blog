package com.blog.aigetcode.service.impl;

import com.blog.aigetcode.DTO.EmailDto;
import com.blog.aigetcode.exceptions.Check;
import com.blog.aigetcode.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements MailService {

    @Value("${email.recipient}")
    private String emailRecipient;

    private final JavaMailSender javaMailSender;

    @Autowired
    public EmailServiceImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendEmail(EmailDto emailDTO) {
        try {
            if (emailRecipient != null) {
                SimpleMailMessage msg = new SimpleMailMessage();
                msg.setTo(emailRecipient);
                msg.setSubject("Blog: " + emailDTO.getSubject());
                msg.setText("Name: " + emailDTO.getName() + "\n" +
                        "Email: " + emailDTO.getEmail() + "\n" +
                        "Text: \n" + emailDTO.getText() + "\n"
                );

                javaMailSender.send(msg);
            }
        } catch(Exception e) {
            Check.fail("Error send mail.");
        }
    }

}
