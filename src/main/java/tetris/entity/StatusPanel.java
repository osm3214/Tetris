package tetris.entity;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Timer;

public class StatusPanel extends JPanel {

	/**
	 *
	 */
	private int numSteps;
	private int numLinesRemoved;
	private int score;
	private String status;

	private JTextField text0, text1, text2, text3, text4, text5, text6, text7;

	static private Color BACKGROUND_COLOR = Color.black;
	static private Color TEXT_COLOR = Color.white;

	private static final long serialVersionUID = 1L;

	public StatusPanel() {
		numSteps = 0;
		numLinesRemoved = 0;
		score = 0;
		status = "";

		setBackground(BACKGROUND_COLOR);
		setLayout(null);

		text0 = new JTextField();
		text0.setBounds(0, 40, 120, 20);
		text0.setForeground(TEXT_COLOR);
		text0.setBackground(BACKGROUND_COLOR);
		text1 = new JTextField();
		text1.setBounds(0, 100, 120, 20);
		text1.setForeground(TEXT_COLOR);
		text1.setBackground(BACKGROUND_COLOR);
		text2 = new JTextField();
		text2.setBounds(0, 160, 120, 20);
		text2.setForeground(TEXT_COLOR);
		text2.setBackground(BACKGROUND_COLOR);

		text3 = new JTextField();
		text3.setBounds(0, 60, 120, 20);
		text3.setForeground(TEXT_COLOR);
		text3.setBackground(BACKGROUND_COLOR);
		text3.setHorizontalAlignment(JTextField.CENTER);
		text4 = new JTextField();
		text4.setBounds(0, 120, 120, 20);
		text4.setForeground(TEXT_COLOR);
		text4.setBackground(BACKGROUND_COLOR);
		text4.setHorizontalAlignment(JTextField.CENTER);
		text5 = new JTextField();
		text5.setBounds(0, 180, 120, 20);
		text5.setForeground(TEXT_COLOR);
		text5.setBackground(BACKGROUND_COLOR);
		text5.setHorizontalAlignment(JTextField.CENTER);

		text6 = new JTextField();
		text6.setBounds(0, 220, 120, 20);
		text6.setForeground(TEXT_COLOR);
		text6.setBackground(BACKGROUND_COLOR);
		text6.setHorizontalAlignment(JTextField.CENTER);

		text7 = new JTextField();
		text7.setBounds(0, 0, 120, 20);
		text7.setForeground(TEXT_COLOR);
		text7.setBackground(BACKGROUND_COLOR);
		text7.setHorizontalAlignment(JTextField.CENTER);

		add(text0);
		add(text1);
		add(text2);
		add(text3);
		add(text4);
		add(text5);
		add(text6);
		add(text7);

		text0.setText("Steps");
		text1.setText("Removed Lines");
		text2.setText("Score");
		text3.setText(Integer.toString(numSteps));
		text4.setText(Integer.toString(numLinesRemoved));
		text5.setText(Integer.toString(score));
		text6.setText(status);
		text7.setText("Status");
	}

	public void blinkStatus(final String status) {
		Timer timer = new Timer(500, new ActionListener() {
			private int count = 0;
			private boolean isDisplayed = true;
			@Override
			public void actionPerformed(ActionEvent e) {
				if (isDisplayed) {
					setStatus("");
					isDisplayed = false;
					count++;
				} else {
					setStatus(status);
					isDisplayed = true;
				}
				if (count > 3) {
					((Timer) e.getSource()).stop();
				}
			}
		});
		timer.start();
	}

	public void setNumSteps(int step) {
		numSteps = step;
		text3.setText(Integer.toString(numSteps));
	}

	public void setNumLinesRemoved(int removedLines) {
		numLinesRemoved = removedLines;
		text4.setText(Integer.toString(numLinesRemoved));
	}

	public void setScore(int score) {
		this.score = score;
		text5.setText(Integer.toString(this.score));
	}

	public void setStatus(String status) {
		this.status = status;
		text6.setText(this.status);
	}
}
