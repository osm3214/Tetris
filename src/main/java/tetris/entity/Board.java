package tetris.entity;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Random;

import javax.swing.JPanel;

public class Board extends JPanel {

	/**
	 *
	 */
	private int[][] board;
	private int currentX;
	private int currentY;
	private Piece currentPiece;
	private int shadowX, shadowY;

	private static final int NUM_COLS = 10;
	private static final int NUM_ROWS = 20;
	private static final int BLOCK_SIZE = 20;
	private static final Color BACKGROUND_COLOR = Color.black;

	private static final long serialVersionUID = 1L;

    public Board() {
		board = new int[NUM_ROWS][NUM_COLS];
    	currentPiece = new Piece();

    	setBackground(BACKGROUND_COLOR);
    	setLayout(null);
    }

   public boolean canMove(int nextX, int nextY) {
	   	for (int i = 0; i < 4; i++) {
			int x = nextX + currentPiece.getXById(i);
			int y = nextY + currentPiece.getYById(i);

			if (x < 0 || NUM_COLS <= x || y < 0 || NUM_ROWS <= y) {
				return false;
			}

			if (board[y][x] != 0) {
				return false;
			}
		}
	   	return true;
   }

   public static boolean canMove(int[][] b, Piece p, int nextX, int nextY) {
	for (int i = 0; i < 4; i++) {
			int x = nextX + p.getXById(i);
			int y = nextY + p.getYById(i);

			if (x < 0 || NUM_COLS <= x || y < 0 || NUM_ROWS <= y) {
				return false;
			}

			if (b[y][x] != 0) {
				return false;
			}
		}
	   	return true;
	}

    public boolean moveTo(int nextX, int nextY) {
    	for (int i = 0; i < 4; i++) {
    		int x = nextX + currentPiece.getXById(i);
    		int y = nextY + currentPiece.getYById(i);

    		if (x < 0 || NUM_COLS <= x || y < 0 || NUM_ROWS <= y) {
    			return false;
    		}

    		if (board[y][x] != 0) {
    			return false;
    		}
    	}
    	currentX = nextX;
    	currentY = nextY;

    	return true;
    }

    public void hardDrop() {
    	while (moveDown()) ;
    }

    public void setShadow() {
    	shadowX = currentX;
    	shadowY = currentY;
    	while (canMove(shadowX, shadowY - 1)) {
    		shadowY--;
    	}
    }


    public boolean moveDown() {
    	boolean isok = moveTo(currentX, currentY - 1);
    	return isok;
    }


    public boolean moveLeft() {
    	boolean isok = moveTo(currentX - 1, currentY);
    	setShadow();
    	return isok;
    }


    public boolean moveRight() {
    	boolean isok = moveTo(currentX + 1, currentY);
    	setShadow();
    	return isok;
    }


    public boolean rotate(boolean clockwise) {
        if (currentPiece.getShape() == 0 || currentPiece.getShape() == 5) {
            return true;
        }
      	for (int i = 0; i < 4; i++) {
	    		int x = currentX + (clockwise ? currentPiece.getYById(i) : -currentPiece.getYById(i));
	    		int y = currentY + (clockwise ? -currentPiece.getXById(i) : currentPiece.getXById(i));

	    		if (x < 0 || NUM_COLS <= x || y < 0 || NUM_ROWS <= y) {
	    			return false;
	    		}

	    		if (board[y][x] != 0) {
	    			return false;
	    		}
	    	}
      	currentPiece.rotate(clockwise);

      	return true;
    }

    public boolean rotateClockwise() {
    	boolean isok = rotate(true);
    	setShadow();
    	return isok;
    }

    public boolean rotateAntiClockwise() {
    	boolean isok = rotate(false);
    	setShadow();
    	return isok;
    }


    public void register() {
		int shape = currentPiece.getShape();
		int x, y;

    	for (int i = 0; i < 4; i++) {
    		x = currentX + currentPiece.getXById(i);
    		y = currentY + currentPiece.getYById(i);
    		if (x < 0 || NUM_COLS <= x || y < 0 || NUM_ROWS <= y) {
    			continue;
    		}
    		board[y][x] = shape;
    	}
    }

	public static void register(int[][] b, Piece p, int curX, int curY) {
		int shape = p.getShape();

		for (int i = 0; i < 4; i++) {
			int x = curX + p.getXById(i);
			int y = curY + p.getYById(i);
			if (x < 0 || NUM_COLS <= x || y < 0 || NUM_ROWS <= y) {
				continue;
			}
			b[y][x] = shape;
		}
	}

