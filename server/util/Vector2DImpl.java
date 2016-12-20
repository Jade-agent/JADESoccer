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

public class Vector2DImpl implements Cloneable, Vector2D {
	private int x;
	private int y;

	public Vector2DImpl(int x, int y) {
			this.x = x;
			this.y = y;
	}


	public Object clone() {
		Vector2DImpl cloneVector;
		try {
			cloneVector = (Vector2DImpl) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			cloneVector = null;
		}
		return cloneVector;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public Vector2DImpl subtract(Vector2DImpl v) {
		return new Vector2DImpl(x - v.getX(), y - v.getY());
	}

	public Vector2DImpl add(Vector2DImpl v) {
		return new Vector2DImpl(x + v.getX(), y + v.getY());
	}

	public Vector2DImpl multiply(int f) {
		return new Vector2DImpl(x * f, y * f);
	}

	public double multiply(Vector2DImpl v) {
		return x * v.getX() + y * v.getY();
	}

	public double polarRadius() {
		return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
	}

	public double polarAngle() {
		double angle;
		if ((y == 0) && (x == 0)) {
			angle = 0.0D;
		} else {
			angle = Math.toDegrees(Math.atan2(y, x));
		}

		return angle;

	}

	public Vector2DImpl normalize(double modulusMax) {
		double currentModulus = polarRadius();
		if (currentModulus > modulusMax) {
			this.x *= (modulusMax / currentModulus);
			this.y *= (modulusMax / currentModulus);
		}
		return this;
	}

	public double distanceTo(int x, int y) {
		return (new Vector2DImpl(x, y)).subtract(this).polarRadius();
	}

	public double distanceTo(Vector2DImpl v) {
		return v.subtract(this).polarRadius();
	}

	public double directionOf(int x, int y) {
		return (new Vector2DImpl(x, y)).subtract(this).polarAngle();
	}

	public double directionOf(Vector2DImpl v) {
		return v.subtract(this).polarAngle();
	}

	public String toString() {
		return String.format("(%g - %g)", x, y);
	}
}

