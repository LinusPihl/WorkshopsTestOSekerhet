package se.yrgo.libraryapp;

import java.util.Optional;
import javax.inject.Inject;

import org.pac4j.core.credentials.password.PasswordEncoder;
import se.yrgo.libraryapp.dao.UserDao;
import se.yrgo.libraryapp.entities.User;
import se.yrgo.libraryapp.entities.UserId;

public class UserService {
    private UserDao userDao;
    private final PasswordEncoder encoder;
    
    @Inject
    public UserService(UserDao userDao, PasswordEncoder encoder) {
    this.userDao = userDao;
    this.encoder = encoder;
    }

    public Optional<UserId> validate(String username, String password) {
        Optional<User> maybeUser = userDao.get(username);
        if (maybeUser.isEmpty()) {
            return Optional.empty();
        }
        User user = maybeUser.get();
        if (!encoder.matches(password, user.getPasswordHash())) {
            return Optional.empty();
        }
        return Optional.of(user.getId());
    }
}