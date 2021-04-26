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

public class GamePanel extends JPanel {

    private PlayerPanel playerPanel;
    private AiPanel aiPanel;

	private Agent agent;
	private Thread thread;

    private boolean isPaused;
    private boolean isGameStarted;

    private JButton startButton, pauseButton, resetButton;

	private Timer timer;
    private Clip bgmClip;

    private static final int DROP_PERIOD = (int) (0.45 * 1000);
    private static final int PLAYERPANEL_HEIGHT = 460;
    private static final int PLAYERPANEL_WIDTH = 400;
	private static final int BUTTON_HEIGHT = 20;
	private static final int BUTTON_WIDTH = 60;
    private static final Color BACKGROUND_COLOR = Color.BLACK;
	private static final String BGM_FILE = "resources/bgm.wav";

	private static final long serialVersionUID = 1L;

    public GamePanel() {

        setLayout(null);
        setBackground(BACKGROUND_COLOR);

        playerPanel = new PlayerPanel();
        playerPanel.setBounds(5, 5, PLAYERPANEL_WIDTH, PLAYERPANEL_HEIGHT);
        add(playerPanel);

        aiPanel = new AiPanel();
        aiPanel.setBounds(410, 5, PLAYERPANEL_WIDTH, PLAYERPANEL_HEIGHT);
        add(aiPanel);

		agent = new Agent(aiPanel);
		aiPanel.setAgent(agent);

		timer = new Timer(DROP_PERIOD, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				update();
			}
		});

        bgmClip = SoundPlayer.createClip(new File(BGM_FILE));

        setFocusable(true);
    	addKeyListener(new KeyListener() {
    	    @Override
    	    public void keyPressed(KeyEvent e) {
    	    	int keycode = e.getKeyCode();
    	    	switch (keycode) {
    	    	case KeyEvent.VK_LEFT:
    	    	case KeyEvent.VK_H:
    	    		if (!isPaused) {
                        playerPanel.moveLeft();
                    }
    	    		break;
    	    	case KeyEvent.VK_RIGHT:
    	    	case KeyEvent.VK_L:
    	    		if (!isPaused) {
                        playerPanel.moveRight();
                    }
    	    		break;
    	    	case KeyEvent.VK_DOWN:
    	    	case KeyEvent.VK_J:
    	    		if (!isPaused) {
                        playerPanel.moveDown();
                    }
    	    		break;
    	    	case KeyEvent.VK_UP:
    	    	case KeyEvent.VK_K:
    	    		if (!isPaused) {
                        playerPanel.hardDrop();
                    }
    	    		break;
    	    	case KeyEvent.VK_A:
    	    		if (!isPaused) {
                        playerPanel.rotateClockwise();
                    }
    	    		break;
    	    	case KeyEvent.VK_D:
    	    		if (!isPaused) {
                        playerPanel.rotateAntiClockwise();
                    }
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
    	    }

    		@Override
    		public void keyReleased(KeyEvent e) {
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

		thread = new Thread(new Runnable(){
			@Override
			public void run() {
				agent.play();
			}
		});
		thread.start();
    }

    public void reset() {
    	bgmClip.stop();
    	bgmClip.flush();
    	bgmClip.setFramePosition(0);

    	setIsPaused(false);
    	setIsGameStarted(false);

        playerPanel.reset();
        aiPanel.reset();

    	timer.restart();
    	timer.stop();

    	startButton.setEnabled(true);
    	pauseButton.setEnabled(false);
    	resetButton.setEnabled(false);
    }

    public void start() {
    	if (!isGameStarted) {
    		playerPanel.blinkStatus("GAME START");
            aiPanel.blinkStatus("GAME START");
    		setIsGameStarted(true);
    	}
    	if (isPaused) {
    		playerPanel.setStatus("");
            aiPanel.setStatus("");
    		setIsPaused(false);
    	}
		timer.start();

		startButton.setEnabled(false);
		pauseButton.setEnabled(true);
		resetButton.setEnabled(false);
		bgmClip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void pause() {
		timer.stop();
		playerPanel.setStatus("PAUSE");
        aiPanel.setStatus("Pause");
		startButton.setEnabled(true);
		pauseButton.setEnabled(false);
		resetButton.setEnabled(true);
		setIsPaused(true);
    	bgmClip.stop();
    }

    public void update() {
        boolean isPlayerGameOver = false, isAiGameOver = false;
		int numPlayerRemovedLines, numAiRemovedLines;

        numPlayerRemovedLines = playerPanel.update();
        numAiRemovedLines = aiPanel.update();

		if (numPlayerRemovedLines == -1) {
			isPlayerGameOver = true;
		}
		if (numAiRemovedLines == -1) {
			isAiGameOver = true;
		}
        if (isPlayerGameOver || isAiGameOver) {
            gameOver(isPlayerGameOver, isAiGameOver);
        } else {
			if (numPlayerRemovedLines > 0) {
				isAiGameOver = aiPanel.stackGarbageBlock(numPlayerRemovedLines);
			}
			if (numAiRemovedLines > 0) {
				isPlayerGameOver = playerPanel.stackGarbageBlock(numAiRemovedLines);
			}
			if (isPlayerGameOver || isAiGameOver) {
				gameOver(isPlayerGameOver, isAiGameOver);
			}
		}
    }

    public void gameOver(boolean isPlayerGameOver, boolean isAiGameOver) {
        playerPanel.gameOver();
        aiPanel.gameOver();

		timer.stop();
        if (isPlayerGameOver) {
		    playerPanel.setStatus("GAME OVER");
            aiPanel.setStatus("YOU WIN!");
        } else {
            playerPanel.setStatus("YOU WIN!");
            aiPanel.setStatus("GAME OVER");
        }

		startButton.setEnabled(false);
		pauseButton.setEnabled(false);
		resetButton.setEnabled(true);
    }

	public void setIsGameStarted(boolean isGameStarted) {
		this.isGameStarted = isGameStarted;
		agent.setIsGameStarted(isGameStarted);
	}

	public void setIsPaused(boolean isPaused) {
		this.isPaused = isPaused;
		agent.setIsPaused(isPaused);
	}
}
