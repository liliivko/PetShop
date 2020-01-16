package utils;

import lombok.NoArgsConstructor;
import pisibg.ittalents.model.pojo.User;

import javax.servlet.http.HttpSession;

@NoArgsConstructor
public class SessionManager {

    public static final String USER__LOGGED = "user_logged";

    public static void logInUser(HttpSession session, User user) {
        session.setAttribute(USER__LOGGED, user);
    }

    public static boolean isLogged(HttpSession session) {
        return session.getAttribute(USER__LOGGED) != null;
    }
}


