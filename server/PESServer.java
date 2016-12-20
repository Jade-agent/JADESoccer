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
This class defines the back-end part of the server that manages the game
*/

package server;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.io.*;

import server.player.Action;


public class PESServer{
	private static Vector socketV = new Vector();
	private Hashtable humanList;
	private int port;

	private static LinkedList<Action> actionQueue = new LinkedList<Action>();

	public static void addAction(Action a){
		actionQueue.addLast(a);
	}

	private static int sessionIndex = 0;

	public static void main (String[] args){
		if (args.length>=1){
			int port = new Integer(args[0].trim()).intValue();
			new PESServer( port ).runServer();
		}
		else{
			new PESServer( ServerPort.DEFAULT_PORT ).runServer();
		}
	}

	public PESServer (int port)
	{
		this.port = port;
		this.humanList = new Hashtable ();
	}

	public void runServer (){
		ServerSocket serverSocket = null;
		boolean tryAgain = true;
		while (tryAgain){
			try{
				serverSocket = new ServerSocket (port);
				System.out.println("JADESoccer Server running on port "+port);
				tryAgain = false;
			}
			catch (IOException ioe){
				System.out.println("Port "+port+" busy, trying port "+(port+1));
				port++;
			}
		}
		try{
			while(true)
				new ServerThread(serverSocket.accept(), this).start();
		}
		catch (IOException ioe){
			System.err.println("Error while running the server: "+ioe);
		}
	}

	//Create a new session
	public int newSession (){
		return sessionIndex++;
	}

	public ServerHuman getHuman (String name){
		return (ServerHuman) humanList.get(name);
	}

	public boolean removeHuman (String name){
		if(humanList.get(name)!=null){
			humanList.remove (name);
			return true;
		}
		return false;
	}

	public boolean addHuman (ServerHuman human){
		if(humanList.get(human.getName())!=null)
			return false;
		humanList.put(human.getName(), human);
		return true;
	}

	public Socket getSocketOf (String name){
		return ((ServerHuman)(humanList.get(name))).getSocket();
	}

	public Enumeration getHumanNames (){
		return humanList.keys();
	}
}

