package com.makzk.games.util;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.geom.Rectangle;

import com.makzk.games.Level;

public class Camera {
	private Rectangle rect;
	private Level level;
	private boolean keepOnLevel = true;
	
	public Camera(Rectangle rect, Level level) {
		this.rect = rect;
		this.level = level;
	}
	
	public Camera(GameContainer gc, Level level) {
		this.rect = new Rectangle(0, 0, gc.getWidth(), gc.getHeight());
		this.level = level;
	}

	public Rectangle getRect() {
		return rect;
	}

	public void setRect(Rectangle rect) {
		this.rect = rect;
	}
	
	public void moveX(float x) {
		rect.setX(rect.getX() + x);
		if(keepOnLevel) {
			if(rect.getX() < 0) {
				rect.setX(0);
			} else if(rect.getX() + rect.getWidth() > level.getWidth()) {
				rect.setX(level.getWidth() - rect.getWidth());
			}
		}	
	}
	
	public void moveY(float x) {
		rect.setX(rect.getX() + x);
	}
	
	public void setX(float x) {
		rect.setX(x);
	}

	public float getX() {
		return rect.getX();
	}
	
	public float getY() {
		return rect.getY();
	}
	
	public void autoMove(float x) {
		rect.setX(x-(rect.getWidth()/2));
		//Espacio para if (tall) then do some shit
	}
}
