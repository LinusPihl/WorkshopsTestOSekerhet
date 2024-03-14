package se.yrgo.libraryapp.controllers;

import java.util.List;
import java.util.UUID;
import org.pac4j.core.exception.CredentialsException;
import com.google.inject.Inject;
import io.jooby.Context;
import io.jooby.Cookie;
import io.jooby.SameSite;
import io.jooby.annotations.CookieParam;
import io.jooby.annotations.GET;
import io.jooby.annotations.POST;
import io.jooby.annotations.Path;
import se.yrgo.libraryapp.dao.RoleDao;
import se.yrgo.libraryapp.dao.SessionDao;
import se.yrgo.libraryapp.dao.UserDao;
import se.yrgo.libraryapp.entities.Role;
import se.yrgo.libraryapp.entities.UserId;
import se.yrgo.libraryapp.entities.forms.LoginData;

@Path("/login")
public class LoginController {

    private RoleDao roleDao;
    private UserDao userDao;
    private SessionDao sessionDao;

    @Inject
    LoginController(RoleDao roleDao, UserDao userDao, SessionDao sessionDao) {
        this.roleDao = roleDao;
        this.userDao = userDao;
        this.sessionDao = sessionDao;
    }

    @POST
    public List<Role> login(Context context, @CookieParam("session") String sessionCookie, LoginData login) {
        
        if (isInvalidSession(sessionCookie)) {
            UserId userId = userDao.validate(login.getUsername(), login.getPassword());
            UUID sessionId = sessionDao.create(userId);
            Cookie cookie = createSessionCookie(sessionId);
            context.setResponseCookie(cookie);

            return roleDao.get(userId);
        }
        
        return List.of();
    }

    @GET
    @Path("/check")
    public List<Role> isLoggedIn(@CookieParam("session") String sessionCookie) {
        try {
            UserId userId = getUserForSession(sessionCookie);
            return roleDao.get(userId);
        }
        catch (IllegalArgumentException | CredentialsException ex) {
            return List.of();
        }
    }

    private boolean isInvalidSession(String sessionCookie) {
        // currently does not care if you try to login with other account

        try {
            getUserForSession(sessionCookie);
            return false;
        }
        catch (IllegalArgumentException | CredentialsException ex) {
            return true;
        }
    }

    private UserId getUserForSession(String sessionCookie) {
        if (sessionCookie == null) {
            throw new IllegalArgumentException("Session cookie is null");
        }

        return sessionDao.validate(UUID.fromString(sessionCookie));
    }

    private Cookie createSessionCookie(UUID session) {
        Cookie cookie = new Cookie("session", session.toString());
        cookie.setSameSite(SameSite.LAX);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(-1);
        return cookie;
    }
}
