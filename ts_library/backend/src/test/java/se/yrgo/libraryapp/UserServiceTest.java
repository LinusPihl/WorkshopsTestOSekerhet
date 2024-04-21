package se.yrgo.libraryapp;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.pac4j.core.credentials.password.PasswordEncoder;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;

import se.yrgo.libraryapp.dao.UserDao;
import se.yrgo.libraryapp.entities.User;
import se.yrgo.libraryapp.entities.UserId;

@MockitoSettings(strictness = Strictness.STRICT_STUBS)
public class UserServiceTest {
    @Mock
    private UserDao userDao;

    @Mock
    private PasswordEncoder encoder;

    @Test
    void correctLogin() {
        final UserId id = UserId.of(1);
        final String username = "testuser";
        final String realname = "bosse";
        final String password = "password";
        final String passwordHash = password;
        final User user = new User(id, username, realname, passwordHash);

        // Mock the behavior of encoder
        when(encoder.matches(password, passwordHash)).thenReturn(true);

        when(userDao.get(username)).thenReturn(Optional.of(user));
        UserService userService = new UserService(userDao, encoder);
        assertThat(userService.validate(username, password)).isEqualTo(Optional.of(id));
    }

}