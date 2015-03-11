package com.makzk.games.entities;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.util.Log;

import com.makzk.games.Main;
import com.makzk.games.util.Direction;

public class Pad extends EntityRect {
	private float movementSpeed = 1f;
	private int controlUp = Input.KEY_UP;
	private int controlDown = Input.KEY_DOWN;

	public Pad(GameContainer gc, Main game) {
		super(gc, game, new Rectangle(0, 0, 10, 100));
		keepOnScreen = true;
		color = Color.white;
        isStatic = false;
	}
	
	public Pad(GameContainer gc) {
		this(gc, null);
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

	public void autoMove(Ball ball, int delta) {
		if(ball.getY() < getY()) {
			moveY(-movementSpeed * delta);
		}
		
		if(ball.getY() > (getY() + getHeight())) {
			moveY(movementSpeed * delta);
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
