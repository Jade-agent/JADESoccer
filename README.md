# JADESoccer - A platform to develop artificial, agent-based, intelligence
## Synopsis
This is the description file for JADESoccer. JADESoccer is a software that aims at becoming a tool for developing artificial intelligences by making use of the `agent` technology, and for testing them in a complex environment as a soccer game. The [JADE](http://jade.tilab.com/) technology is elected as framework to implement agent control.

## License
JADESoccer is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.

JADESoccer is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with JADESoccer.  If not, see <http://www.gnu.org/licenses/>.

## Disclaimer
JADESoccer is still at an early stage of development. Priority has been given to include the JADE technology within the framework, and to develop simple test case of intelligence for soccer player, in order to ease the testing and the future development. A lot has to be improved, and even more is missing, in particular GUI's side.
- [ ] Make design of the field flexible (i.e., with respect to screen size, resolution, etc.). Currently no flexibility in this respect is provided
- [ ] Create better painting of the players
- [ ] Implement control for 360 degrees movement (currently only 8 directions are supported)
- [ ] Develop more complex game's strategies
- [ ] Ease the development of strategies for single players, rather than for a team
- [ ] Create other types of actions
- [ ] Create other types of messages between players

## Compatibility with JADE
The current release of JADESoccer has been tested with JADE 4.4.0.

## Installation
Simply download and compile, linking the JADE .jar archive (available at http://jade.tilab.com/).

## How to, for users
We provide an example of runnable file in src/PServer/TestMain.java

## How to, for developers
We hope that users of JADESoccer are interested in developing and testing their own game's strategies, using the platform we are developing. To do so, a few key points to know are the following:
- Every player is created as an object that extends the class `jade.core.Agent`, see src/server/player/Player.java
- To define the player (agent) behavior, one has to override the method `setup`, see src/server/player/Player.java#setup, and the doc @JADE
- The strategy that a team employs must be defined as a class that implement the interface src/server/strategy/Strategy.java. Notice the method `chooseAction`, which return the action that the player wants to execute in the next time step, and that receive an instance of the Message enumeration (it represents messages that player exchange each other in order to decide what to do, see src/server/Message.java.
- All possible actions are categorized into a finite set of types of actions (e.g., `kick`, `turn`, `reach the ball`), see src/server/Action.java
- After a player has decided what to do, according to his own strategy and to the messages received (if any), it communicates his action to the server, which will take care to implement it (also in the GUI). For instance, in the method Player.java#setup, inside the block addBehaviour, we do

	```
	Action action = myStrategy.chooseAction(m, pgui.isHomeTeam(),  myNum);
	```
where the player uses its own strategy to choose its next action, and then

	```
	if (action.getType().equals(Action.KICK))
		pgui.kick();
	```
where pgui is an instance of src/GUI/PlayerGUI.java, that is kept as class field by every Player.java (so every player as its own playerGUI), and communicates directly with the server (see, src/GUI/PlayerGUI.java#kick).

# Contributors
JADESoccer has been entirely developed by Pietro Grandinetti and Michele Ianni.

