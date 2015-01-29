package com.makzk.games.entities;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;

public class Entity {
	protected GameContainer gc;
	protected float speedX = 0;
	protected float speedY = 0;
	protected Color color;
	protected boolean keepOnScreen = true;
	protected boolean gravity = false;
	protected boolean solid = true;
	protected boolean onGround = false;

	public Entity(GameContainer gc) {
		this.gc = gc;
	}

	public float getSpeedX() {
		return speedX;
	}

	public void setSpeedX(float speedX) {
		this.speedX = speedX;
	}

	public float getSpeedY() {
		return speedY;
	}

	public void setSpeedY(float speedY) {
		this.speedY = speedY;
	}
	
	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}
	
	public boolean isOnGround() {
		return onGround;
	}
}
