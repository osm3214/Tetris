package tetris.entity;

import java.awt.Color;
import java.util.Random;

public class Piece {
	private int shape;
	private int coordinate[][];
	private Color color;

	private static final int COORDINATES[][][] = {
        { { 0, 0 },   { 0, 0 }, { 0, 0 },  { 0, 0 } }, // NULL
        { { -1, 0 },  { 0, 0 }, { 0, 1 },  { 1, 1 } }, // S
        { { -1, 1 },  { 0, 1 }, { 0, 0 },  { 1, 0 } }, // Z
        { { -1, 0 },  { 0, 0 }, { 1, 0 },  { 2, 0 } }, // I
        { { -1, 0 },  { 0, 0 }, { 1, 0 },  { 0, 1 } }, // T
        { { 1, 0 },   { 0, 0 }, { 0, 1 },  { 1, 1 } }, // O
        { { -1, 1 },  { -1, 0 },{ 0, 0 },  { 1, 0 } }, // J
        { { -1, 0 },  { 0, 0 }, { 1, 0 },  { 1, 1 } } // L
	};
	private static final Color COLORS[] = {
		Color.black,
		new Color(239,83,80), // red
		new Color(66,165,245), // blue
		new Color(102,187,106), //green
		new Color(255,167,38), // orange
		new Color(38,198,218), // cyan
		new Color(255,238,88), // yellow
		new Color(171,71,188) // purple
	};
	private static final int NUM_SHAPE_KINDS[] = {
		1, 2, 2, 2, 4, 1, 4, 4
	};

	public Piece() {
		coordinate = new int[4][2];
		setShape(0);
	}

	public Piece(int shape) {
		coordinate = new int[4][2];
		setShape(shape);
	}

    public void rotate(boolean clockwise) {
        if (shape == 0 || shape == 5)
            return;

        for (int i = 0; i < 4; ++i) {
        	int x = clockwise ? getYById(i) : -getYById(i);
    		int y = clockwise ? -getXById(i) : getXById(i);
            this.setXById(i, x);
            this.setYById(i, y);
        }
    }

	public int getShape() {
		return shape;
	}

	public void setShape(int s) {
		shape = s;
		for (int i = 0; i < 4 ; i++) {
			for (int j = 0; j < 2; ++j) {
				coordinate[i][j] = COORDINATES[s][i][j];
			}
		}
		color = COLORS[s];
	}

	public void setRandomShape() {
		Random r = new Random();
		int s = r.nextInt(7) + 1;

		setShape(s);
	}

	public int getXById(int index) {
		return coordinate[index][0];
	}

	public int getYById(int index) {
		return coordinate[index][1];
	}

    public void setXById(int index, int x) {
    	coordinate[index][0] = x;
    }

    public void setYById(int index, int y) {
    	coordinate[index][1] = y;
    }

	public int getMinX() {
		int minX = 2;
		for (int i = 0; i < 4; i++) {
			if (minX > coordinate[i][0]) {
				minX = coordinate[i][0];
			}
		}
		return minX;
	}

	public int getMaxX() {
		int maxX = -1;
		for (int i = 0; i < 4; i++) {
			if (maxX < coordinate[i][0]) {
				maxX = coordinate[i][0];
			}
		}
		return maxX;
	}

	public int getMinY() {
		int minY = 1;
		for (int i = 0; i < 4; i++) {
			if (minY > coordinate[i][1]) {
				minY = coordinate[i][1];
			}
		}

		return minY;
	}

	public int getMaxY() {
		int maxY = -1;
		for (int i = 0; i < 4; i++) {
			if (maxY < coordinate[i][1]) {
				maxY = coordinate[i][1];
			}
		}
		return maxY;
	}

	public Color getColor() {
		return color;
	}

	public static Color getColorByShape(int shape) {
		return COLORS[shape];
	}

	public int getNumShapeKind() {
		return NUM_SHAPE_KINDS[shape];
	}
}
