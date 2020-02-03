package pw.react.flatly.flatlybackend.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import pw.react.flatly.flatlybackend.exception.UnauthorizedException;
import pw.react.flatly.flatlybackend.model.User;
import pw.react.flatly.flatlybackend.repository.UserRepository;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Spy
    @InjectMocks
    private UserServiceImpl userService;

    @Test
    public void givenNotExistingUser_whenLogin_thenThrowUnauthorizedException() {

        Mockito.when(userRepository.findByLogin(Mockito.anyString())).thenReturn(Optional.empty());

        try {
            UUID uuid = userService.login("AA", "BB");
            fail("Should throw UnauthorizedException");
        } catch (UnauthorizedException ex) {
            assertEquals(ex.getMessage(), "Login or password is incorrect");
        }
    }

    @Test
    public void givenExistingUser_whenLoginWithWrongPassword_thenThrowUnauthorizedException() {

        User user = new User();
        user.setLogin("AAA");
        user.setPassword("BBB");

        Mockito.when(userRepository.findByLogin(Mockito.anyString())).thenReturn(Optional.of(user));

        try {
            UUID uuid = userService.login("AAA", "AAA");
            fail("Should throw UnauthorizedException");
        } catch (UnauthorizedException ex) {
            assertEquals(ex.getMessage(), "Login or password is incorrect");
        }
    }

    @Test
    public void givenExistingUser_whenLoginWithCorrectPassword_thenReturnUUID() {

        UUID uuid = UUID.randomUUID();

        User user = new User();
        user.setLogin("AAA");
        user.setPassword("BBB");
        user.setSecurityToken(uuid);

        Mockito.when(userRepository.findByLogin(Mockito.anyString())).thenReturn(Optional.of(user));

        UUID uuid2 = userService.login("AAA", "BBB");
        assertEquals(uuid, uuid2);
    }

}