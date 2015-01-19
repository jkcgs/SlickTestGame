package com.makzk.games.entities;

import org.newdawn.slick.GameContainer;

public class Entity {
	protected GameContainer gc;
	protected float speedX;
	protected float speedY;

	public Entity(GameContainer gc) {
		this.gc = gc;
		speedX = 0;
		speedY = 0;
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

}
