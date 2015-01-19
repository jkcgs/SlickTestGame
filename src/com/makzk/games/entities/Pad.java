package com.makzk.games.entities;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.util.Log;

import com.makzk.games.util.Direction;

public class Pad extends EntityRect {
	private float movementSpeed = 1f;
	private int controlUp = Input.KEY_UP;
	private int controlDown = Input.KEY_DOWN;

	public Pad(GameContainer gc) {
		super(gc, new Rectangle(0, 0, 10, 100));
	}
	
	public void setDirection(Direction dir) {
		switch(dir) {
		case NORTH:
			this.speedY = -movementSpeed;
			break;
		case SOUTH:
			this.speedY = movementSpeed;
			break;
		case NONE:
			this.speedY = 0;
			break;
		default:
			Log.error("Wrong direction for Pad entity: " + dir.toString());	
		}
	}
	
	public void move(Input in, int delta) {
		super.move(delta);
		if(in.isKeyDown(controlUp)) {
			setDirection(Direction.NORTH);
		} else if(in.isKeyDown(controlDown)) {
			setDirection(Direction.SOUTH);
		} else {
			setDirection(Direction.NONE);
		}
	}

	public void autoMove(Ball ball) {
		if(ball.getRect().getY() < rect.getY()) {
			rect.setY(rect.getY() - movementSpeed);
		}
		
		if(ball.getRect().getY() > (rect.getY() + rect.getHeight())) {
			rect.setY(rect.getY() + movementSpeed);
		}
	}
	
	public int getControlUp() {
		return controlUp;
	}

	public void setControlUp(int controlUp) {
		this.controlUp = controlUp;
	}

	public int getControlDown() {
		return controlDown;
	}

	public void setControlDown(int controlDown) {
		this.controlDown = controlDown;
	}

	public float getMovementSpeed() {
		return movementSpeed;
	}

	public void setMovementSpeed(float movementSpeed) {
		this.movementSpeed = movementSpeed;
	}
}
