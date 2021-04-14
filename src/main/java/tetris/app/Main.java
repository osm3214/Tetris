package tetris.app;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.SwingUtilities;
import javax.swing.Timer;

public class Main {

	private static final int PAINT_PERIOD = (int) (0.05*1000);

	public static void main(String[] args) {
		final App frame = new App();
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