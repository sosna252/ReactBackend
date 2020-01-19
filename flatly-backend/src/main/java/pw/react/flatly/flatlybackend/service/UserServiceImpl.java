package pw.react.flatly.flatlybackend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pw.react.flatly.flatlybackend.exception.UserNotFoundException;
import pw.react.flatly.flatlybackend.model.User;
import pw.react.flatly.flatlybackend.repository.UserRepository;

import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
    UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UUID login(String login, String password) {
        User user = userRepository.findByLogin(login).orElse(null);

        if(user==null || !user.getPassword().equals(password)) {
            throw new UserNotFoundException("Login or password is incorrect");
        }

        return user.getSecurity_token();
    }
}
