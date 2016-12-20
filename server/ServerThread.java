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

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

import GUI.PES;

/**
* ServerThread is launched by the server when it received a new connection
*/

public class ServerThread extends Thread implements ServerProtocol{
	private Socket socket;
	private PESServer server;
	private static Hashtable<String, Integer> protocolTable = null;
	private ServerHuman human;
	private String currentOpponentName;
	private int clientMode;

	public ServerThread (Socket socket, PESServer server){
		this.socket = socket;
		this.server = server;
		this.human = new ServerHuman (socket);
		this.currentOpponentName = null;
		//this.clientMode = Mode.MODE_NONE;

		if(this.protocolTable==null){
			this.protocolTable = new Hashtable<String, Integer>();
			this.protocolTable.put (NEW_HUMAN, new Integer(NEW_HUMAN_INT));
			this.protocolTable.put (GET_HUMAN_LIST, new Integer(GET_HUMAN_LIST_INT));
			this.protocolTable.put (LOGOFF, new Integer(LOGOFF_INT));
			this.protocolTable.put (ASK, new Integer(ASK_INT));
			this.protocolTable.put (ACCEPT, new Integer(ACCEPT_INT));
			this.protocolTable.put (REFUSE, new Integer(REFUSE_INT));
			this.protocolTable.put (TEAM, new Integer(TEAM_INT));
			this.protocolTable.put (STARTMATCH, new Integer(STARTMATCH_INT));
			this.protocolTable.put (GETSTATS, new Integer(GETSTATS_INT));
			this.protocolTable.put (MY_TEAM_INIT, new Integer(MY_TEAM_INIT_INT));
			this.protocolTable.put (MY_TEAM_UPDATE, new Integer(MY_TEAM_UPDATE_INT));
			this.protocolTable.put (PING, new Integer(PING_INT));
			this.protocolTable.put (PLAYER_SHOOTS, new Integer(PLAYER_SHOOTS_INT));
			this.protocolTable.put (PLAYER_TURNS, new Integer(PLAYER_TURNS_INT));
			this.protocolTable.put (PLAYER_STOPS, new Integer(PLAYER_STOPS_INT));
			this.protocolTable.put (PLAYER_RUNS, new Integer(PLAYER_RUNS_INT));
			this.protocolTable.put (PLAYER_DRIBBLES, new Integer(PLAYER_DRIBBLES_INT));
			this.protocolTable.put (BALL_SHOOTED, new Integer(BALL_SHOOTED_INT));
			this.protocolTable.put (CHAT_MESSAGE, new Integer(CHAT_MESSAGE_INT));
			this.protocolTable.put (WAIT, new Integer(WAIT_INT));
		}
	}


