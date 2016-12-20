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


package server.util;

public class NullVectorException extends NullPointerException{
	private static final long serialVersionUID = 1L;

	public NullVectorException(){
	}

	public NullVectorException(String message){
		super(message);
	}

}
