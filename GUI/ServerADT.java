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


//Copyright: (c) Pietro Grandinetti, Michele Ianni

package GUI;

import jade.core.AID;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.lang.acl.ACLMessage;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Rectangle;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

import server.util.Vector2DImpl;

import server.Message;
import server.ServerIF;
import server.player.Player;
import server.strategy.Strategy;

import GUI.PlayerGUI.direction;

public class ServerADT extends JPanel implements ActionListener, ServerIF {
	private boolean gameStarted;

	public static final int NUM_PLAYERS = 5;

	protected Strategy s1, s2;
	
	private long timeLastSent;

	// from 0 to NUM they are HomeTeam
	private AgentController[] team1AC = new AgentController[NUM_PLAYERS * 2];

	// private AgentController [] team2AC = new AgentController [NUM_PLAYERS];

	// private final static int PORT_TEAM_1 = 8080;

	// private final static int PORT_TEAM_2 = 9090;

	// the soccer field has the top left corner at coordinate (30,30)
	// Dimensions 690*450
	private static final long serialVersionUID = 1L;

	private Image field;
	private String pitch = "pitchf.gif";

	public static Ball ball;

	private Timer timer;
	//private PlayerGUI player1;

	private int shootlenght;
	private final int MAX_SHOOT_LENGHT = 80;

	private static PlayerGUI[] team1;
	private static PlayerGUI[] team2;

	public static int scoreTeam1;
	public static int scoreTeam2;

	private int[] moduloXSquadra1;
	private int[] moduloYSquadra1;
	private int[] moduloXSquadra2;
	private int[] moduloYSquadra2;

	public ServerADT() {

		setFocusable(true);
		//setBackground(Color.BLACK);
		setDoubleBuffered(true);

		ball = new Ball(730, 395, direction.STOP);

		team1 = new PlayerGUI[5];
		team2 = new PlayerGUI[5];

		scoreTeam1 = 0;
		scoreTeam2 = 0;

		moduloXSquadra1 = new int[] { 100, 100, 200, 300, 300 };
		moduloYSquadra1 = new int[] { 150, 450, 400, 150, 500 };
		moduloXSquadra2 = new int[] { 850, 855, 905, 1000, 1030 };
		moduloYSquadra2 = new int[] { 150, 450, 400, 150, 500 };

		// Parameters: #players (shirt), initial coordinates (x,y) and
		// boolean value that says the team (true -> home team)
		for (int i = 0; i < team1.length; i++) {
			team1[i] = new PlayerGUI(i + 1, moduloXSquadra1[i],
					moduloYSquadra1[i], true);
		}

		for (int i = 0; i < team2.length; i++) {
			team2[i] = new PlayerGUI(i + 1, moduloXSquadra2[i],
					moduloYSquadra2[i], false);
		}

		//player1 = new PlayerGUI(10, (int) (Math.random() * 660 + 30),
				//(int) (Math.random() * 420 + 30), true);

		timer = new Timer(20, this);
		timer.start();
	}

	public void paint(Graphics g) {
		super.paint(g);

		field = new ImageIcon(this.getClass().getResource(pitch)).getImage();

		Graphics2D g2d = (Graphics2D) g;
		g2d.drawImage(field, 0, 0, this);
		for (int i = 0; i < team1.length; i++) {
			g2d.drawImage(team1[i].getImage(), team1[i].getX(),
					team1[i].getY(), this);
		}

		for (int i = 0; i < team2.length; i++) {
			g2d.drawImage(team2[i].getImage(), team2[i].getX(),
					team2[i].getY(), this);
		}
		//g2d.drawImage(player1.getImage(), player1.getX(), player1.getY(), this);

		g2d.drawImage(ball.getImage(), ball.getX(), ball.getY(), this);

		Toolkit.getDefaultToolkit().sync();
		g.dispose();
	}

	public int getScoreTeam1() {
		return scoreTeam1;
	}

	public int getScoreTeam2() {
		return scoreTeam2;
	}

	public void returnToModule() {
		for (int i = 0; i < team1.length; i++) {
			team1[i].setX(moduloXSquadra1[i]);
			team1[i].setY(moduloYSquadra1[i]);
			team2[i].setX(moduloXSquadra2[i]);
			team2[i].setY(moduloYSquadra2[i]);
		}
	}

	public static Vector2DImpl[] getPlayersPosition() {
		Vector2DImpl[] v2d = new Vector2DImpl[2 * team1.length];
		for (int i = 0; i < team1.length; i++) {
			v2d[i] = new Vector2DImpl(team1[i].getX(), team1[i].getY());
			v2d[i + team1.length] = new Vector2DImpl(team2[i].getX(), team2[i]
					.getY());
		}
		return v2d;
	}
	