	public void run(){
		// Here we start the communication with the remote host
		try{
			// To handle HTTP tunneling
			// Comment and recompile to save CPU
			String httpSession = null;
			String remoteInetAdress = this.socket.getInetAddress().getHostAddress();
			System.out.println(remoteInetAdress+"> login");

			PrintWriter out = new PrintWriter(this.socket.getOutputStream(), true);
			BufferedReader in = new BufferedReader( new InputStreamReader(this.socket.getInputStream()));
			String inputLine;

			while ((inputLine = in.readLine()) != null){
				//TODO:
				System.out.println ("> "+inputLine);
				try{
					// To handle HTTP tunneling
					// Comment and recompile to save CPU
					if (inputLine.startsWith("GET /")){
						inputLine = decodeURL(inputLine.substring(5, inputLine.indexOf("HTTP/")-1));
						int interroMarkIndex = inputLine.indexOf("?");
						if(interroMarkIndex!=-1){
							httpSession = inputLine.substring(interroMarkIndex+7);
							System.err.println("The session is = <"+httpSession+">");
							inputLine = inputLine.substring(0, interroMarkIndex-1);
						}
						System.out.println(inputLine);
						//_clientMode = Mode.MODE_APPLICATION_PROXY;
					}
					if (inputLine.indexOf(":")==-1){
						StringTokenizer tokenizer = new StringTokenizer (inputLine);
						if(!tokenizer.hasMoreTokens()) continue;
						String command = tokenizer.nextToken();
						switch ( ((Integer)this.protocolTable.get(command)).intValue()){
							case WAIT_INT:
								break;
						case GET_HUMAN_LIST_INT:{
							out.print (HUMANLIST);
							for (Enumeration enu = this.server.getHumanNames(); enu.hasMoreElements();){
								out.print(" "+enu.nextElement());
							}
							out.println("");
							break;
						}
						case NEW_HUMAN_INT:{
							this.human.setName (tokenizer.nextToken());
							if(this.server.addHuman (this.human)){
								out.println(PING+" "+System.currentTimeMillis());
							}
							else{
								out.println(ERROR);
								this.human.setName (null);
							}
							break;
						}
						case PING_INT:{
							if (tokenizer.hasMoreTokens()){
								// diff is half the time made by the ping to get back here
								long diff = ( System.currentTimeMillis() - new Long (tokenizer.nextToken()).longValue() ) / 2;
								out.println (SYNC+" "+(System.currentTimeMillis()+diff));
							}
							break;
						}
						case LOGOFF_INT:{
							out.println(LOGOFF);
							break;
						}
						case GETSTATS_INT:{
							String humanName = tokenizer.nextToken();
							ServerHuman humanStat = this.server.getHuman(humanName);
							out.println(STATS+" "+humanName+" "+humanStat.getWin()+" "+humanStat.getDraw()+" "+humanStat.getLost()+" "+humanStat.getPoints());
							break;
						}
						case ASK_INT:{
							String humanName = tokenizer.nextToken();
							if (! this.server.getHuman(humanName).isAvailable()){
								out.println (REFUSE+" "+humanName);
							}
							else{
								Socket opponentSocket = this.server.getSocketOf (humanName);
								if (opponentSocket!=null){
									PrintWriter opponentOut = new PrintWriter(opponentSocket.getOutputStream(), true);
									opponentOut.println(REQUESTFROM+" "+this.human.getName());
									this.currentOpponentName = humanName;
								}
								else{
									out.println (REFUSE+" "+humanName);
								}
							}
							break;
						}
						case ACCEPT_INT:{
							String human = tokenizer.nextToken();
							this.currentOpponentName = human;
							Socket opponentSocket = this.server.getSocketOf (human);
							if (opponentSocket!=null){
								// For the moment create an empty game
								ServerMatch match = new ServerMatch ();
								this.server.getHuman (human).setMatch (match);
								this.human.setMatch (match);
								PES.start();
								// ask for teams
								double north = Math.random();
								PrintWriter opponentOut = new PrintWriter(opponentSocket.getOutputStream(), true);
								opponentOut.println(GET_INIT_TEAM+" "+(north>0.5?"1":"0"));
								out.println(GET_INIT_TEAM+" "+(north>0.5?"0":"1"));
							}
							break;
						}
						case REFUSE_INT:{
							String human = tokenizer.nextToken();
							Socket opponentSocket = this.server.getSocketOf (human);
							if (opponentSocket!=null){
								PrintWriter opponentOut = new PrintWriter(opponentSocket.getOutputStream(), true);
								opponentOut.println(REFUSE+" "+this.human.getName());
							}
							break;
						}
						case MY_TEAM_INIT_INT:{
							break;
						}
						case PLAYER_RUNS_INT:
						case PLAYER_STOPS_INT:
						case PLAYER_TURNS_INT:
						case PLAYER_SHOOTS_INT:
						case PLAYER_DRIBBLES_INT:
						case BALL_SHOOTED_INT:
						case CHAT_MESSAGE_INT:{
							Socket opponentSocket = this.server.getSocketOf(this.currentOpponentName);
							if (opponentSocket!=null){
								PrintWriter opponentOut = new PrintWriter(opponentSocket.getOutputStream(), true);
								opponentOut.println(inputLine);
							}
							break;
						}
						default:{
							out.println(ERROR+" "+ERROR_UNKNOWN_COMMAND_INT+" "+ERROR_UNKNOWN_COMMAND+" <<"+inputLine+">>");
							break;
						}
					}
				}
				else{
					System.err.println("ignored: "+inputLine);
				}
			
				}catch (NoSuchElementException e){
					e.printStackTrace();
					out.println(ERROR+" "+ERROR_NOT_ENOUGH_ARGS_INT+" "+ERROR_NOT_ENOUGH_ARGS+" <<"+inputLine+">>");
				}catch (Exception e2){
					//e2.printStackTrace();
					//out.println(ERROR+" "+ERROR_INVALID_COMMAND_INT+" "+ERROR_INVALID_COMMAND+" <<"+inputLine+">>");
				}
	
			}
			if(this.human!=null) this.server.removeHuman (this.human.getName());
			out.close();
			in.close();
			System.out.println(remoteInetAdress+"> logoff");
		}catch (IOException e){
			//e.printStackTrace();
			if(this.human!=null) this.server.removeHuman (this.human.getName());
			System.err.println("Connection closed with "+this.human.getName());
		}
	}

	private String decodeURL (String url){
		StringBuffer buf = new StringBuffer();
		int urlLength = url.length();
		for (int i = 0; i < urlLength ; i++){
			char c = url.charAt(i);
			if (c == '%'){
				c = (char)((Character.digit(url.charAt(++i), 16) << 4) | 
				Character.digit(url.charAt(++i), 16));
			}
			buf.append(c);
		}
		return buf.toString ();
	}
}
