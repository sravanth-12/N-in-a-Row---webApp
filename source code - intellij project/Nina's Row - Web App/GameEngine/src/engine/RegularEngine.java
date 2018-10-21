package engine;

public class RegularEngine extends Engine {

    public RegularEngine(int numOfRows, int numOfCols, int target) {
        super(numOfRows, numOfCols, target);
    }

    @Override
    public String toString() {
        return "Regular Game";
    }

    @Override
    public String getInstruction() {
        return "Make a series of " +target+ " discs!";
    }
}