	public static Vector2DImpl getBallPosition(){
		return new Vector2DImpl(ball.getX(), ball.getY());
	}
	
	
	public void actionPerformed(ActionEvent e) {

		if (ball.isVisible()) {
			if (shootlenght < MAX_SHOOT_LENGHT) {
				if (shootlenght == MAX_SHOOT_LENGHT / 3)
					Ball.BALL_SPEED = 1;
				ball.move();
				shootlenght++;
			} else {
				shootlenght = 0;
				ball.stop();
			}

		} else {
			ball.stop();
		}

		checkCollisions();
		repaint();
	}

	public void checkCollisions() {

		Rectangle r1 = Net.getBounds(true);
		Rectangle r2 = Net.getBounds(false);

		Rectangle rb = ball.getBounds();

		if (r1.intersects(rb) || r2.intersects(rb)) {
			Puff puff = new Puff();
			puff.goal();
			ball.stop();
			try {
				Thread.sleep(3*1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			puff.setVisible(false);
			ball.setPosition(370, 247);
			if (r1.intersects(rb)) {
				scoreTeam2++;
			} else {
				scoreTeam1++;
			}
			
			PES.updateScore();
			returnToModule();
		}
	}

	// This function is called by the players (PlayerGUI)
	public static void kick(int x, int y, direction actualdirection) {
		ball = new Ball(x, y, actualdirection);
	}

	@Override
	public void startGame() {

		if (s1 == null || s2 == null)
			return;
		PES.start();

		Runtime rt = Runtime.instance();
		Profile p = new ProfileImpl();
		p.setParameter(ProfileImpl.LOCAL_HOST, "127.0.0.1");
		ContainerController cc = rt.createMainContainer(p); // oppure
		// mainCointainer?
		// Profile p1 = new ProfileImpl();
		// p1.setParameter(ProfileImpl.LOCAL_PORT, ""+PORT_TEAM_2);
		// ContainerController cc1 = rt.createAgentContainer(p);

		Object toTeam1[];
		// Object toTeam2 [] ;

		// create and start the agents
		try {
			for (int i = 0; i < team1AC.length; i++) {

				// Strategy, #player, PlayerGUI and pointer to the server - the input for the players
				if (i < 5) {//home team
					toTeam1 = new Object[] {
							s1.hasToClone() ? s1.cloneStrategy() : s1,
							i, team1[i], this };
				} else { //team in trasferta
					toTeam1 = new Object[] { 
							s2.hasToClone() ? s2.cloneStrategy() : s2,
							i-5, team2[i - 5], this };
				}
				// toTeam2 = new Object[]{s2,i, team2[i], this};

				team1AC[i] = cc.createNewAgent("PES" + i,
						"server.player.Player", toTeam1);

				// team2AC[i] = cc1.createNewAgent("inProcess",
				// "server.player.Player", toTeam2);

				team1AC[i].start();
				// team2AC[i].start();
			}
		} catch (StaleProxyException e) {
			e.printStackTrace();
		}

		gameStarted = true;
	}

	public boolean isGameStarted() {
		return gameStarted;
	}

	@Override
	public void createTeam1( Strategy strategy) {
		s1 = strategy;
	}

	@Override
	public void createTeam2(Strategy strategy) {
		s2 = strategy;
	}
	

	public synchronized void sendMessage(Message m, boolean isHomeTeam, int numPlayer)
			throws StaleProxyException{
	
		// one player sends to all team-mate
		System.out.println("NUMERO MAGLIA "+numPlayer+ " "+isHomeTeam);//Log
		
		AID aid = new AID();
		
		int toEnd = isHomeTeam ? NUM_PLAYERS-1 : NUM_PLAYERS; 
		
		ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
		msg.setContent(m.toString());
		
		while(toEnd!=-1 && toEnd!=2*NUM_PLAYERS){
			aid.setName(team1AC[isHomeTeam ? toEnd-- : toEnd++].getName()); 
			if((toEnd!=(numPlayer+1) && toEnd!= (numPlayer+1+NUM_PLAYERS)))
					msg.addReceiver(aid);
		}
	
		team1AC[isHomeTeam ? numPlayer : numPlayer + NUM_PLAYERS].
		putO2AObject(msg, AgentController.ASYNC);
		
		//see below
		
	}
	
	public synchronized void sendMessage(Message m, boolean isHomeTeam, int numPlayer,
			int receiver) throws StaleProxyException{
		// one player sends to one receiver
		ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
		msg.setContent(m.toString());
		
		AID aid = new AID();
		aid.setName(team1AC[isHomeTeam ? receiver : receiver + NUM_PLAYERS].getName());
		
		msg.addReceiver(aid);
		team1AC[isHomeTeam ? numPlayer : numPlayer + NUM_PLAYERS].
		putO2AObject(msg, AgentController.ASYNC);
	}

}

