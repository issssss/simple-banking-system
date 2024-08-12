package org.simple.bankingsystem.email;

public interface EmailSenderService {
    void sendSimpleEmail(String to, String subject, String body);
}
