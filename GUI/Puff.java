/*
This file is part of JADESoccer.

JADESoccer is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

JADESoccer is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with JADESoccer.  If not, see <http://www.gnu.org/licenses/>.
*/


//Copyright: (c) Michele Ianni, Pietro Grandinetti

package GUI;
import java.awt.AlphaComposite;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Puff extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;

	Timer timer;
	int x = 1;
	float alpha = 1;

	private static String applause;
	
	File sf;
	AudioFileFormat aff;
	AudioInputStream ais;

	public Puff() {
		timer = new Timer(8, this);
		timer.setInitialDelay(190);
		timer.start();

		applause = "applause.wav";
	}

	public void paint(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;

		RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		rh.put(RenderingHints.KEY_RENDERING,
				RenderingHints.VALUE_RENDER_QUALITY);

		g2d.setRenderingHints(rh);

		Font font = new Font("Dialog", Font.PLAIN, x);
		g2d.setFont(font);

		FontMetrics fm = g2d.getFontMetrics();			
		String s = "Goal";
		Dimension size = getSize();

		int w = (int) size.getWidth();
		int h = (int) size.getHeight();

		int stringWidth = fm.stringWidth(s);

		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
				alpha));

		g2d.drawString(s, (w - stringWidth) / 2, h / 2);
	}

	public void goal() {

		JFrame frame = new JFrame("Goal");
		frame.add(new Puff());
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setSize(400, 300);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		applause();
		try {
			Thread.sleep(1500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		frame.setVisible(false);
	}

	private void applause() {
		sf = new File(applause);
		
			try {
				aff = AudioSystem.getAudioFileFormat(sf);
			} catch (UnsupportedAudioFileException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				ais = AudioSystem.getAudioInputStream(sf);
			} catch (UnsupportedAudioFileException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			AudioFormat af = aff.getFormat();

			DataLine.Info info = new DataLine.Info(Clip.class, ais.getFormat(),
					((int) ais.getFrameLength() * af.getFrameSize()));

			Clip ol = null;
			try {
				ol = (Clip) AudioSystem.getLine(info);
			} catch (LineUnavailableException e) {
				e.printStackTrace();
			}

			try {
				ol.open(ais);
			} catch (LineUnavailableException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			ol.loop(0);
	}

	public void actionPerformed(ActionEvent e) {
		x += 1;

		if (x > 40)
			alpha -= 0.01;

		if (alpha <= 0.01)
			timer.stop();
		repaint();
	}
	
}

