package WeiamGroup.Project.service;

import WeiamGroup.Project.dto.LoginUserDto;
import WeiamGroup.Project.dto.RegisterUserDto;
import WeiamGroup.Project.dto.VerifyUserDto;
import WeiamGroup.Project.model.User;
import WeiamGroup.Project.repository.UserRepository;
import jakarta.mail.MessagingException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;

    public AuthenticationService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            EmailService emailService
    ) {
        this.authenticationManager = authenticationManager;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    public User signup(RegisterUserDto input) {
        System.out.println("Starting signup process for: " + input.getEmail());

        try {
            User user = new User(input.getUsername(), input.getEmail(), passwordEncoder.encode(input.getPassword()));
            user.setVerificationCode(generateVerificationCode());
            user.setVerificationCodeExpiresAt(LocalDateTime.now().plusMinutes(15));
            user.setEnabled(false);

            sendVerificationEmail(user);
            System.out.println("Verification email sent successfully to: " + user.getEmail());

            return userRepository.save(user);
        } catch (Exception e) {
            System.err.println("Error during signup for email: " + input.getEmail());
            e.printStackTrace();
            throw new RuntimeException("Signup failed. Please try again.", e);
        }
    }

    public User authenticate(LoginUserDto input) {
        System.out.println("Attempting to authenticate user with email: " + input.getEmail());

        try {
            User user = userRepository.findByEmail(input.getEmail())
                    .orElseThrow(() -> {
                        System.err.println("User not found with email: " + input.getEmail());
                        return new RuntimeException("User not found");
                    });

            if (!user.isEnabled()) {
                System.err.println("Account not verified for email: " + input.getEmail());
                throw new RuntimeException("Account not verified. Please verify!");
            }

            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            input.getEmail(),
                            input.getPassword()
                    )
            );

            System.out.println("Authentication successful for email: " + input.getEmail());
            return user;
        } catch (Exception e) {
            System.err.println("Authentication failed for email: " + input.getEmail());
            e.printStackTrace();
            throw new RuntimeException("Authentication failed. Please check your credentials.", e);
        }
    }

    public void verifyUser(VerifyUserDto input) {
        System.out.println("Starting verification process for email: " + input.getEmail());

        Optional<User> optionalUser = userRepository.findByEmail(input.getEmail());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (user.getVerificationCodeExpiresAt().isBefore(LocalDateTime.now())) {
                throw new RuntimeException("Verification code has expired");
            }
            if (user.getVerificationCode().equals(input.getVerificationCode())) {
                user.setEnabled(true);
                user.setVerificationCode(null);
                user.setVerificationCodeExpiresAt(null);
                userRepository.save(user);
                System.out.println("User verified successfully: " + input.getEmail());
            } else {
                throw new RuntimeException("Invalid verification code");
            }
        } else {
            System.err.println("User not found during verification process: " + input.getEmail());
            throw new RuntimeException("User not found");
        }
    }

    public void resendVerificationCode(String email) {
        System.out.println("Attempting to resend verification code to email: " + email);

        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (user.isEnabled()) {
                throw new RuntimeException("Account is already verified");
            }
            user.setVerificationCode(generateVerificationCode());
            user.setVerificationCodeExpiresAt(LocalDateTime.now().plusHours(1));
            sendVerificationEmail(user);
            userRepository.save(user);
            System.out.println("Verification code resent to: " + email);
        } else {
            System.err.println("User not found during resend process: " + email);
            throw new RuntimeException("User not found");
        }
    }

    public void sendVerificationEmail(User user) {
        String subject = "Account Verification";
        String verificationCode = user.getVerificationCode();
        String htmlMessage = "<html>"
                + "<body style=\"font-family: Arial, sans-serif;\">"
                + "<div style=\"background-color: #f5f5f5; padding: 20px;\">"
                + "<h2 style=\"color: #333;\">Welcome to our app!</h2>"
                + "<p style=\"font-size: 16px;\">Please enter the verification code below to continue:</p>"
                + "<div style=\"background-color: #fff; padding: 20px; border-radius: 5px; box-shadow: 0 0 10px rgba(0,0,0,0.1);\">"
                + "<h3 style=\"color: #333;\">Verification Code:</h3>"
                + "<p style=\"font-size: 18px; font-weight: bold; color: #007bff;\">" + verificationCode + "</p>"
                + "</div>"
                + "</body>"
                + "</html>";

        try {
            System.out.println("Sending verification email to: " + user.getEmail());
            emailService.sendVerificationEmail(user.getEmail(), subject, htmlMessage);
            System.out.println("Email sent successfully to: " + user.getEmail());
        } catch (MessagingException e) {
            System.err.println("Error sending verification email to: " + user.getEmail());
            e.printStackTrace();
            throw new RuntimeException("Failed to send verification email", e);
        }
    }

    private String generateVerificationCode() {
        Random random = new Random();
        int code = random.nextInt(900000) + 100000;
        return String.valueOf(code);
    }
}
