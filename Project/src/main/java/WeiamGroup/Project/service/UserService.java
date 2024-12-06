package WeiamGroup.Project.service;

import WeiamGroup.Project.model.User;
import WeiamGroup.Project.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository,EmailService emailService) {
        this.userRepository = userRepository;
    }

    public List<User> allUsers(){
        List<User> user= new ArrayList<>();
        userRepository.findAll().forEach(user::add);
        return user;
    }
}