	public boolean stackGarbageBlock(int numLines) {
		boolean gameover = false;

		for (int x = 0; x < NUM_COLS; x++) {
			if (board[NUM_ROWS - numLines][x] != 0) {
				gameover = true;
			}
		}
		for (int y = NUM_ROWS - 1; y >= numLines; y--) {
			for (int x = 0; x < NUM_COLS; x++) {
				board[y][x] = board[y - numLines][x];
			}
		}
		for (int y = numLines - 1; y >= 0; y--) {
			int holeIdx = new Random().nextInt(NUM_COLS);
			for (int x = 0; x < NUM_COLS; x++) {
				if (x != holeIdx) {
					board[y][x] = 1;
				} else {
					board[y][x] = 0;
				}
 			}
		}
		setShadow();

		return gameover;
	}

	public static void binarize(int[][] b) {
		for (int y = 0; y < NUM_ROWS; y++) {
			for (int x = 0; x < NUM_COLS; x++) {
				if (b[y][x] > 0) {
					b[y][x] = 1;
				} else {
					b[y][x] = 0;
				}
			}
		}
	}

    public void clearBoard() {
    	for (int y = 0; y < NUM_ROWS; y++) {
    		for (int x = 0; x < NUM_COLS; x++) {
    			board[y][x] = 0;
    		}
    	}
    }


    public int removeLines() {
    	int numLinesRemoved = 0;

    	for (int y =  NUM_ROWS - 1; y >= 0; y--) {
    		boolean isFilled = true;
    		for (int x = 0; x < NUM_COLS; x++) {
    			if (board[y][x] == 0) {
    				isFilled = false;
    				break;
    			}
    		}
    		if (isFilled) {
    			for (int i = y; i < NUM_ROWS - 1; i++) {
    				for (int x = 0; x < NUM_COLS; x++) {
    					board[i][x] = board[i + 1][x];
    				}
    			}
	    		numLinesRemoved++;
    		}
    	}

    	return numLinesRemoved;
    }

    public boolean isCollided(int nextX, int nextY) {
    	int x, y;

    	for (int i = 0; i < 4; i++) {
    		x = nextX + currentPiece.getXById(i);
    		y = nextY + currentPiece.getYById(i);
    		if (x < 0 || NUM_COLS <= x || y < 0 || NUM_ROWS <= y) {
    			continue;
    		}

    		if (board[y][x] != 0) {
    			return true;
    		}
    	}

    	return false;
    }

	public static void flip(int[][] b) {
		for (int y = 0; y < NUM_ROWS / 2; y++) {
			for (int x = 0; x < NUM_COLS; x++) {
				int temp = b[y][x];
				b[y][x] = b[NUM_ROWS - y - 1][x];
				b[NUM_ROWS - y - 1][x] = temp;
			}
		}
	}

	public static void printBoard(int[][] b) {
		for (int y = 0; y < NUM_ROWS; y++) {
			for (int x = 0; x < NUM_COLS; x++) {
				System.out.print(b[y][x] + " ");
			}
			System.out.println();
		}
	}

	public static float[][] toFloat(int[][] b) {
		float[][] res = new float[NUM_ROWS][NUM_COLS];
		for (int y = 0; y < NUM_ROWS; y++) {
			for (int x = 0; x < NUM_COLS; x++) {
				res[y][x] = (float) b[y][x];
			}
		}
		return res;
	}

    public boolean setNewPiece() {
    	boolean gameover = false;
    	currentPiece.setRandomShape();
    	currentX = NUM_COLS / 2 - 1;
    	currentY = NUM_ROWS - 1 - currentPiece.getMaxY() + 2;
    	for (int i = 0; i < 2; i++) {
    		if (isCollided(currentX, currentY - 1)) {
    			gameover = true;
    			break;
    		} else {
    			currentY--;
    		}
    	}
    	setShadow();
    	return gameover;
    }

    public boolean setNewPiece(int shape) {
    	boolean gameover = false;
    	currentPiece.setShape(shape);
    	currentX = NUM_COLS / 2 - 1;
    	currentY = NUM_ROWS - 1 - currentPiece.getMaxY() + 2;
    	for (int i = 0; i < 2; i++) {
    		if (isCollided(currentX, currentY - 1)) {
    			gameover = true;
    			break;
    		} else {
    			currentY--;
    		}
    	}
    	setShadow();
    	return gameover;
    }

