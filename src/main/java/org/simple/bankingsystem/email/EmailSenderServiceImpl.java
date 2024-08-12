package org.simple.bankingsystem.email;


import org.simple.bankingsystem.SimpleBankingSystemApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service("MailService")
public class EmailSenderServiceImpl implements EmailSenderService{

    private final JavaMailSender emailSender;
    private final Logger log = LoggerFactory.getLogger(SimpleBankingSystemApplication.class);

    private static final String NOREPLY_ADDRESS = "noreply@simplebank.com";

    @Autowired
    public EmailSenderServiceImpl(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    public void sendSimpleEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
       try {
           message.setFrom(NOREPLY_ADDRESS);
           message.setTo(to);
           message.setSubject(subject);
           message.setText(body);
           emailSender.send(message);
       }
       catch (Exception e) {
           log.error(e.getMessage());
       }
    }

}
