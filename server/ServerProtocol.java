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

package server;

public interface ServerProtocol{

	// NEWHUMAN (name) is sent by a player to the server to add himself to the list
	// The server replies OK or ERROR
	public final static String NEW_HUMAN = "NEWHUMAN";
	public final static int	NEW_HUMAN_INT = 1;

	// GET_HUMAN_LIST is sent by a player to the server to get the list of players
	// The server replies OK (player1) ... (playerN)
	public final static String GET_HUMAN_LIST = "GETHUMANLIST";
	public final static int	GET_HUMAN_LIST_INT = 2;

	// ASK (dest) is sent by a player to ask (dest) to play against him
	public final static String ASK = "ASK";
	public final static int	ASK_INT = 3;

	// REFUSE is sent by the server when a player refuses to play against another
	public final static String REFUSE = "REFUSE";
	public final static int	REFUSE_INT = 4;

	// ACCEPT is sent by the server when a player accepts to play against another
	public final static String ACCEPT = "ACCEPT";
	public final static int	ACCEPT_INT = 5;

	// LOGOFF is sent by the client to it is logging off server replies LOGOFF too
	public final static String LOGOFF = "LOGOFF";
	public final static int	LOGOFF_INT = 6;

	// HUMANLIST h1 h2... is sent by the server as a reply to GETHUMANLIST
	public final static String HUMANLIST = "HUMANLIST";
	public final static int	HUMANLIST_INT = 7;

	public final static String REQUESTFROM = "REQUESTFROM";
	public final static int	REQUESTFROM_INT = 8;

	public final static String TEAM = "TEAM";
	public final static int	TEAM_INT = 9;

	public final static String INITMATCHWITH = "INITMATCHWITH";
	public final static int	INITMATCHWITH_INT = 10;

	public final static String STARTMATCH = "STARTMATCH";
	public final static int	STARTMATCH_INT = 11;

	public final static String GET_INIT_TEAM = "GET_INIT_TEAM";
	public final static int	GET_INIT_TEAM_INT = 12;

	public final static String SETTEAM = "SETTEAM";
	public final static int SETTEAM_INT = 13;

	public final static String GETSTATS = "GETSTATS";
	public final static int	GETSTATS_INT = 14;

	public final static String STATS = "STATS";
	public final static int	STATS_INT = 15;

	public final static String MY_TEAM_INIT = "MY_TEAM_INIT";
	public final static int	MY_TEAM_INIT_INT = 16;

	public final static String OPPONENT_INIT = "OPPONENT_INIT";
	public final static int	OPPONENT_INIT_INT = 17;

	public final static String MY_TEAM_UPDATE = "MY_TEAM_UPDATE";
	public final static int	MY_TEAM_UPDATE_INT = 18;

	public final static String OPPONENT_UPDATE = "OPPONENT_UPDATE";
	public final static int	OPPONENT_UPDATE_INT = 19;

	public final static String BALL_UPDATE = "BALL_UPDATE";
	public final static int	BALL_UPDATE_INT = 20;

	/** Command used to ping the player */
	public final static String PING = "PING";
	public final static int	PING_INT = 21;

	/** Command used to synchronized player's timer with the server's */
	public final static String SYNC = "SYNC";
	public final static int SYNC_INT = 22;

	public final static String PLAYER_SHOOTS = "PLAYER_SHOOTS";
	public final static int	PLAYER_SHOOTS_INT = 23;

	public final static String PLAYER_RUNS = "PLAYER_RUNS";
	public final static int	PLAYER_RUNS_INT = 24;

	public final static String PLAYER_STOPS = "PLAYER_STOPS";
	public final static int	PLAYER_STOPS_INT = 25;

	public final static String PLAYER_TURNS = "PLAYER_TURNS";
	public final static int	PLAYER_TURNS_INT = 26;

	public final static String PLAYER_DRIBBLES = "PLAYER_DRIBBLES";
	public final static int	PLAYER_DRIBBLES_INT = 27;

	public final static String BALL_SHOOTED = "BALL_SHOOTED";
	public final static int	BALL_SHOOTED_INT = 28;

	public final static String CHAT_MESSAGE = "CHAT_MESSAGE";
	public final static int CHAT_MESSAGE_INT = 33;

	// Used by HTTP tunneling to wait for a new message
	public final static String WAIT = "WAIT";
	public final static int	WAIT_INT = 34;

	// Used by HTTP tunneling
	public final static String SESSION = "SESSION";
	public final static int SESSION_INT = 35;

	// ex: MATCH T1 T2 SCORE1 SCORE2 1
	public final static String MATCH = "MATCH";
	public final static int	MATCH_INT = 13;

	public final static String OK = "OK";
	public final static int	OK_INT = 200;

	public final static String ERROR = "ERROR";
	public final static int	ERROR_INT = 400;
	public final static String ERROR_NOT_ENOUGH_ARGS = "Not enough arguments" ;
	public final static int ERROR_NOT_ENOUGH_ARGS_INT = 401;
	public final static String ERROR_INVALID_COMMAND = "Invalid command" ;
	public final static int	 ERROR_INVALID_COMMAND_INT = 402;
	public final static String ERROR_UNKNOWN_COMMAND = "Unknown command" ;
	public final static int	 ERROR_UNKNOWN_COMMAND_INT = 403;

}

