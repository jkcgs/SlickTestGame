package com.makzk.games.util;

public enum Direction {
	NONE,
	NORTH,
	SOUTH,
	WEST,
	EAST,
	NORTH_WEST,
	NORTH_EAST,
	SOUTH_EAST,
	SOUTH_WEST;

	@Override
	public String toString() {
		switch (this) {
			case NONE:	 return "None";
			case NORTH:	 return "North";
			case SOUTH:	 return "South";
			case WEST:	 return "West";
			case EAST:	 return "East";
			case NORTH_WEST:	 return "North-West";
			case NORTH_EAST:	 return "North-East";
			case SOUTH_WEST:	 return "South-West";
			case SOUTH_EAST:	 return "South-East";
			default: throw new IllegalArgumentException();
		}
	}
}
