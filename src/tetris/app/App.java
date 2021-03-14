package tetris.app;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import tetris.entity.GamePanel;


public class App extends JFrame {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private GamePanel panel;

	private static final int PAINT_PERIOD = (int) (0.05*1000);

	public App() {
        setTitle("TETRIS");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 410, 560);
		setBackground(Color.white);
		setResizable(false);

		panel = new GamePanel();
		setContentPane(panel);
		panel.reset();
	}

	public static void main(String[] args) {
		App frame = new App();
		frame.setVisible(true);

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				Timer timer = new Timer(PAINT_PERIOD, new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						frame.repaint();
					}
				});

				timer.start();
			}
		});
	}
}
