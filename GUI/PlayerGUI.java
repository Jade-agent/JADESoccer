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
import java.awt.Image;
import java.awt.Rectangle;
import javax.swing.ImageIcon;
import server.util.Vector2DImpl;

public class PlayerGUI{

	private String playerEast = "player_east.gif";
	private String playerNorth = "player_north.gif";
	private String playerSouth = "player_south.gif";
	private String playerWest = "player_west.gif";

	private String playerEastRed = "player_east_red.gif";
	private String playerNorthRed = "player_north_red.gif";
	private String playerSouthRed = "player_south_red.gif";
	private String playerWestRed = "player_west_red.gif";
	
	private int shirt;

	private int dx;
	private int dy;
	private int x;
	private int y;
	private Image image;

	private final int PLAYER_SIZE = 20;

	private ImageIcon iie, iin, iis, iiw;

	public static enum direction {
		STOP, NORTH, EAST, SOUTH, WEST
	};

	private direction actualdirection;
	private boolean isTeam1;
	

	public PlayerGUI(int shirt, int x, int y, boolean isTeam1) {

		this.shirt = shirt;
		this.x = x;
		this.y = y;
		this.isTeam1 = isTeam1;

		if (isTeam1) {
			iie = new ImageIcon(this.getClass().getResource(playerEast));
			iin = new ImageIcon(this.getClass().getResource(playerNorth));
			iis = new ImageIcon(this.getClass().getResource(playerSouth));
			iiw = new ImageIcon(this.getClass().getResource(playerWest));
		} else {
			iie = new ImageIcon(this.getClass().getResource(playerEastRed));
			iin = new ImageIcon(this.getClass().getResource(playerNorthRed));
			iis = new ImageIcon(this.getClass().getResource(playerSouthRed));
			iiw = new ImageIcon(this.getClass().getResource(playerWestRed));

		}
		if (isTeam1) {
			image = iie.getImage();
			actualdirection = direction.EAST;
		} else {
			image = iiw.getImage();
			actualdirection = direction.WEST;
		}
	}

	public boolean isHomeTeam() {
		return isTeam1;
	}

	public void move() {
		x += dx;
		y += dy;
	}

	public int getShirt() {
		return shirt;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
	
	public void setX(int x){
		this.x=x;
	}
	
	public void setY(int y){
		this.y=y;
	}

	public Image getImage() {
		return image;
	}

	public Ball getBall() {
		return ServerADT.ball;
	}

	public Rectangle getBounds() {
		return new Rectangle(x, y);
	}

	public void turn(PlayerGUI.direction direction){
		actualdirection=direction;
		switch(direction){
		case NORTH: image = iin.getImage();
		case SOUTH: image = iis.getImage();
		case EAST: image = iie.getImage();
		case WEST: image = iiw.getImage();
		default: {}
		}
	}


	public void kick() {
		if (this.canKick()) {
			ServerADT.kick(x + PLAYER_SIZE, y + PLAYER_SIZE / 2, actualdirection);
		}
	}
	
	public void reachPosition(int xp, int yp) {
		dx = (xp > getX() - 2) ? 1 : (xp == getX() - 2) ? 0 : -1;
		dy = (yp > getY() + 10) ? 1 : (yp == getY() + 10) ? 0 : -1;
		move();
	}

	public void reachBall() {
		reachPosition(ServerADT.ball.getX(), ServerADT.ball.getY());
	}

	public boolean canKick() {
		Rectangle r1 = new Rectangle(this.getX(), this.getY(), 30, 40);

		Ball ball = this.getBall();
		if(!ball.isStopped()) return false;
		Rectangle r2 = new Rectangle(ball.getX(), ball.getY(), 12, 12);

		if (r1.intersects(r2)) {
			return true;
		} else {
			return false;
		}
	}

	public void reachBallAndKick() {
		reachBall();
		if(canKick()) kick();
	}
	
	public Vector2DImpl getCoordinatesVector(){
		return new Vector2DImpl(getX(), getY());
	}
	
}

