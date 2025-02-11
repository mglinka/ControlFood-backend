package pl.lodz.pl.it.utils.mail;

import pl.lodz.pl.it.entity.Account;
import pl.lodz.pl.it.utils._enum.AccountRoleEnum;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Transactional
//@PreAuthorize("hasRole('ROLE_SYSTEM')")
public class MailService {

    private final MailTemplateService mailTemplateService;



    public void sendEmailToVerifyAccount(Account account, String randString) {
        System.out.println("Mailservice start");
        StringBuilder sb = new StringBuilder();
        sb.append("<a href='http://localhost:8080/api/v1/auth/verify-account/");
        sb.append(randString);
        sb.append("'>Link</a>");
        System.out.println("Mailservice wywoluje sendEMailTemplate");
        mailTemplateService.sendEmailTemplate(account, "mail.verify.account.subject",
                "mail.verify.account.body", new Object[] {sb});
    }

    public void sendEmailToInformAboutVerification(Account account) {
        mailTemplateService.sendEmailTemplate(account, "mail.after.verify.subject", "mail.after.verify.body",
                new Object[] {AccountRoleEnum.ROLE_USER});
    }





}