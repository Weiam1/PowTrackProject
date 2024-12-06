package WeiamGroup.Project.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api") // Base path to avoid conflicts
public class DefaultController {

    @GetMapping("/")
    public String home() {
        return "Welcome to the application!";
    }

    @GetMapping("/hello")
    public String sayHello() {
        return "Hello, World!";
    }
}
