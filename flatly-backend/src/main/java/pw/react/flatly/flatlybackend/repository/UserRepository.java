package pw.react.flatly.flatlybackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pw.react.flatly.flatlybackend.model.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByLogin(String login);
}
