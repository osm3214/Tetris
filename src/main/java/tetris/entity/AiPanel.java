package tetris.entity;

import java.awt.Color;

import javax.swing.JPanel;

public class AiPanel extends JPanel {

    private Piece currentPiece;
    private Piece nextPiece;
    private Board board;

    private boolean isFalling;
    private int numSteps;
    private int numLinesRemoved;
    private int score;
    private String status;

    private NextPiecePanel nextPiecePanel;
    private StatusPanel statusPanel;

    private static final Color BACKGROUND_COLOR = Color.lightGray;
    private static final int NUM_COLUMNS = 10;
    private static final int NUM_ROWS = 20;
    private static final int BLOCK_SIZE = 20;

    private static final long serialVersionUID = 1L;

    public AiPanel() {
        currentPiece = new Piece();
        nextPiece = new Piece();

        setBackground(BACKGROUND_COLOR);
        setLayout(null);

        board = new Board();
        board.setBounds(20, 20, NUM_COLUMNS * BLOCK_SIZE, NUM_ROWS * BLOCK_SIZE);

        nextPiecePanel = new NextPiecePanel();
        nextPiecePanel.setBounds(260, 20, 120, 120);
        statusPanel = new StatusPanel();
        statusPanel.setBounds(260, 180, 120, 240);
        add(board);
        add(nextPiecePanel);
        add(statusPanel);
    }

    public void reset() {
        isFalling = false;

        board.clearBoard();
        resetPiece();
        resetStatus();
    }

    public boolean update() {
        int numLinesRemoved = 0;
        boolean gameover;

        if (isFalling) {
            if (!moveDown()) {
                isFalling = false;
                board.register();
                numLinesRemoved = board.removeLines();
                if (numLinesRemoved > 0) {
                    setCurrentPieceShape(0);
                    setNumLinesRemoved(this.numLinesRemoved + numLinesRemoved);
                    setScore(this.score + (int)Math.pow(numLinesRemoved, 2));
                }
            }
            gameover = false;
        } else {
            isFalling = true;
            gameover = setNewPiece();
            setNumSteps(numSteps + 1);
        }
        return gameover;
    }

    public void gameOver() {
        board.register();
        setCurrentPieceShape(0);
    }

    public boolean moveDown() {
        boolean isok = board.moveDown();
        return isok;
    }

    public boolean moveLeft() {
        boolean isok = board.moveLeft();
        return isok;
    }

    public boolean moveRight() {
        boolean isok = board.moveRight();
        return isok;
    }

    public void hardDrop(){
        board.hardDrop();
    }

    public boolean rotateClockwise() {
        boolean isok = board.rotateClockwise();
        return isok;
    }

    public boolean rotateAntiClockwise() {
        boolean isok = board.rotateAntiClockwise();
        return isok;
    }

    public void resetPiece() {
        setCurrentPieceShape(0);
        setNextPieceShape(0);
    }

    public void resetStatus() {
        setNumSteps(0);
        setNumLinesRemoved(0);
        setScore(0);
        setStatus("STAND BY");
    }

    public boolean setNewPiece() {
        boolean gameover;

        if (nextPiece.getShape() == 0) {
            currentPiece.setRandomShape();
        } else {
            currentPiece.setShape(nextPiece.getShape());
        }
        nextPiece.setRandomShape();

        gameover = board.setNewPiece(currentPiece.getShape());
        nextPiecePanel.setNextPieceShape(nextPiece.getShape());

        return gameover;
    }

    public Piece getCurrentPiece() {
        return currentPiece;
    }

    public Board getBoard() {
        return board;
    }

    public void setCurrentPieceShape(int shape) {
        currentPiece.setShape(shape);
        board.setCurrentPieceShape(shape);
    }

    public void setNextPieceShape(int shape) {
        nextPiece.setShape(shape);
        nextPiecePanel.setNextPieceShape(shape);
    }

    public boolean getIsFalling() {
        return isFalling;
    }

    public void setIsFalling(boolean isFalling) {
        this.isFalling = isFalling;
    }

    public void setNumSteps(int numSteps) {
        this.numSteps = numSteps;
        statusPanel.setNumSteps(this.numSteps);
    }

    public void setNumLinesRemoved(int numLinesRemoved) {
        this.numLinesRemoved = numLinesRemoved;
        statusPanel.setNumLinesRemoved(this.numLinesRemoved);
    }

    public void setScore(int score) {
        this.score = score;
        statusPanel.setScore(this.score);
    }

    public void setStatus(String status) {
        this.status = status;
        statusPanel.setStatus(this.status);
    }

    public void blinkStatus(String status) {
        statusPanel.blinkStatus(status);
    }
}
