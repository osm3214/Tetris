package tetris.entity;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;
import javax.swing.JTextField;

public class NextPiecePanel extends JPanel {

	/**
	 * 
	 */	
	private Piece nextPiece;
	private JTextField titleField;
	
	private static final int BLOCK_SIZE = 20;
	private static final Color BACKGROUND_COLOR = Color.black;
	private static final Color TEXT_COLOR = Color.white;
	
	private static final long serialVersionUID = 1L;

	public NextPiecePanel() {
		nextPiece = new Piece();

		setBackground(Color.black);
		setLayout(null);
		
		titleField = new JTextField();
		titleField.setBounds(0, 0, 120, 20);
		titleField.setForeground(TEXT_COLOR);
		titleField.setBackground(BACKGROUND_COLOR);
		titleField.setText("Next");
		add(titleField);
	}
	
	public void setNextPieceShape(int shape) {
		nextPiece.setShape(shape);
	}
	
    @Override
    public void paintComponent(Graphics g0) {
        Graphics2D g = (Graphics2D)g0;
        super.paintComponent(g);
    	setBackground(Color.black);
    	
    	for (int i = 0; i < 4; i++) {
    		int x = 2 + nextPiece.getXById(i);
    		int y = 3 - nextPiece.getYById(i);
    		
    		drawSquare(g, x * BLOCK_SIZE, y * BLOCK_SIZE, nextPiece.getColor());   
    	}
    }
    
    public void drawSquare(Graphics g, int x, int y, Color c) {        
        g.setColor(c);
        g.fillRect(x + 1, y + 1, BLOCK_SIZE - 2, BLOCK_SIZE - 2);

        g.setColor(c.brighter());
        g.drawLine(x + 1, y + 1, x + 1, y + BLOCK_SIZE - 1);
        g.drawLine(x + 2, y + 2, x + 2, y + BLOCK_SIZE - 2);
        g.drawLine(x + 1, y + 1, x + BLOCK_SIZE - 1, y + 1);
        g.drawLine(x + 2, y + 2, x + BLOCK_SIZE - 2, y + 2);
        g.setColor(c.darker());
        g.drawLine(x + 1, y + BLOCK_SIZE - 1, x + BLOCK_SIZE - 1, y + BLOCK_SIZE - 1);
        g.drawLine(x + 2, y + BLOCK_SIZE - 2, x + BLOCK_SIZE - 2, y + BLOCK_SIZE - 2);
        g.drawLine(x + BLOCK_SIZE - 1, y + 1, x + BLOCK_SIZE - 1, y + BLOCK_SIZE - 1);
        g.drawLine(x + BLOCK_SIZE - 2, y + 2, x + BLOCK_SIZE - 2, y + BLOCK_SIZE - 2);
    }
    
}
