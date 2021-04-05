package tetris.entity;

import java.util.ArrayList;

public class Agent {

    public Brain brain;
    public AiPanel panel;

    private boolean isFalling;
    private boolean isGameStarted;
    private boolean isPaused;

    private static final int NUM_COLS = 10;
    private static final int NUM_ROWS = 20;

    public Agent(AiPanel p) {
        brain = new Brain();
        panel = p;
        isGameStarted = false;
        isPaused = false;
    }

    public void play() {
        ArrayList<Action> nextActions;
        ArrayList<int[][]> nextStates;
        Action nextAction;
        int nextActionIdx;

        while (true) {
            sleep(100);
            if (isFalling && !isPaused && isGameStarted) {
                nextActions = searchNextActions();
                nextStates = searchNextStates(nextActions);
                System.out.println(nextActions);
                System.out.println(nextActions.size());
                System.out.println(nextStates.size());

                nextActionIdx = brain.selectAction(nextStates);
                nextAction = nextActions.get(nextActionIdx);
                System.out.println(nextAction);

                step(nextAction);
                isFalling = false;
                panel.update();
            }
            isFalling = panel.getIsFalling();
        }
    }

    public void step(Action nextAction) {
        // sleep(250);
        rotate(nextAction.numRotation);
        // sleep(250);
        moveX(nextAction.x);
        // sleep(250);
        moveY();
    }

    public ArrayList<Action> searchNextActions() {
        ArrayList<Action> nextActions = new ArrayList<Action>();
        Piece piece = new Piece(panel.getCurrentPiece().getShape());
        int currentShape, numRotations;

        currentShape = piece.getShape();
        if (piece.getShape() == 5) {
            numRotations = 1;
        } else if (currentShape == 1 || currentShape == 2 || currentShape == 3) {
            numRotations = 2;
        } else {
            numRotations = 4;
        }

        for (int numRotation = 0; numRotation < numRotations; numRotation++) {
            for (int x = -piece.getMinX(); x < NUM_COLS - piece.getMaxX(); x++) {
                int[][] b = panel.getBoard().createCopy();
                int currentX = x;
                int currentY = NUM_ROWS - piece.getMaxY() - 1;

                if (!Board.canMove(b, piece, currentX, currentY)) {
	    			System.out.println("Cannot move to the point where a block is already placed.");
                    continue;
                }
                nextActions.add(new Action(currentX, numRotation));
            }
            piece.rotate(true);
        }

        return nextActions;
    }

    public ArrayList<int[][]> searchNextStates(ArrayList<Action> nextActions) {
        ArrayList<int[][]> nextStates = new ArrayList<int[][]>();
        Piece p = new Piece(panel.getCurrentPiece().getShape());
        int lastNumRoatation = 0;

        for (Action action: nextActions) {
            int[][] b = panel.getBoard().createCopy();
            int currentX = action.x;
            int currentY = NUM_ROWS - p.getMaxY();

            if (action.numRotation != lastNumRoatation) {
                p.rotate(true);
            }

            while (Board.canMove(b, p, currentX, currentY - 1)) {
                currentY--;
            }

            Board.register(b, p, currentX, currentY);
            nextStates.add(b);
            lastNumRoatation = action.numRotation;
        }

        return nextStates;
    }

    public void rotate(int numRotation) {
        panel.setNewPiece(numRotation);
    }

    public void moveX(int goal) {
        boolean moved;

        while (panel.getBoard().getCurrentX() != goal) {
            if (panel.getBoard().getCurrentX() > goal) {
                moved = panel.moveLeft();
                if (!moved) {
                    break;
                }
            } else {
                moved = panel.moveRight();
                if (!moved) {
                    break;
                }
            }

            // sleep(100);
        }
    }

    public void moveY() {
        panel.hardDrop();
    }

    public void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setIsGameStarted(boolean isGameStarted) {
        this.isGameStarted = isGameStarted;
    }

    public void setIsPaused(boolean isPaused) {
        this.isPaused = isPaused;
    }
}
