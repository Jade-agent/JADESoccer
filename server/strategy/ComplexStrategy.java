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

import server.Message;
import server.player.Action;

/*
With this strategy the players first verify if there is any message.
If not they try to take the ball
*/

public class ComplexStrategy implements Strategy {
	
	private static final long serialVersionUID = 1L;
	private Action lastAction;
	private boolean alreadySent; // if a message was already sent

	@Override
	public synchronized Action chooseAction(Message msg, boolean isHomeTeam, int numPlayer) {
		Action a = new Action();
		if (msg != null) {
			if (msg.equals(Message.BALL_IS_MINE)) {
				// Some team-mate wants to take the ball first
				// This player agrees, leave the ball to him, and run towards the gol
				// un compagno pensa di prendere la palla per primo
				a.setType(Action.REACH_POSITION);
				int args[] = { 650, 400 }; // This is the goal position, to be checked with the gui
				a.setArgs(args);
				// alreadySent = false;
			}
		} else {
			// if there was not any message, then this player tries to take the ball for itself
			if(!alreadySent){
				a.setType(Action.MESSAGE);
				a.setMessage(Message.BALL_IS_MINE); // !
				alreadySent = true;
			}
			else{
				a.setType(Action.REACH_POSITION);
				int args[] = { 650, 20 }; 
				a.setArgs(args);
				alreadySent = false;
			}
		}
		return a;
	}

	public Strategy cloneStrategy() {
		return new ComplexStrategy();
	}
	
	public boolean hasToClone(){
		return true;
	}

}

