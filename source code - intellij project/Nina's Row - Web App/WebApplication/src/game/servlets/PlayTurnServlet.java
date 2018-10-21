package game.servlets;

import com.google.gson.Gson;
import engine.Move;
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
import java.io.PrintWriter;

import static game.servlets.LoginServlet.LOGIN_ERROR_URL;
import static rooms.Room.NO_MESSAGE;

public class PlayTurnServlet extends HttpServlet {
    static int count = 0;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        String userName = SessionUtils.getUsername(request);

        if (userName == null) {
            request.setAttribute(Constants.USER_NAME_ERROR, "You must sign in first!");
            getServletContext().getRequestDispatcher(LOGIN_ERROR_URL).forward(request, response);
        }
        else {
            //get parameters and check validation
            String col = request.getParameter(Constants.CHOOSEN_COL);
            String moveType = request.getParameter(Constants.MOVE_TYPE);

            if (!isANumber(col) || moveType == null) {
                return;
            }

            //parse json to Move object
            Move move = new Move(col, moveType);
            //check move validation
            if (move.moveType == null || move.choosenCol == -1) {
                return;
            }

            //get room and check validation
            RoomsController roomsController = ServletUtils.getRoomsController(getServletContext());
            String roomName = SessionUtils.getPlayerLocation(request);
            Room room = roomsController.getRoomByName(roomName);
            if (room == null) {
                return;
            }

            //check if game over
            if (room.getRoomInfo().isActive() && !room.getLogic().isGameDone()) {
                Gson gson = new Gson();
                //play move
                if (SessionUtils.getUsername(request) == room.getLogic().getCurrentPlayerName()) {
                    room.playTurn(move);
                    count++;
                    System.out.println(SessionUtils.getUsername(request));
                    System.out.println("in play turn Servlet number: " + count);
                    System.out.println("num of players: " + room.getLogic().getPlayerCollection().getNumOfPlayers());
                    //when player do invalid move (full column or popout wrong disk)
                    String message = room.getEndTurnMessage();
//                    if (!message.equals(NO_MESSAGE)) {
                        try (PrintWriter out = response.getWriter()) {
                            out.println(gson.toJson(message));
                            out.flush();
//                        }
                    }
                } else {
                    String servletMessage = "It is " + room.getLogic().getCurrentPlayerName() + "'s turn. Please wait to your turn";
                    try (PrintWriter out = response.getWriter()) {
                        out.println(gson.toJson(servletMessage));
                        out.flush();
                    }
                }
            } else {
                return;
            }
        }
    }
        private boolean isANumber (String col){
            if (col == null)
                return false;
            if (col.isEmpty())
                return false;
            else {
                for (int i = 0; i < col.length(); i++) {
                    if (col.charAt(i) < '0' || col.charAt(i) > '9')
                        return false;
                }
                return true;
            }
        }

        @Override
        protected void doPost (HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            processRequest(req, resp);
        }

        @Override
        protected void doGet (HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            processRequest(req, resp);
        }


    }
