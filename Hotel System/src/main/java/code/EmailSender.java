package main.java.code;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

public class EmailSender {

    // Your Gmail address
    private static final String FROM_EMAIL = "jwmarriottasu@gmail.com";

    // Your 16-character Gmail App Password WITHOUT spaces
    private static final String PASSWORD = "jzrcxbdbyzceynjo";

    public static void sendCode(String toEmail, String code) throws MessagingException {

        // Gmail SMTP settings
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true"); // TLS
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");

        // Create email session with authentication
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(FROM_EMAIL, PASSWORD);
            }
        });

        // Create the message
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(FROM_EMAIL));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
        message.setSubject("Password Reset Verification Code");

        // Email body text
        String emailBody =
                "Hello,\n\n" +
                "You requested to reset your password.\n" +
                "Use the verification code below:\n\n" +
                "Verification Code: " + code + "\n\n" +
                "If you did not request this, please ignore the email.\n\n" +
                "Regards,\n" +
                "JW Marriott Hotel Management System Support";

        message.setText(emailBody);

        // Send the email
        Transport.send(message);

        System.out.println("Email sent successfully to " + toEmail);
    }

    // For testing
    /*
    public static void main(String[] args) {
        try {
            sendCode("ur_email", "123456");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
    */
}
