package game.utils;


import game.constants.Constants;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class SessionUtils {

    public static String getUsername(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        Object sessionAttribute = session != null ? session.getAttribute(Constants.USERNAME) : null;
        return sessionAttribute != null ? sessionAttribute.toString() : null;
    }

    public static String getPlayerLocation(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        Object sessionAttribute = session != null ? session.getAttribute(Constants.PLAYER_LOCATION) : null;
        return sessionAttribute != null ? sessionAttribute.toString() : null;
    }

    public static void clearSession(HttpServletRequest request) {
        request.getSession().invalidate();
    }

}