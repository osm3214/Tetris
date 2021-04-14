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

        // while (true) {
        //     sleep(500);
        // System.out.println(isFalling + " " + isPaused + " " + isGameStarted);
        //     if (isFalling && !isPaused && isGameStarted) {
                nextActions = searchNextActions();
                nextStates = searchNextStates(nextActions);

                nextActionIdx = brain.selectAction(nextStates);
                nextAction = nextActions.get(nextActionIdx);

                step(nextAction);
                // isFalling = false;
            // }
            // isFalling = panel.getIsFalling();
        // }
    }

    public void step(Action nextAction) {
        rotate(nextAction.numRotation);
        moveX(nextAction.x);
        moveY();
    }

    public ArrayList<Action> searchNextActions() {
        ArrayList<Action> nextActions = new ArrayList<Action>();
        Piece piece = new Piece(panel.getCurrentPiece().getShape());

        for (int numRotation = 0; numRotation < piece.getNumShapeKind(); numRotation++) {
            for (int x = -piece.getMinX(); x < NUM_COLS - piece.getMaxX(); x++) {
                int[][] board = panel.getBoard().createCopy();
                int currentX = x;
                int currentY = NUM_ROWS - piece.getMaxY() - 1;

                if (!Board.canMove(board, piece, currentX, currentY)) {
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
        Piece piece = new Piece(panel.getCurrentPiece().getShape());
        int lastNumRoatation = 0;

        for (Action action: nextActions) {
            int[][] board = panel.getBoard().createCopy();
            int currentX = action.x;
            int currentY = NUM_ROWS - piece.getMaxY();

            if (action.numRotation != lastNumRoatation) {
                piece.rotate(true);
            }

            while (Board.canMove(board, piece, currentX, currentY - 1)) {
                currentY--;
            }

            Board.register(board, piece, currentX, currentY);
            nextStates.add(board);
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
