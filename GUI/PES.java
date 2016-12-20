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
import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class PES extends JFrame {

	private static final long serialVersionUID = 1L;
	private static JPanel scorePanel;
	
	public PES() {
		ServerADT field = new ServerADT();
		add(field);
		scorePanel = new JPanel();
		scorePanel.add(new JLabel("Team1 "+field.getScoreTeam1()+" : "+field.getScoreTeam2()+" Team2"));
		add(scorePanel, BorderLayout.SOUTH);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1450, 800);
		setLocationRelativeTo(null);
		setTitle("Jade Agent Soccer");
		setResizable(false);
		setVisible(true);
    }

	public static void updateScore(){
		scorePanel.removeAll();
		scorePanel.add(new JLabel("Team1 "+ServerADT.scoreTeam1+" : "+ServerADT.scoreTeam2+" Team2"));
		scorePanel.validate();
		scorePanel.repaint();
	}
	
    public static void start() {
        new PES();
    }
    
    public static void main(String[] args){
    	start();
    }
}

