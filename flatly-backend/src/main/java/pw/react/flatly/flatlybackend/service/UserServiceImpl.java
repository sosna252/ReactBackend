package pw.react.flatly.flatlybackend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pw.react.flatly.flatlybackend.exception.UnauthorizedException;
import pw.react.flatly.flatlybackend.model.User;
import pw.react.flatly.flatlybackend.repository.UserRepository;

import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UUID login(String login, String password) {
        User user = userRepository.findByLogin(login).orElseThrow(() -> new UnauthorizedException("Login or password is incorrect"));

        if(!user.getPassword().equals(password)) {
            throw new UnauthorizedException("Login or password is incorrect");
        }

        return user.getSecurityToken();
    }
}
