package se.yrgo.libraryapp.dao;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import java.sql.*;
import java.util.Optional;

import javax.sql.DataSource;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.pac4j.core.exception.AccountNotFoundException;

import se.yrgo.libraryapp.entities.User;
import se.yrgo.libraryapp.entities.UserId;

@MockitoSettings(strictness = Strictness.STRICT_STUBS)
public class UserDaoTest {
    @Mock
    private DataSource ds;
    @Mock
    private Connection conn;
    @Mock
    private Statement stmt;
    @Mock
    private ResultSet rs;

    @Test
    void testValidateWithCorrectUserAndPassword() throws SQLException {
        final UserId id = UserId.of(1);
        final String username = "testuser";
        final String password = "password";
        final String passwordHash = "$argon2i$v=19$m=16,t=2,p=1$QldXU09Sc2dzOWdUalBKQw$LgKb6x4usOpDLTlXCBVhxA";
        when(ds.getConnection()).thenReturn(conn);
        when(conn.createStatement()).thenReturn(stmt);
        when(stmt.executeQuery(contains(username))).thenReturn(rs);
        when(rs.next()).thenReturn(true, false);
        when(rs.getInt("id")).thenReturn(id.getId());
        when(rs.getString("password_hash")).thenReturn(passwordHash);
        UserDao userDao = new UserDao(ds);
        assertThat(userDao.validate(username, password)).isEqualTo(id);
    }

    @Test
    void testValidateWithWrongPassword() throws SQLException {
        final UserId id = UserId.of(1);
        final String username = "testuser";
        final String wrongPassword = "wrongpassword";
        final String passwordHash = "$argon2i$v=19$m=16,t=2,p=1$QldXU09Sc2dzOWdUalBKQw$LgKb6x4usOpDLTlXCBVhxA";
        when(ds.getConnection()).thenReturn(conn);
        when(conn.createStatement()).thenReturn(stmt);
        when(stmt.executeQuery(contains(username))).thenReturn(rs);
        when(rs.next()).thenReturn(true, false);
        when(rs.getInt("id")).thenReturn(id.getId());
        when(rs.getString("password_hash")).thenReturn(passwordHash);
        UserDao userDao = new UserDao(ds);
        assertThatExceptionOfType(AccountNotFoundException.class)
                .isThrownBy(() -> userDao.validate(username, wrongPassword));
    }

    @Test
    void getExistingUser() throws SQLException {
        final UserId id = UserId.of(1);
        final String username = "testuser";
        final String realname = "bosse";
        final String passwordHash = "xxx";
        final User expectedUser = new User(id, username, realname, passwordHash);
        // Mock DataSource, Connection, PreparedStatement, and ResultSet
        DataSource ds = mock(DataSource.class);
        Connection conn = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);

        when(ds.getConnection()).thenReturn(conn);
        when(conn.prepareStatement(anyString())).thenReturn(ps); // Mock PreparedStatement
        when(ps.executeQuery()).thenReturn(rs); // Mock ResultSet
        when(rs.next()).thenReturn(true, false);
        when(rs.getInt("id")).thenReturn(id.getId());
        when(rs.getString("realname")).thenReturn(realname);
        when(rs.getString("password_hash")).thenReturn(passwordHash);

        UserDao userDao = new UserDao(ds);
        assertThat(userDao.get(username)).isEqualTo(Optional.of(expectedUser));
    }

    @Test
    void getNonExistingUser() throws SQLException {
        final String username = "testuser";
        // Mock DataSource, Connection, PreparedStatement, and ResultSet
        DataSource ds = mock(DataSource.class);
        Connection conn = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);

        when(ds.getConnection()).thenReturn(conn);
        when(conn.prepareStatement(anyString())).thenReturn(ps);
        when(ps.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(false);

        UserDao userDao = new UserDao(ds);
        assertThat(userDao.get(username)).isEmpty();
    }

}