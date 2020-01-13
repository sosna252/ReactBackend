package pw.react.flatly.flatlybackend.service;

import java.util.UUID;

public interface UserService {
    UUID login(String login, String password);
}
