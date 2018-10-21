package game;

import java.io.Serializable;

public class BoardCell implements Serializable {
    public static final int EMPTY_CELL = -1;

    private int playerNum = EMPTY_CELL;

    public int getPlayerNum() {
        return playerNum;
    }

    public void setPlayerNum(int playerNum) {
        this.playerNum = playerNum;
    }

    public boolean isEmpty() {
        return playerNum == EMPTY_CELL;
    }

    @Override
    protected BoardCell clone() throws CloneNotSupportedException {
        BoardCell boardCell = new BoardCell();
        boardCell.setPlayerNum(playerNum);

        return boardCell;
    }
}