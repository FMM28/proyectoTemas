package mx.unam.aragon.elZorro.service.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String from;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendSimpleEmail(String to, String subject, String text) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(from);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);
            mailSender.send(message);
            log.info("Correo simple enviado a {}", to);
        } catch (MailException ex) {
            log.error("Error al enviar correo simple a {}: {}", to, ex.getMessage(), ex);
        }
    }

    public void sendHtmlEmail(String to,
                              String subject,
                              String htmlContent,
                              byte[] attachment,
                              String attachmentName) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom(from);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlContent, true);

        if (attachment != null && attachmentName != null && !attachmentName.isBlank()) {
            helper.addAttachment(attachmentName, new ByteArrayResource(attachment));
        }

        try {
            mailSender.send(message);
            log.info("Correo HTML enviado a {} con asunto '{}'", to, subject);
        } catch (MailException ex) {
            log.error("Error al enviar correo HTML a {}: {}", to, ex.getMessage(), ex);
            throw ex;
        }
    }
}
