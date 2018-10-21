package players;

public class Computer extends Player {

    public Computer(String name) {
        super(name);
    }

    @Override
    public String getType() {
        return "Computer";
    }
}