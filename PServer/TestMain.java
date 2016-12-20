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

package PServer;

import GUI.Puff;
import GUI.ServerADT;
import server.strategy.ComplexStrategy;
import server.strategy.ComplexStrategy2;
import server.strategy.TrialStrategy;


public class TestMain{
	public static void main(String[] args){
		
		ServerADT s = new ServerADT();
		// Create two teams with two strategies and start two games
		s.createTeam1(new TrialStrategy());
		s.createTeam2(new ComplexStrategy2());
		try{
			s.startGame();
		}catch(Exception e){
			e.printStackTrace();
			System.exit(-1);
		}
		
	}
}
