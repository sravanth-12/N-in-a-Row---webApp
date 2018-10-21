package game.servlets;

import com.sun.org.apache.bcel.internal.generic.NEW;
import game.constants.Constants;
import game.utils.ServletUtils;
import game.utils.SessionUtils;
import rooms.RoomsController;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.*;
import java.util.Scanner;

import static game.servlets.LoginServlet.LOBBY_URL;
import static game.servlets.LoginServlet.LOGIN_ERROR_URL;
import static rooms.RoomsController.SUCCESS;

@WebServlet("/new_room")
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024 * 5, maxRequestSize = 1024 * 1024 * 5 * 5)
public class NewRoomServlet extends HttpServlet {

    public static final String NEW_ROOM_MSG_URL = "/pages/lobby/new_room_msg.jsp";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect(LOBBY_URL);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String userName = SessionUtils.getUsername(request);
        if (userName == null) {
            request.setAttribute(Constants.USER_NAME_ERROR, "You must sign in first!");
            getServletContext().getRequestDispatcher(LOGIN_ERROR_URL).forward(request, response);
        } else {
            response.setContentType("text/html");
            String message = "Not a XML file";
            String ownerName;

            RoomsController roomsController = ServletUtils.getRoomsController(getServletContext());
            Part part = request.getPart("file");

            if (part.getContentType().equals("text/xml")) {
                //to write the content of the file to a string
                ownerName = SessionUtils.getUsername(request);
                message = roomsController.readXMLFile(part.getInputStream(), ownerName);
            }

            if (message.equals(SUCCESS)) {
                message += "!<br> Enjoy your game";
            }

            message.replace("\n", "<br>");
            request.setAttribute(Constants.NEW_ROOM_MSG, message);
            getServletContext().getRequestDispatcher(NEW_ROOM_MSG_URL).forward(request, response);
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>


    private String readFromInputStream(InputStream inputStream) {
        return new Scanner(inputStream).useDelimiter("\\Z").next();
    }


}
