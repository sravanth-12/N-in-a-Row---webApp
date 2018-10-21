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

public class ResetRoomServlet extends HttpServlet {
    private void processRequest(HttpServletRequest request, HttpServletResponse respone) {
        RoomsController roomsController = ServletUtils.getRoomsController(getServletContext());
        String roomName = SessionUtils.getPlayerLocation(request);
        Room room = roomsController.getRoomByName(roomName);
        String username = SessionUtils.getUsername(request);

        if (room == null || username == null) {
            return;
        }
        room.removePlayerInEndOfMatch(username);
        request.getSession(true).setAttribute(Constants.PLAYER_LOCATION, roomsController.getPlayer(username).getLocation());
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

}
