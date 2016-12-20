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

/*
This class defines the possible actions for a JADESoccer player
*/
package server.player;

import server.Message;

public class Action{

	// Notice that there are four different turns, which are fixed and not parameters.
	// This is because the parameters are the standard coordinates of the point to be reached in REACH_POSITION
	// Lower case notation for all actions' string
	public static final String KICK = "kick", DASH = "dash",
			TURN_NORTH = "turn_north", TURN_SOUTH = "turn_south",
			TURN_EAST = "turn_east", TURN_WEST = "turn_west",
			MESSAGE = "message", REACH_POSITION = "reach_position",
			REACH_BALL = "reach_ball",
			REACH_BALL_AND_KICK = "reach_ball_and_kick",
			PASS_NORTH = "pass_north", PASS_SOUTH = "pass_south",
			PASS_EAST = "pass_east", PASS_WEST = "pass_west";

	// The args are:
	// 0 - #player
	// 1,2 - coordinate of the position to reach, if any
	private int args[]; 

	private String type;
	
	private Message msg;

	public Action(){
		setArgs(new int[3]);
	}

	public String getType(){
		return type;
	}

	public void setType(String type) {
		if (!(type.equals(DASH) || (type.equals(KICK))
				|| (type.equals(TURN_NORTH)) || (type.equals(TURN_SOUTH))
				|| (type.equals(TURN_EAST)) || (type.equals(TURN_WEST))
				|| (type.equals(MESSAGE)) || (type.equals(REACH_POSITION))
				|| (type.equals(REACH_BALL))
				|| (type.equals(REACH_BALL_AND_KICK))
				|| (type.equals(PASS_NORTH)) || (type.equals(PASS_SOUTH))
				|| (type.equals(PASS_EAST)) || (type.equals(PASS_WEST))))
			return;
		this.type = type;
	}

	public int[] getArgs(){
		return args;
	}

	public void setArgs(int args[]){
		this.args = args;
	}
	
	public void setMessage(Message msg){
		this.msg = msg;
	}
	
	public Message getMessage(){
		return msg;
	}
}

