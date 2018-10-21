package rooms;

import mypackage.GameDescriptor;
import players.Computer;
import players.Human;
import players.Player;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Integer.min;

public class RoomsController {
    public static final String LOBBY = "@lobby@!";
    public static final String SUCCESS = "Loading succeeded";
    private final int MAX_NUM_OF_PLAYERS = 6;
    private final int MIN_NUM_OF_PLAYERS = 2;
    private final int MIN_TARGET = 2;
    private final int MAX_COLUMNS = 30;
    private final int MIN_COLUMNS = 6;
    private final int MAX_ROWS = 50;
    private final int MIN_ROWS = 5;

    private Map<String, Room> roomsCollection;
    private Map<String, Player> users;

    public RoomsController() {
        roomsCollection = new HashMap<String, Room>();
        users = new HashMap<String, Player>();
    }

    public String readXMLFile(InputStream is, String ownerName) {
        String message;
        GameDescriptor gameDescriptor;

        try {
            //get GameDescriptor
            //File file = new File(path);
            JAXBContext jaxbContext = JAXBContext.newInstance(GameDescriptor.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            gameDescriptor = (GameDescriptor) jaxbUnmarshaller.unmarshal(is);

            //check validation
            message = checkXMLValidation(gameDescriptor);

            if (message.equals(SUCCESS))
                openNewRoom(gameDescriptor,ownerName);

        } catch (JAXBException e) {
            message = "File not found!";
        }

        return message;
    }

    private String checkXMLValidation(GameDescriptor gameDescriptor) {
        int columns = gameDescriptor.getGame().getBoard().getColumns().intValue();
        int rows = gameDescriptor.getGame().getBoard().getRows();
        int target = gameDescriptor.getGame().getTarget().intValue();
        int totalPlayers = gameDescriptor.getDynamicPlayers().getTotalPlayers();
        String errorMsg = "Error: illegal XML file details\n";

        if (columns < MIN_COLUMNS || columns > MAX_COLUMNS)
            return errorMsg + "The number of columns is out of range!";
        else if (rows < MIN_ROWS || rows > MAX_ROWS)
            return errorMsg + "The number of rows is out of range!";
        else if (target >= min(columns, rows))
            return errorMsg + "The target length is longer than the board!";
        else if (target < MIN_TARGET)
            return errorMsg + "The target length is too short";
        else if (totalPlayers < MIN_NUM_OF_PLAYERS) {
            return errorMsg + "The total number of players is less then 2";
        } else if (totalPlayers > MAX_NUM_OF_PLAYERS) {
            return errorMsg + "The total number of players is above 6";
        }
        else if(isRoomNameExist(gameDescriptor.getDynamicPlayers().getGameTitle())){
            return errorMsg + "The room name already exist";
        }
        return SUCCESS;
    }

    private boolean isRoomNameExist(String newRoomName) {
        return roomsCollection.containsKey(newRoomName);
    }

    private void openNewRoom(GameDescriptor gameDescriptor, String ownerName) {
        Room newRoom = new Room(gameDescriptor);
        newRoom.setOwner(ownerName);
        roomsCollection.put(newRoom.getName(), newRoom);
    }

    public void setRoomOwner(String roomName, String ownerName) {
        roomsCollection.get(roomName).setOwner(ownerName);
    }

    public boolean isUserExists(String username) {
        return users.containsKey(username);
    }

    public void addUser(String username, String type) {
        users.put(username, newPlayer(username, type));
    }

    private Player newPlayer(String username, String type) {
        Player newPlayer;

        if (type.equals("Computer"))
            newPlayer = new Computer(username);
        else
            newPlayer = new Human(username);

        newPlayer.setLocation(LOBBY);
        return newPlayer;
    }

    public void removeUser(String usernameFromSession) {
        Player userLogout = users.get(usernameFromSession);

        if (userLogout != null) {
//            if (userLogout.getLocation() != LOBBY){
//                roomsCollection.get(userLogout.getLocation()).quitPlayer(usernameFromSession);
//            }
            users.remove(usernameFromSession);
        }
    }

    public List<RoomInfo> getRoomsList() {
        List<RoomInfo> roomsInfo = new ArrayList<RoomInfo>();

        for (Room room : roomsCollection.values()) {
            roomsInfo.add(room.getRoomInfo());
        }

        return roomsInfo;
    }

    public Room getRoomByName(String playerLocation) {
        return roomsCollection.get(playerLocation);
    }

    public List<String> getUsers() {
        List<String> usersList = new ArrayList<String>();

        for (String userName : users.keySet()) {
            usersList.add(userName);
        }

        return usersList;
    }

    public Player getPlayer(String username) {
        return users.get(username);
    }
}
