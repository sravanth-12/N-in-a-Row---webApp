package rooms;

import mypackage.GameDescriptor;

import java.util.ArrayList;
import java.util.List;

public class RoomInfo {
    private String roomName;
    private String ownersName;
    private int cols;
    private int rows;
    private int target;
    private List<Integer> winnersList = new ArrayList<Integer>();
    private boolean isDraw = false;
    private int totalPlayers;
    private int actualPlayers;
    private String gameType;
    private boolean active;

    public void setTotalPlayers(byte totalPlayers) {
        this.totalPlayers = totalPlayers;
    }

    public void setName(String roomName) {
        this.roomName = roomName;
    }

    public void setSize(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
    }

    public void setTarget(int target) {
        this.target = target;
    }

    public void setGameType(String gameType) {
        this.gameType = gameType;
    }

    public String getRoomName() {
        return roomName;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void initializeRoomInfo(GameDescriptor gameDescriptor) {
        setName(gameDescriptor.getDynamicPlayers().getGameTitle());
        setTotalPlayers(gameDescriptor.getDynamicPlayers().getTotalPlayers());
        setSize(gameDescriptor.getGame().getBoard().getRows(), gameDescriptor.getGame().getBoard().getColumns().intValue());
        setTarget(gameDescriptor.getGame().getTarget().intValue());
        setGameType(gameDescriptor.getGame().getVariant());
    }

    public void setOwnerName(String ownerName) {
        this.ownersName = ownerName;
    }

    public int getNumOfRows() {
        return rows;
    }

    public int getNumOfCols() {
        return cols;
    }

    public int getTarget() {
        return target;
    }

    public void setDraw(boolean draw) {
        this.isDraw = draw;
    }

    public void setWinnersList(List<Integer> winnersList) {
        this.winnersList = new ArrayList<>(winnersList);
    }

    public List<Integer> getWinnersList(){return this.winnersList;}

    public boolean isDraw() {
        return isDraw;
    }

    public int getActualPlayers() {
        return actualPlayers;
    }

    public void setActualPlayers(int actualPlayers) {
        this.actualPlayers = actualPlayers;
    }

    public boolean isRoomFull() {
        return actualPlayers == totalPlayers;
    }

    public String getGameType() {
        return gameType;
    }
}
