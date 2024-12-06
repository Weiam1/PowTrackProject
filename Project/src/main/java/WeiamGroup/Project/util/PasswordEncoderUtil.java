package WeiamGroup.Project.util;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncoderUtil {




        public static void main(String[] args) {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            String rawPassword = "password123"; // كلمة المرور الأصلية
            String hashedPassword = encoder.encode(rawPassword);
            System.out.println("Hashed Password: " + hashedPassword);
        }
    }


