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

/*
This class creates the object that is the representation (server-side) of the player
*/

package server;

import java.net.Socket;

public class ServerHuman{
	
	private Socket socket;
	private String name;
	private ServerMatch match;

	// A pointer to my team representation on the server
	//private Team myTeam;

	// Number of wins, draws, losses, total games
	private int win;
	private int draw;
	private int lost;
	private int playedMatches;

	private static final int POINTS_WIN = 3;
	private static final int POINTS_DRAW = 1;
	private static final int POINTS_LOST = 0;

	public ServerHuman (Socket socket){
		this.socket = socket;
		this.name = null;
		this.match = null;
		//this.myTeam = null;
		this.win = 0;
		this.draw = 0;
		this.lost = 0;
		this.playedMatches = 0;
	}

	/*
	public void setTeam (Team team){
		this.myTeam = team;
	}

	public Team getTeam (){
		return this.myTeam;
	}
	*/

	public int getPoints(){
		return this.win*POINTS_WIN + this.draw*POINTS_DRAW + this.lost*POINTS_LOST;
	}

	public int getNumberOfMatches(){
		return this.playedMatches;
	}

	public int getWin(){ 
		return this.win;
	}

	public int getDraw(){
		return this.draw;
	}

	public int getLost(){
		return this.lost;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return this.name;
	}

	public Socket getSocket(){
		return this.socket;
	}

	public ServerMatch getMatch(){
		return this.match;
	}

	public void setMatch (ServerMatch match){
		this.match = match;
	}

	public boolean isAvailable(){
		return (this.match==null);
	}
}

