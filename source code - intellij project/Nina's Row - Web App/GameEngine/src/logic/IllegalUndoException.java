package logic;

public class IllegalUndoException extends Exception {
    private final String message = "Error: There are no moves to undo for the last player";

    public IllegalUndoException() {
    }

    @Override
    public String toString() {
        return message;
    }
}