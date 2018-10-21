package game;

import engine.MoveTypes;
import javafx.util.Pair;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class Board implements Serializable {
    public final int FULL_COL = -1;

    private BoardCell[][] board;
    private Set<Integer> emptyColumns;

    public Board(int numOfRows, int numOfCols) {
        board = new BoardCell[numOfRows][numOfCols];
        for (int i = 0; i < numOfRows; i++)
            for (int j = 0; j < numOfCols; j++)
                board[i][j] = new BoardCell();

        initializeEmptyColumns(numOfCols);
    }

    public int numOfRows() {
        return board.length;
    }

    public int numOfCols() {
        return board[0].length;
    }

    public Set getEmptyColumns() {
        return emptyColumns;
    }

    public int getCellWithPlayerNum(int row, int col) {
        return board[row][col].getPlayerNum();
    }

    public void setCellWithPlayerNum(int row, int col, int playerNum) {
        board[row][col].setPlayerNum(playerNum);
    }

    public int getFirstEmptyCellInCol(int col) {
        if (!isColFull(col)) {
            for (int i = numOfRows() - 1; i >= 0; i--) {
                if (board[i][col].isEmpty())
                    return i;
            }
        }
        return FULL_COL;
    }

    public Set<Integer> getPopoutOptions(int currentPlayerNum) {
        Set<Integer> optionsList = new HashSet<>();
        for (int i = 0; i < numOfCols(); i++) {
            if (board[numOfRows() - 1][i].getPlayerNum() == currentPlayerNum)
                optionsList.add(i);
        }
        return optionsList;
    }

    private void initializeEmptyColumns(int numOfCols) {
        emptyColumns = new HashSet(numOfCols);

        for (int i = 0; i < numOfCols; i++)
            emptyColumns.add(i);
    }

    public boolean isColFull(int col) {
        return !board[0][col].isEmpty();
    }

    public boolean isInBorders(int row, int col) {
        return row < numOfRows() && col < numOfCols() && row >= 0 && col >= 0;
    }

    public void updateEmptyColumns(int col) {
        if (isColFull(col))
            emptyColumns.remove(col);
    }

    public void restart() {
        for (BoardCell[] boardCells : board) {
            for (BoardCell boardCell : boardCells) {
                boardCell.setPlayerNum(BoardCell.EMPTY_CELL);
            }
        }
        initializeEmptyColumns(board[0].length);
    }

    public void makeEmptyFIrstCellInCol(Pair<Integer, MoveTypes> lastMoveCol) {
        int row = getFirstEmptyCellInCol(lastMoveCol.getKey()) + 1;
        int col = lastMoveCol.getKey();

        board[row][col].setPlayerNum(BoardCell.EMPTY_CELL);
    }

    public boolean isCurrentPlayerInTheLastRow(int playerNum) {
        for (BoardCell boardCell : board[numOfRows() - 1]) {
            if (boardCell.getPlayerNum() == playerNum)
                return true;
        }
        return false;
    }

    public void addToEmptyColumns(int col) {
        emptyColumns.add(col);
    }

    public void clearCurrPlayerDiscsFromBoard(int currentPlayerNum) {
        for (int i = 0; i < numOfRows(); i++)
            for (int j = 0; j < numOfCols(); j++) {
                if (board[i][j].getPlayerNum() == currentPlayerNum)
                    board[i][j].setPlayerNum(BoardCell.EMPTY_CELL);
            }
    }

    public void orderDiscsAfterQuitPlayer() {
        for (int col = 0; col < numOfCols(); col++)
            for (int row = numOfRows() - 1; row > 0; row--) {
                if (board[row][col].getPlayerNum() == BoardCell.EMPTY_CELL && exsitingUpperDiscs(col, row)) {
                    for (int x = row; x > 0; x--) {
                        board[x][col].setPlayerNum(board[x - 1][col].getPlayerNum());
                    }
                    board[0][col].setPlayerNum(BoardCell.EMPTY_CELL);
                    row++;
                }
            }
    }

    private boolean exsitingUpperDiscs(int col, int row) {
        for (int i = 0; i < numOfRows(); i++) {
            if (board[i][col].getPlayerNum() != BoardCell.EMPTY_CELL)
                return i < row;
        }

        return false;
    }

    public BoardCell[][] getBoard() {
        return board;
    }

    public void setBoard(BoardCell[][] boardBackup) {
        this.board = boardBackup;
    }
}