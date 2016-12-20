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
This class define the player object within the soccer.
*/
package server.player;

import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import jade.wrapper.StaleProxyException;
import server.Message;
import server.strategy.Strategy;
import GUI.ServerADT;
import GUI.PlayerGUI;

public class Player extends jade.core.Agent {

	private static final long serialVersionUID = 1L;

	// Frequency of the action of this player (millisec)
	public final static int STEP_TIME = 50;

	// Max time to wait for a message from other players (or the field)
	private final int MAX_WAIT = 2 * STEP_TIME;

	// The strategy of this player
	private Strategy myStrategy;

	// A numeric identifier for this player
	private int myNum;

	// A pointer to the GUI of this player
	private PlayerGUI pgui;

	// A pointer to the server that manages the game
	private ServerADT server;

	/*
	Override the setup method from jade.core.Agent in order to set up the behavior of this player
	*/
	protected void setup() {
		
		// Initialize the fields of this object using the parameters passed to the jade object
		// We assume that every cast that follows is possible (otherwise there will be an exception)
		setEnabledO2ACommunication(true, 0);
		myStrategy = (Strategy) getArguments()[0];
		myNum = (Integer) getArguments()[1];
		pgui = (PlayerGUI) getArguments()[2];
		server = (ServerADT) getArguments()[3];

		// Specify the behavior for the player
		// User the TickerBehaviour from JADE (which execute the method onTick, at every 'tick')

		addBehaviour(new TickerBehaviour(this, STEP_TIME) {
			private static final long serialVersionUID = 1L;
			@Override
			protected void onTick() {
				if (!server.isGameStarted())
					return;

				// The player waits for a possible message from a teamate
				ACLMessage msg = blockingReceive(MAX_WAIT);
				Message m = null; // A wrapper of ACLMessage 
				if (msg != null){ 
					System.out.println("PLAYER " + getName() + " RECEIVED MESS");
					String content = msg.getContent();
					// check if received 'Ball is mine' from someone else
					if (content.equals(Message.BALL_IS_MINE.toString()))
						m = Message.BALL_IS_MINE;
				}

				// If it did not receive anything then m will be NULL
				// The method choose action has to handle this.
				Action action = myStrategy.chooseAction(m, pgui.isHomeTeam(),  myNum);
				
				// NOTICE: The action is executed directly from the player
				if (action.getType().equals(Action.KICK))
					pgui.kick();
				else if (action.getType().equals(Action.TURN_NORTH))
					pgui.turn(PlayerGUI.direction.NORTH);
				else if (action.getType().equals(Action.TURN_SOUTH))
					pgui.turn(PlayerGUI.direction.SOUTH);
				else if (action.getType().equals(Action.TURN_EAST))
					pgui.turn(PlayerGUI.direction.EAST);
				else if (action.getType().equals(Action.TURN_WEST))
					pgui.turn(PlayerGUI.direction.WEST);
				else if (action.getType().equals(Action.REACH_POSITION)){
					int[] args = action.getArgs();
					pgui.reachPosition(args[1], args[2]);
				}
				else if (action.getType().equals(Action.REACH_BALL))
					pgui.reachBall();
				else if (action.getType().equals(Action.REACH_BALL_AND_KICK))
					pgui.reachBallAndKick();
				else if (action.getType().equals(Action.PASS_NORTH)){
					pgui.turn(PlayerGUI.direction.NORTH);
					pgui.kick();
				}
				else if (action.getType().equals(Action.PASS_SOUTH)){
					pgui.turn(PlayerGUI.direction.SOUTH);
					pgui.kick();
				}
				else if (action.getType().equals(Action.PASS_EAST)){
					pgui.turn(PlayerGUI.direction.EAST);
					pgui.kick();
				}
				else if (action.getType().equals(Action.PASS_WEST)){
					pgui.turn(PlayerGUI.direction.WEST);
					pgui.kick();
				}
				else if (action.getType().equals(Action.MESSAGE)){
					// In this case the player must send its message
					try{
						System.out.println("PLAYER " + getName() + " SENDING MESS");
						server.sendMessage(action.getMessage(), pgui.isHomeTeam(), myNum);
					}catch (StaleProxyException e){
						System.out.println("PLAYER " + getName() + " EXCEPTION");
					}

				}

				// Check if there is something in the queue to be read
				Object obj = getO2AObject();
				if (obj != null) {
					System.out.println("PLAYER " + getName()+ " FOUND OBJ IN QUEUE");
					// Then it must be a message to sent to the other players
					ACLMessage aclmsg = (ACLMessage) obj;
					System.out.println(aclmsg.getContent());
					send(aclmsg);
				}
			}
		});
	}

}