	public boolean setNewPiece(int shape, int numRotation) {
		boolean gameover = false;
		currentPiece.setShape(shape);
		for (int i = 0; i < numRotation; i++) {
			currentPiece.rotate(true);
		}
		currentX = NUM_COLS / 2 - 1;
		currentY = NUM_ROWS - 1 - currentPiece.getMaxY() + 2;
		for (int i = 0; i < 2; i++) {
			if (isCollided(currentX, currentY - 1)) {
				gameover = true;
				break;
			} else {
				currentY--;
			}
		}
		setShadow();
		return gameover;
	}

	public int getCurrentX() {
		return currentX;
	}

	public int getCurrentY() {
		return currentY;
	}

	public Piece getCurrentPiece() {
		return currentPiece;
	}

	public int getCurrentPieceShape() {
		return currentPiece.getShape();
	}

    public void setCurrentPieceShape(int shape) {
    	currentPiece.setShape(shape);
    }

	public int[][] createCopy() {
		int[][] b = new int[NUM_ROWS][NUM_COLS];
		for (int y = 0; y < NUM_ROWS; y++) {
			for (int x = 0; x < NUM_COLS; x++) {
				b[y][x] = board[y][x];
			}
		}
		return b;
	}


    @Override
    public void paintComponent(Graphics g0) {
        Graphics2D g = (Graphics2D)g0;
        super.paintComponent(g);

        for (int y = 0; y < NUM_ROWS; y++) {
	        for (int x = 0; x < NUM_COLS; x++) {
        		int shape = board[NUM_ROWS - y - 1][x];
        		if (shape != 0) {
                    drawBlock(g, x * BLOCK_SIZE, y * BLOCK_SIZE, Piece.getColorByShape(shape));
	             }
        	}
        }

        if (currentPiece.getShape() != 0) {
        	for (int i = 0; i < 4; i++) {
        		int x = currentX + currentPiece.getXById(i);
        		int y = currentY + currentPiece.getYById(i);

        		drawBlock(g, x * BLOCK_SIZE,
        				(NUM_ROWS - y - 1) * BLOCK_SIZE, currentPiece.getColor());
        	}
        	for (int i = 0; i < 4; i++) {
        		int x = shadowX + currentPiece.getXById(i);
        		int y = shadowY + currentPiece.getYById(i);

        		drawShadowBlock(g, x * BLOCK_SIZE,
        				(NUM_ROWS - y - 1) * BLOCK_SIZE, currentPiece.getColor());
        	}
        }
    }

    public void drawBlock(Graphics g, int x, int y, Color c) {
        g.setColor(c);
        g.fillRect(x + 1, y + 1, BLOCK_SIZE - 3, BLOCK_SIZE - 3);

        g.setColor(c.brighter());
        g.drawLine(x, y, x, y + BLOCK_SIZE - 1);
        g.drawLine(x + 1, y + 1, x + 1, y + BLOCK_SIZE - 2);
        g.drawLine(x, y, x + BLOCK_SIZE - 1, y);
        g.drawLine(x + 1, y + 1, x + BLOCK_SIZE - 2, y + 1);
        g.setColor(c.darker());
        g.drawLine(x, y + BLOCK_SIZE - 1, x + BLOCK_SIZE - 1, y + BLOCK_SIZE - 1);
        g.drawLine(x + 1, y + BLOCK_SIZE - 2, x + BLOCK_SIZE - 2, y + BLOCK_SIZE - 2);
        g.drawLine(x + BLOCK_SIZE - 1, y, x + BLOCK_SIZE - 1, y + BLOCK_SIZE - 1);
        g.drawLine(x + BLOCK_SIZE - 2, y + 1, x + BLOCK_SIZE - 2, y + BLOCK_SIZE - 2);
    }

    public void drawShadowBlock(Graphics g, int x, int y, Color c) {
    	g.setColor(c);
    	g.drawLine(x, y, x, y + BLOCK_SIZE - 1);
    	g.drawLine(x, y, x + BLOCK_SIZE - 1, y);
        g.drawLine(x, y + BLOCK_SIZE - 1, x + BLOCK_SIZE - 1, y + BLOCK_SIZE - 1);
        g.drawLine(x + BLOCK_SIZE - 1, y, x + BLOCK_SIZE - 1, y + BLOCK_SIZE - 1);
    }
}
