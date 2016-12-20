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

import server.util.Vector2DImpl;
import GUI.ServerADT;
import server.Message;
import server.player.Action;

public class TrialStrategy implements Strategy{

	private static final long serialVersionUID = 1L;
	
	private final int MINIMUM_DISTANCE_PLAYERS = 5;
	
	public boolean hasToClone(){
		return false;
	}

	@Override
	public Action chooseAction(Message msg, boolean isHomeTeam, int numPlayer) {
		
		Action a = new Action();

		int n=((int) Math.random())*3;
		if(n<1)
		a.setType("reach_ball_and_kick");
		if(n==1)
			a.setType("pass_north");
		if(n==2)
			a.setType("pass_south");
		return a;
	}
	
	public Strategy cloneStrategy(){
		throw new UnsupportedOperationException();
	}

}

