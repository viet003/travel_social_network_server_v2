package api.v2.travel_social_network_server.services.mail;

import api.v2.travel_social_network_server.dtos.mail.MailDto;
import jakarta.mail.MessagingException;

import java.util.Map;

public interface IMailService {
    void sendMail(MailDto mailDto);
}
