package WeiamGroup.Project.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.mail.MessagingException;
import WeiamGroup.Project.service.EmailService;

@RestController
@RequestMapping("/email")
public class EmailController {

    private final EmailService emailService;

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @GetMapping("/test")
    public ResponseEntity<String> sendTestEmail() {
        try {
            emailService.sendVerificationEmail(
                    "recipient@example.com",
                    "Test Email",
                    "<h1>This is a test email</h1>"
            );
            return ResponseEntity.ok("Email sent successfully!");
        } catch (MessagingException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Failed to send email.");
        }
    }
}
