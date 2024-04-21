package se.yrgo.libraryapp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import javax.inject.Inject;
import javax.sql.DataSource;
import org.pac4j.core.exception.AccountNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import se.yrgo.libraryapp.entities.User;
import se.yrgo.libraryapp.entities.UserId;

public class UserDao {
    private static Logger logger = LoggerFactory.getLogger(UserDao.class);
    private DataSource ds;

    @Inject
    UserDao(DataSource ds) {
        this.ds = ds;
    }

    public UserId validate(String user, String password) {
        try (Connection conn = ds.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(
                     "SELECT id, password_hash FROM user WHERE user = '" + user + "'")) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String passwordHash = rs.getString("password_hash");
                Argon2PasswordEncoder encoder = new Argon2PasswordEncoder();
                if (encoder.matches(password, passwordHash)) {
                    return UserId.of(id);
                }
            }
        } catch (SQLException ex) {
            throw new AccountNotFoundException(ex);
        }

        throw new AccountNotFoundException("Unable to find user " + user);
    }

    public Optional<User> get(UserId id) {
        String query = "SELECT user, realname FROM user WHERE id = ?";
        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, id.getId());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String name = rs.getString("user");
                    String realname = rs.getString("realname");
                    return Optional.of(new User(id, name, realname));
                }
            }
        } catch (SQLException ex) {
            logger.error("Unable to fetch user " + id, ex);
        }
        return Optional.empty();
    }

    public boolean register(String name, String realname, String password) {
        Argon2PasswordEncoder encoder = new Argon2PasswordEncoder();
        String passwordHash = encoder.encode(password);

        // handle names like Ian O'Toole
        realname = realname.replace("'", "\\'");

        try (Connection conn = ds.getConnection()) {
            conn.setAutoCommit(false);

            return insertUserAndRole(name, realname, passwordHash, conn);
        } catch (SQLException ex) {
            logger.error("Unable to register user " + name, ex);
            return false;
        }
    }

    public boolean isNameAvailable(String name) {
        if (name == null || name.trim().length() < 3) {
            return false;
        }

        String query = "SELECT id FROM user WHERE user = ?";
        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, name);
            try (ResultSet rs = ps.executeQuery()) {
                return !rs.next();
            }
        } catch (SQLException ex) {
            logger.error("Unable to lookup user name " + name, ex);
            return false;
        }
    }

    public Optional<User> get(String user) {
        String query = "SELECT id, realname, password_hash FROM user WHERE user = ?";
        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, user);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("id");
                    UserId userId = UserId.of(id);
                    String realname = rs.getString("realname");
                    String passwordHash = rs.getString("password_hash");
                    return Optional.of(new User(userId, user, realname, passwordHash));
                }
            }
        } catch (SQLException ex) {
            logger.error("Unable to get user " + user, ex);
        }
        return Optional.empty();
    }

    private boolean insertUserAndRole(String name, String realname, String passwordHash,
                                      Connection conn) throws SQLException {
        String insertUser = "INSERT INTO user (user, realname, password_hash) VALUES (?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(insertUser, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, name);
            stmt.setString(2, realname);
            stmt.setString(3, passwordHash);
            stmt.executeUpdate();
            UserId userId = getGeneratedUserId(stmt);

            if (userId.getId() > 0 && addToUserRole(conn, userId)) {
                conn.commit();
                return true;
            } else {
                conn.rollback();
                return false;
            }
        } catch (SQLException ex) {
            conn.rollback();
            logger.error("Unable to register user " + name, ex);
            return false;
        } finally {
            conn.setAutoCommit(true);
        }
    }

    private UserId getGeneratedUserId(Statement stmt) throws SQLException {
        try (ResultSet rs = stmt.getGeneratedKeys()) {
            rs.next();
            return UserId.of(rs.getInt(1));
        }
    }

    private boolean addToUserRole(Connection conn, UserId user) throws SQLException {
        String insertRole = "INSERT INTO user_role (user_id, role_id) VALUES (?, 2)";

        try (PreparedStatement stmt = conn.prepareStatement(insertRole)) {
            stmt.setInt(1, user.getId());
            return stmt.executeUpdate() == 1;
        }
    }
}
