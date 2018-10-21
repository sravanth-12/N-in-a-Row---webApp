package game.utils;

import game.constants.Constants;
import rooms.RoomsController;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import static game.constants.Constants.ROOMS_CONTROLLER_ATTRIBUTE_NAME;


public class ServletUtils {


    /*
    Note how the synchronization is done only on the question and\or creation of the relevant managers and once they exists -
    the actual fetch of them is remained unchronicled for performance POV
     */
    private static final Object roomsControllerLock = new Object();

    public static RoomsController getRoomsController(ServletContext servletContext) {

        synchronized (roomsControllerLock) {
            if (servletContext.getAttribute(ROOMS_CONTROLLER_ATTRIBUTE_NAME) == null) {
                servletContext.setAttribute(ROOMS_CONTROLLER_ATTRIBUTE_NAME, new RoomsController());
            }
        }
        return (RoomsController) servletContext.getAttribute(ROOMS_CONTROLLER_ATTRIBUTE_NAME);
    }


    public static int getIntParameter(HttpServletRequest request, String name) {
        String value = request.getParameter(name);
        if (value != null) {
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException numberFormatException) {
            }
        }
        return Constants.INT_PARAMETER_ERROR;
    }
}
