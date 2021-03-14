package tetris.entity;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;

import javax.sound.sampled.Clip;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.Timer;

import tetris.util.SoundPlayer;

public class PlayerPanel extends JPanel {

	/**
	 *
	 */
	private Piece currentPiece;
	private Piece nextPiece;
	private Board board;

	private boolean isFalling;
	private boolean isPaused;
	private boolean isGameStarted;
	private int numSteps;
	private int numLinesRemoved;
	private int score;
	private String status;

	private Timer timer;

	private Clip bgmClip;

	private NextPiecePanel nextPiecePanel;
	private StatusPanel statusPanel;
	private JButton startButton, pauseButton, resetButton;

	private static final Color BACKGROUND_COLOR = Color.lightGray;
	private static final int NUM_COLUMNS = 10;
	private static final int NUM_ROWS = 20;
	private static final int BLOCK_SIZE = 20;
	private static final int BUTTON_HEIGHT = 20;
	private static final int BUTTON_WIDTH = 60;
	private static final String BGM_FILE = "golden_wind.wav";

	private static final long serialVersionUID = 1L;

    public PlayerPanel(Timer timer) {
    	currentPiece = new Piece();
    	nextPiece = new Piece();
    	this.timer = timer;
		bgmClip = SoundPlayer.createClip(new File(BGM_FILE));

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

    	setFocusable(true);
    	addKeyListener(new KeyListener() {
    	    @Override
    	    public void keyPressed(KeyEvent e) {
    	    	int keycode = e.getKeyCode();
    	    	switch (keycode) {
    	    	case KeyEvent.VK_LEFT:
    	    	case KeyEvent.VK_H:
    	    		if (!isPaused) moveLeft();
    	    		break;
    	    	case KeyEvent.VK_RIGHT:
    	    	case KeyEvent.VK_L:
    	    		if (!isPaused) moveRight();
    	    		break;
    	    	case KeyEvent.VK_DOWN:
    	    	case KeyEvent.VK_J:
    	    		if (!isPaused) moveDown();
    	    		break;
    	    	case KeyEvent.VK_UP:
    	    	case KeyEvent.VK_K:
    	    		if (!isPaused) board.hardDrop();
    	    		break;
    	    	case KeyEvent.VK_A:
    	    		if (!isPaused) rotateClockwise();
    	    		break;
    	    	case KeyEvent.VK_D:
    	    		if (!isPaused) rotateAntiClockwise();
    	    		break;
    	    	case KeyEvent.VK_SPACE:
    	    		if (isPaused || !isGameStarted) {
    	    			start();
    	    		} else {
    	    			pause();
    	    		}
    	    		break;
    	    	}
    	    }

    	    @Override
    	    public void keyTyped(KeyEvent e) {
    			// TODO Auto-generated method stub
    	    }

    		@Override
    		public void keyReleased(KeyEvent e) {
    			// TODO Auto-generated method stub
    		}
    	});

    	startButton = new JButton("Start");
    	startButton.setFocusable(false);
    	startButton.setBounds(20, 480, BUTTON_WIDTH, BUTTON_HEIGHT);
    	startButton.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			start();
    		}
    	});
    	pauseButton = new JButton("Pause");
    	pauseButton.setBounds(100, 480, BUTTON_WIDTH, BUTTON_HEIGHT);
    	pauseButton.setFocusable(false);
    	pauseButton.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			pause();
    		}
    	});
    	resetButton = new JButton("Reset");
    	resetButton.setBounds(180, 480, BUTTON_WIDTH, BUTTON_HEIGHT);
    	resetButton.setFocusable(false);
    	resetButton.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			reset();
    		}
    	});
    	add(startButton);
    	add(pauseButton);
    	add(resetButton);
    }

    public void reset() {
    	bgmClip.stop();
    	bgmClip.flush();
    	bgmClip.setFramePosition(0);

    	isFalling = false;
    	isPaused = false;
    	isGameStarted = false;

    	board.clearBoard();
    	resetPiece();
    	resetStatus();
    	timer.restart();
    	timer.stop();

    	startButton.setEnabled(true);
    	pauseButton.setEnabled(false);
    	resetButton.setEnabled(false);
    }

    public void update() {
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
    	} else {
    		isFalling = true;
    		gameover = setNewPiece();
        	setNumSteps(numSteps + 1);
	    	if (gameover) {
	    		board.register();
	    		gameOver();
	    	}
    	}
    }

    public void start() {
    	if (!isGameStarted) {
    		statusPanel.blinkStatus("GAME START");
    		isGameStarted = true;
    	}
    	if (isPaused) {
    		setStatus("");
    		isPaused = false;
    	}
		timer.start();
		startButton.setEnabled(false);
		pauseButton.setEnabled(true);
		resetButton.setEnabled(false);
		bgmClip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void pause() {
		timer.stop();
		setStatus("PAUSE");
		startButton.setEnabled(true);
		pauseButton.setEnabled(false);
		resetButton.setEnabled(true);
		isPaused = !isPaused;
    	bgmClip.stop();
    }

    public void gameOver() {
		setCurrentPieceShape(0);
		timer.stop();
		setStatus("GAME OVER");
		startButton.setEnabled(false);
		pauseButton.setEnabled(false);
		resetButton.setEnabled(true);
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

	public boolean hardDrop(){
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

    public void setCurrentPieceShape(int shape) {
    	currentPiece.setShape(shape);
    	board.setCurrentPieceShape(shape);
    }

    public void setNextPieceShape(int shape) {
    	nextPiece.setShape(shape);
    	nextPiecePanel.setNextPieceShape(shape);
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
