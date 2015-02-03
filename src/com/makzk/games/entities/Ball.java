package com.makzk.games.entities;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.util.Log;

import com.makzk.games.util.Direction;

public class Ball extends EntityRect {
	private float speed;

	public Ball(GameContainer gc) {
		super(gc, new Rectangle(0, 0, 10, 10));
		speed = .5f;
		color = Color.white;
	}
	
	public void move(int delta, Pad leftPad, Pad rightPad) {
		super.move(delta);

		if ( collisionInX(leftPad) || collisionInX(rightPad) ) {
			speedX = -speedX;
		}
		if (getY() <= 0 
				|| collisionBox.getMaxY() >= gc.getHeight()
				|| collisionInY(leftPad)
				|| collisionInY(rightPad)) {
			speedY = -speedY;
		}
	}

	public void reset() {
		setX((gc.getWidth() / 2) - (getWidth() / 2));
		setY((gc.getHeight() / 2) - (getHeight() / 2));

		Direction newDir;
		switch((int) (Math.random() * 4)) {
			case 0: newDir = Direction.NORTH_EAST; break;
			case 1: newDir = Direction.NORTH_WEST; break;
			case 2: newDir = Direction.SOUTH_EAST; break;
			default: newDir = Direction.SOUTH_WEST;
		}
		setDirection(newDir);
	}
	
	public void setDirection(Direction dir) {
		switch (dir) {
		case NORTH_WEST:
			speedX = -speed;
			speedY = -speed;
			break;
		case NORTH_EAST:
			speedX = speed;
			speedY = -speed;
			break;
		case SOUTH_WEST:
			speedX = -speed;
			speedY = speed;
			break;
		case SOUTH_EAST:
			speedX = speed;
			speedY = speed;
			break;
		default:
			Log.error("Wrong direction for ball: " + dir.toString());
		}
	}
}
