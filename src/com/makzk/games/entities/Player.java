package com.makzk.games.entities;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Rectangle;

import com.makzk.games.Level;

public class Player extends EntityRect {
	private int controlLeft = Input.KEY_A;
	private int controlRight = Input.KEY_D;
	private int controlJump = Input.KEY_SPACE;
	private float speed = 1f;
	private float jumpImpulse = 5;
	private float initialX = 10;
	private float initialY = 10;

	public Player(GameContainer gc, Rectangle rect) {
		super(gc, rect);
		gravity = true;
		keepOnScreen = false;
	}
	
	public void move(Level level, int delta) {
		if(rect.getY() > gc.getHeight()) {
			reset();
		}
		
		Input in = gc.getInput();
		if(in.isKeyDown(controlLeft)) {
			speedX -= .01f;
			if(speedX < -speed) {
				speedX = -speed;
			}
		} else if(in.isKeyDown(controlRight)) {
			speedX += .01f;
			if(speedX > speed) {
				speedX = speed;
			}
		} else if(in.isKeyDown(controlJump)) {
			speedY -= jumpImpulse;
			if(speedY < -speed) {
				speedY = -speed;
			}
		} else {
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

		super.move(level, delta);
	}
	
	public void reset() {
		rect.setX(initialX);
		rect.setY(initialY);
	}

}
