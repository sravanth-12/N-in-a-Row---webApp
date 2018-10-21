package game.servlets;

import game.constants.Constants;
import game.utils.ServletUtils;
import game.utils.SessionUtils;
import rooms.RoomsController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static game.constants.Constants.LOBBY;
import static game.constants.Constants.USERNAME;
import static game.constants.Constants.USER_TYPE;

public class LoginServlet extends HttpServlet {
    public static final String LOGIN_ERROR_URL = "/pages/signup/login_error.jsp";
    public static final String LOBBY_URL = "../lobby/lobby.html";
    public static final String DIRECT_LOBBY_URL = "pages/lobby/lobby.html";
    private final String SIGN_UP_URL = "../signup/singup-error.html";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String usernameFromSession = SessionUtils.getUsername(request);
        RoomsController roomsController = ServletUtils.getRoomsController(getServletContext());

        if (usernameFromSession == null) {
            //user is not logged in yet
            String usernameFromParameter = request.getParameter(USERNAME);
            if (usernameFromParameter == null) {
                //no username in session and no username in parameter -
                //redirect back to the index page
                //this return an HTTP code back to the browser telling it to load
                response.sendRedirect(SIGN_UP_URL);
            } else {
                //normalize the username value
                usernameFromParameter = usernameFromParameter.trim();

                /*
                One can ask why not enclose all the synchronizations inside the userManager object ?
                Well, the atomic question we need to perform here includes both the question (isUserExists) and (potentially) the insertion
                of the new user (addUser). These two actions needs to be considered atomic, and synchronizing only each one of them solely is not enough.
                (off course there are other more sophisticated and performable means for that (atomic objects etc) but these are not in our scope)

                The synchronized is on this instance (the servlet).
                As the servlet is singleton - it is promised that all threads will be synchronized on the same instance (crucial here)

                A better code would be to perform only as little and as nessessary things we need here inside the synchronized block and avoid
                do here other not related actions (such as request dispatcher\redirection etc. this is shown here in that manner just to stress this issue
                 */
                synchronized (this) {
                    if (roomsController.isUserExists(usernameFromParameter)) {
                        String errorMessage = "Username " + usernameFromParameter + " already exists. Please enter a different username.";
                        // username already exists, forward the request back to index
                        // with a parameter that indicates that an error should be displayed
                        // the request dispatcher obtained from the servlet context is one that MUST get an absolute path (starting with'/')
                        // and is relative to the web app root
                        // see this link for more details:
                        // http://timjansen.github.io/jarfiller/guide/servlet25/requestdispatcher.xhtml
                        request.setAttribute(Constants.USER_NAME_ERROR, errorMessage);
                        getServletContext().getRequestDispatcher(LOGIN_ERROR_URL).forward(request, response);
                    } else if (!isUsernameValid(usernameFromParameter)) {
                        String errorMessage = "Username \"" + usernameFromParameter + "\" Invalid. Please enter a username without spaces.";
                        request.setAttribute(Constants.USER_NAME_ERROR, errorMessage);
                        getServletContext().getRequestDispatcher(LOGIN_ERROR_URL).forward(request, response);
                    } else {
                        //add the new user to the users list
                        String type = request.getParameter(USER_TYPE);
                        roomsController.addUser(usernameFromParameter, type);
                        //set the username in a session so it will be available on each request
                        //the true parameter means that if a session object does not exists yet
                        //create a new one
                        request.getSession(true).setAttribute(Constants.USERNAME, usernameFromParameter);
                        request.getSession(true).setAttribute(Constants.PLAYER_LOCATION, LOBBY);
                        response.sendRedirect(LOBBY_URL);
                    }
                }
            }
        } else {
            //user is already logged in
            if(SessionUtils.getPlayerLocation(request).equals(LOBBY)){
                response.sendRedirect(DIRECT_LOBBY_URL);
            }
            else{
                request.getRequestDispatcher("/enter_room").forward(request,response);
            }
        }
    }

    private boolean isUsernameValid(String usernameFromParameter) {
        return !usernameFromParameter.contains(" ") && !usernameFromParameter.isEmpty();
    }
}
