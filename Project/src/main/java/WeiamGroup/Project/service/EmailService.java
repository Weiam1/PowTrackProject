package WeiamGroup.Project.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender emailSender;

    public void sendVerificationEmail(String to, String subject, String text) throws MessagingException {
        System.out.println("Starting email preparation..."); // تسجيل بداية العملية

        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        try {
            System.out.println("Setting email recipient to: " + to); // تسجيل المستقبل
            helper.setTo(to);

            System.out.println("Setting email subject to: " + subject); // تسجيل الموضوع
            helper.setSubject(subject);

            System.out.println("Setting email content."); // تسجيل المحتوى
            helper.setText(text, true); // ضع `false` إذا كنت تريد رسالة نصية عادية

            System.out.println("Sending email..."); // تسجيل محاولة الإرسال
            emailSender.send(message);

            System.out.println("Email successfully sent to: " + to); // تأكيد نجاح الإرسال
        } catch (MessagingException e) {
            System.out.println("Error occurred while sending email: " + e.getMessage()); // تسجيل الخطأ
            throw e; // إعادة رفع الخطأ للتعامل معه في مكان آخر
        }
    }
}
