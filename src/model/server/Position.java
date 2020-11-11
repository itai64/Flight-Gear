package model.server;

public class Position {

	private int x, y;

	public Position(int x, int y) {
		this.x = x;
		this.y = y;
	};

	public Position() {
	};
	
	public Position(String str) {
		String[] arr= str.split(",");
		x = (int)Double.parseDouble(arr[0]);
		y = (int)Double.parseDouble(arr[1]);
	}

	@Override
	public boolean equals(Object position) {
		Position p = (Position) position;
		return ((this.x == p.x) && (this.y == p.y));

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
	
	@Override
	public String toString() {
		return ( "(" + x + "," + y + ")" );
	}
}
