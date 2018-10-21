package logic;

import java.io.Serializable;

public class IllegalColChoiceException extends Exception implements Serializable {
    private String message;

    public IllegalColChoiceException(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return message;
    }
}