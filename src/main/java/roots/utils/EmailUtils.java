package roots.utils;

import jakarta.mail.Authenticator;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;

import java.util.Properties;

public class EmailUtils {
    public static void main(String[] args) {
        final String emailFrom = "sakuraminekochan@gmail.com";
        final String password = "yhxtlucitucjlwse";

        final String emailTo = "myuyen120806@gmail.com";

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Authenticator authenticator = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(emailFrom, password);
            }
        };

        Session session = Session.getInstance(props, authenticator);






    }

}
