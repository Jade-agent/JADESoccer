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

package server.strategy;

import java.awt.Rectangle;
import server.util.Vector2DImpl;
import GUI.ServerADT;
import server.Message;
import server.player.Action;

@SuppressWarnings("serial")
public class ComplexStrategy2 implements Strategy {
	
	private final int DISTANCE_FOR_REACH_BALL = 5;
	// If the ball is at shorter distance, then the player with this strategy leaves its position and go after the ball

	@Override
	public Action chooseAction(Message msg, boolean isHomeTeam, int numPlayer) {
		Action a = new Action();
		Vector2DImpl[] v = ServerADT.getPlayersPosition();
		// (0,0) is top left
		Vector2DImpl ball = ServerADT.getBallPosition();
		Rectangle r1 = new Rectangle(v[numPlayer].getX()-30, v[numPlayer].getY()-30, 30, 30);
		Rectangle r2 = new Rectangle(ball.getX()-5, ball.getY()-5, 5, 5);
		
		if (r1.intersects(r2)) {
			//System.out.println("intersect"+numPlayer);
			double x = Math.random();
			if(x<0.3)
				a.setType(Action.KICK);
			else if (x<0.6)
				a.setType(Action.PASS_NORTH);
			else a.setType(Action.PASS_SOUTH);
			//a.setType(Action.REACH_BALL_AND_KICK);
			return a;
		}
		/*
		if (Math.abs(ball.distanceTo(v[numPlayer]))<DISTANCE_FOR_REACH_BALL){
				a.setType(Action.REACH_BALL_AND_KICK);
				return a;
		}
		*/
		
		boolean nearest = true; // is it the closes to the ball?
		for (int i = 0; i<v.length && nearest; i++){
			if (i!= numPlayer){
				if (Math.abs(v[i].distanceTo(ball))<Math.abs(v[numPlayer].distanceTo(ball)))
					nearest = false;
			}
		}
		if(nearest){
			//System.out.println("NEAREST"+numPlayer);
			a.setType(Action.REACH_BALL_AND_KICK);
			return a;
		}
		if (numPlayer == 0) { // is the goal keeper
			a.setType(Action.REACH_POSITION);
			int x = isHomeTeam ? 35 : 655;
			int y = 225;
			a.setArgs(new int[] { numPlayer, x, y });
			return a;
		} 
		if (numPlayer == 2) { // the central mid-fielder
				a.setType(Action.REACH_BALL_AND_KICK);
				return a;
		} 
		if(numPlayer == 1){ //pivot :)
			a.setType(Action.REACH_POSITION);
			int x = isHomeTeam? 450 : 250;
			int y =190;
			a.setArgs(new int[] {numPlayer, x,y});
			return a;
		}
		// the other players are on the sides
		a.setType(Action.REACH_POSITION);
		if(numPlayer == 4)
			a.setArgs(new int[] { numPlayer, 345, 75 }); 
		else a.setArgs(new int []{numPlayer, 345, 375});
	
		return a;
	}

	@Override
	public boolean hasToClone() {
		return true;
	}

	@Override
	public Strategy cloneStrategy() {
		return new ComplexStrategy2();
	}

}
