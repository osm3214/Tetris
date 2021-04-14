package tetris.entity;

public class Action {

    public int x;
    public int numRotation;

    public Action(int x, int r) {
        this.x = x;
        numRotation = r;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + numRotation + ")";
    }
}