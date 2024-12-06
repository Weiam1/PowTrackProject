package WeiamGroup.Project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/")
    public String homePage() {
        return "index"; // Assumes `index.html` is in `src/main/resources/templates`
    }

    @GetMapping("/signup")
    public String signupPage() {
        return "signup"; // Assumes `signup.html` is in `src/main/resources/templates`
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login"; // Assumes `login.html` is in `src/main/resources/templates`
    }
}
