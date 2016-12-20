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

import java.awt.Image;
import java.awt.Rectangle;
import javax.swing.ImageIcon;

public class Ball {

	private int x, y;
	private Image image;
	boolean visible;

	private final int BOARD_WIDTH1 = 30;
	private final int BOARD_HEIGHT1 = 30;
	private final int BOARD_WIDTH2 = 1400;
	private final int BOARD_HEIGHT2 = 790;
	public static int BALL_SPEED = 2;

	private PlayerGUI.direction actualdirection;

	public Ball(int x, int y, PlayerGUI.direction actualdirection) {

		ImageIcon ii = new ImageIcon(this.getClass().getResource("soccer.gif"));
		image = ii.getImage();
		visible = true;
		this.x = x;
		this.y = y;

		this.actualdirection = actualdirection;
	}

	public Image getImage() {
		return image;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public boolean isVisible() {
		return visible;
	}

	public void move() {
		if (actualdirection == PlayerGUI.direction.STOP) {

		}

		if (actualdirection == PlayerGUI.direction.EAST) {
			x += BALL_SPEED;
			if (!(x > BOARD_WIDTH1 && x < BOARD_WIDTH2))
				stop();
		}

		if (actualdirection == PlayerGUI.direction.WEST) {
			x -= BALL_SPEED;
			if (!(x > BOARD_WIDTH1 && x < BOARD_WIDTH2))
				stop();
		}

		if (actualdirection == PlayerGUI.direction.NORTH) {
			y -= BALL_SPEED;
			if (!(y > BOARD_HEIGHT1 && y < BOARD_HEIGHT2))
				stop();
		}

		if (actualdirection == PlayerGUI.direction.SOUTH) {
			y += BALL_SPEED;
			if (!(y > BOARD_HEIGHT1 && y < BOARD_HEIGHT2))
				stop();
		}
	}

	public Rectangle getBounds() {
		return new Rectangle(this.getX(), this.getY(), 12, 12);
	}

	public void stop() {
		actualdirection = PlayerGUI.direction.STOP;
		BALL_SPEED = 4;
	}
	
	public boolean isStopped(){
		return (actualdirection==PlayerGUI.direction.STOP);
	}
}
