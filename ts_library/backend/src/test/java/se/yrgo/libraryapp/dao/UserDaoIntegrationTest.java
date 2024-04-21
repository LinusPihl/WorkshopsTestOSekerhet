package se.yrgo.libraryapp.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import javax.sql.DataSource;

import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import com.radcortez.flyway.test.annotation.H2;
import se.yrgo.libraryapp.entities.User;
import se.yrgo.libraryapp.entities.UserId;

@Tag("integration")
@H2
public class UserDaoIntegrationTest {

    private static DataSource ds;

    @BeforeAll
    static void initDataSource() {
        // Set up H2 DataSource
        final JdbcDataSource ds = new JdbcDataSource();
        ds.setURL("jdbc:h2:mem:test");
        UserDaoIntegrationTest.ds = ds;
    }

    @Test
    void getUserByName() {
        // Given
        final String username = "test";
        final UserId userId = UserId.of(1);

        // When
        UserDao userDao = new UserDao(ds);
        Optional<User> maybeUser = userDao.get(username);

        // Then
        assertThat(maybeUser).isPresent();
        assertThat(maybeUser.get().getId()).isEqualTo(userId);
    }
    @Test
    void registerUser() {
        // Given
        final String username = "newuser";
        final String realname = "New User";
        final String password = "newpassword";

        // When
        UserDao userDao = new UserDao(ds);
        boolean registered = userDao.register(username, realname, password);

        // Then
        assertThat(registered).isTrue();

        // Additional assertions to check if user is successfully registered
        Optional<User> maybeUser = userDao.get(username);
        assertThat(maybeUser).isPresent();
        assertThat(maybeUser.get().getName()).isEqualTo(username);
        assertThat(maybeUser.get().getRealname()).isEqualTo(realname);
    }
}
