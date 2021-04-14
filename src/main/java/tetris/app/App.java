package tetris.app;

import java.awt.Color;

import javax.swing.JFrame;

import tetris.entity.GamePanel;


public class App extends JFrame {

	private static final long serialVersionUID = 1L;

	private GamePanel panel;

	public App() {
        setTitle("TETRIS");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 810, 560);
		setBackground(Color.white);
		setResizable(false);

		panel = new GamePanel();
		setContentPane(panel);
		panel.reset();
	}
}
