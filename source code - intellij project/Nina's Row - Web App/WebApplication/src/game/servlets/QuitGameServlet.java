package game.servlets;

import game.constants.Constants;
import game.utils.ServletUtils;
import game.utils.SessionUtils;
import rooms.Room;
import rooms.RoomsController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static game.constants.Constants.LOBBY;
import static game.servlets.LoginServlet.LOGIN_ERROR_URL;

public class QuitGameServlet extends HttpServlet {
    private final String LOBBY_URL = "pages/lobby/lobby.html";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = SessionUtils.getUsername(request);
        String currnetRoom = SessionUtils.getPlayerLocation(request);


        if (username == null) {
            request.setAttribute(Constants.USER_NAME_ERROR, "You must sign in first!");
            getServletContext().getRequestDispatcher(LOGIN_ERROR_URL).forward(request, response);
        } else if(currnetRoom.equals(LOBBY)) {
            response.sendRedirect(LOBBY_URL);
        }else{
            if (currnetRoom != null && currnetRoom != LOBBY) {
                RoomsController roomsController = ServletUtils.getRoomsController(getServletContext());
                Room room = roomsController.getRoomByName(currnetRoom);

                if (room != null) {
                    //current room is aviable
                    room.quitPlayer(username);
                    request.getSession(true).setAttribute(Constants.PLAYER_LOCATION, roomsController.getPlayer(username).getLocation());
                    response.sendRedirect(LOBBY_URL);
                }
            }
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }
}
