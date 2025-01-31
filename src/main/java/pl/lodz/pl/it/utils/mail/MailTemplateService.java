package pl.lodz.pl.it.utils.mail;

import pl.lodz.pl.it.entity.Account;
import pl.lodz.pl.it.utils.Mail;
import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

@Service
@AllArgsConstructor
@Transactional
//@PreAuthorize("hasRole('ROLE_SYSTEM')")
public class MailTemplateService {

    private MessageSource messageSource;
    private final MailSenderService mailSenderService;


    public void sendEmailTemplate(Account mailTo, String mailSubject, String mailContent, Object[] contentArgs) {
        Locale locale = Locale.forLanguageTag("pl");
        String subject = messageSource.getMessage(mailSubject, null, locale);
        String mailBody = messageSource.getMessage(mailContent, contentArgs, locale);
        String name = messageSource.getMessage("mail.hello", new Object[] {mailTo.getFirstName()}, locale);
        String mailText = "<html> <body> <h2> " + name + "</h2>" + "<p> " + mailBody + " </p> </body> </html>";
        Mail mail = new Mail(mailTo.getEmail(), subject, mailText);

        System.out.println("sendMail");
        mailSenderService.sendEmail(mail);
    }

    public void sendEmailOnNewMail(Account mailTo, String mailSubject, String mailContent, Object[] contentArgs, String newEmail) {
        Locale locale = Locale.forLanguageTag("pl");
        String subject = messageSource.getMessage(mailSubject, null, locale);
        String mailBody = messageSource.getMessage(mailContent, contentArgs, locale);
        String name = messageSource.getMessage("mail.hello", new Object[] {mailTo.getFirstName()}, locale);
        String mailText = "<html> <body> <h2> " + name + "</h2>" + "<p> " + mailBody + " </p> </body> </html>";
        Mail mail = new Mail(newEmail, subject, mailText);

        mailSenderService.sendEmail(mail);
    }
}