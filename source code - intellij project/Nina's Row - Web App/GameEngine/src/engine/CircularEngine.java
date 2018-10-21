package engine;

public class CircularEngine extends Engine {
    public CircularEngine(int numOfRows, int numOfCols, int target) {
        super(numOfRows, numOfCols, target);
    }

    @Override
    public boolean checkSeries(int playerNum, int row, int col, int rowSteps, int colSteps) {
        int countSeries = 0;
        int[] cell;

        for (int i = 0; i < target * 2 + 1; i++) {
            cell = getCircularIndex(row, col);
            if (board.getCellWithPlayerNum(cell[0], cell[1]) == playerNum) {
                countSeries++;
                if (countSeries == target)
                    return true;
            } else {
                countSeries = 0;
            }
            col += colSteps;
            row += rowSteps;
        }
        return false;
    }

    private int[] getCircularIndex(int row, int col) {
        int[] cell = new int[2];
        cell[0] = row >= 0 ? row % board.numOfRows() : board.numOfRows() + row;
        cell[1] = col >= 0 ? col % board.numOfCols() : board.numOfCols() + col;

        return cell;
    }

    @Override
    public boolean madeSeriesInDiagonal(int row, int col, int playerNum) {
        boolean hasSeries = super.checkSeries(playerNum, row - target, col - target, FORWARD, FORWARD);
        if (!hasSeries) {
            hasSeries = super.checkSeries(playerNum, row - target, col + target, FORWARD, BACKWARD);
        }

        return hasSeries;
    }

    @Override
    public String toString() {
        return "Circular game";
    }

    @Override
    public String getInstruction() {
        return "Make a series of " +target+ " discs!\nIn horizontal or vertical it is a 2D board";
    }
}
