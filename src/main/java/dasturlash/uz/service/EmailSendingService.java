package dasturlash.uz.service;

import dasturlash.uz.enums.AppLanguage;
import dasturlash.uz.exps.AppBadException;
import dasturlash.uz.util.JwtUtil;
import dasturlash.uz.util.RandomUtil;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class EmailSendingService {
    private Integer emailLimit = 3;
    @Value("${spring.mail.username}")
    private String fromAccount;

    @Value("${server.domain}")
    private String serverDomain;

    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private EmailHistoryService emailHistoryService;

    public void sendRegistrationEmail(String email, String profileId, AppLanguage lang) {
        String subject = "Complete Registration";
        String body = "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title>Title</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "\n" +
                "<h1>Mazgigram.uz complete registration. You are becoming onr of the mazgi.</h1>\n" +
                "<p>\n" +
                "    Please click to button for completing registration: <a href=\"%s/auth/registration/email-verification/%s?lang=%s\"\n" +
                "                                                           target=\"_blank\">I will mazgi</a>\n" +
                "</p>\n\n" +
                "</body>\n" +
                "</html>";
        body = String.format(body, serverDomain, JwtUtil.encode(profileId), lang.name());
        System.out.println(JwtUtil.encode(profileId));
        sendMimeEmail(email, subject, body);
    }

    public void sendResetPasswordEmail(String email, AppLanguage lang) {
        String subject = "Reset Password Confirmation";
        String code = RandomUtil.getRandomSmsCode();
        String body = "How are you mazgi. This is your confirm code for reset password. Your Code: " + code;
        checkAndSendMimeEmail(email, subject, body, code);
    }

    public void sendChangeUsernameEmail(String email, AppLanguage lang) {
        String subject = "Username change confirmation";
        String code = RandomUtil.getRandomSmsCode();
        String body = "How are you mazgi. This is your confirm code for changing username. Your Code: " + code;
        checkAndSendMimeEmail(email, subject, body, code);
    }

    private void checkAndSendMimeEmail(String email, String subject, String body, String code) {
        // check
        Long count = emailHistoryService.getEmailCount(email);
        if (count >= emailLimit) {
            System.out.println(" --- Email Limit reached. Email: " + email);
            throw new AppBadException("Sms limit reached");
        }
        // send
        sendMimeEmail(email, subject, body);
        // create
        emailHistoryService.create(email, code);
    }

    private void sendMimeEmail(String email, String subject, String body) {
        try {
            MimeMessage msg = javaMailSender.createMimeMessage();
            msg.setFrom(fromAccount);

            MimeMessageHelper helper = new MimeMessageHelper(msg, true);
            helper.setTo(email);
            helper.setSubject(subject);
            helper.setText(body, true);

            CompletableFuture.runAsync(() -> {
                javaMailSender.send(msg);
            });
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendSimpleEmail(String email, String subject, String body) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom(fromAccount);
        msg.setTo(email);
        msg.setSubject(subject);
        msg.setText(body);
        javaMailSender.send(msg);
    }
}
