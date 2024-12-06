package WeiamGroup.Project.responses;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.stereotype.Service;

@Getter
@Service
public class LoginResponse {
    private final String token;
    private final long expiresIn;

    // منشئ بدون معلمات (لـ Spring)
    public LoginResponse() {
        this.token = "defaultToken";
        this.expiresIn = 3600;
    }

    // منشئ مع المعلمات (لإنشاء الكائن ديناميكيًا)
    public LoginResponse(String token, long expiresIn) {
        this.token = token;
        this.expiresIn = expiresIn;
    }

    @PostConstruct
    public void printConfig() {
        System.out.println("DB URL: " + System.getenv("SPRING_DATASOURCE_URL"));
        System.out.println("DB User: " + System.getenv("SPRING_DATASOURCE_USERNAME"));
        System.out.println("DB Password: " + System.getenv("SPRING_DATASOURCE_PASSWORD"));
    }
}
