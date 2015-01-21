package com.makzk.games.entities;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Rectangle;

import com.makzk.games.Level;

public class Player extends EntityRect {
	private int controlLeft = Input.KEY_A;
	private int controlRight = Input.KEY_D;
	private int controlJump = Input.KEY_SPACE;
	private float speed = 0.5f;
	private float jumpImpulse = 1.5f;
	private float initialX;
	private float initialY;

	public Player(GameContainer gc, Rectangle rect) {
		super(gc, rect);
		gravity = true;
		keepOnScreen = false;
		initialX = rect.getX();
		initialY = rect.getY();
	}
	
	public void move(Level level, int delta) {
		super.move(level, delta);

		if(rect.getY() > gc.getHeight()) {
			reset();
		}
		if(rect.getX() < 0) {
			rect.setX(gc.getWidth() - 1);
		}
		if(rect.getX() >= gc.getWidth()) {
			rect.setX(0);
		}
		
		Input in = gc.getInput();
		if(in.isKeyDown(controlLeft)) {
			speedX -= .01f;
			if(speedX < -speed) {
				speedX = -speed;
			}
		}
		if(in.isKeyDown(controlRight)) {
			speedX += .01f;
			if(speedX > speed) {
				speedX = speed;
			}
		}

		if(in.isKeyDown(controlJump)) {
			if(speedY == 0 && onGround) {
				speedY = -jumpImpulse;
			}
		}

		if(!in.isKeyDown(controlLeft) 
				&& !in.isKeyDown(controlRight)){
			if(speedX != 0) {
				if(speedX > 0) {
					speedX -= .01f;
					if(speedX < 0) {
						speedX = 0;
					}
				} else {
					speedX += .01f;
					if(speedX > 0) {
						speedX = 0;
					}
				}
			}
		}
	}
	
	public void reset() {
		rect.setX(initialX);
		rect.setY(initialY);
	}

}
