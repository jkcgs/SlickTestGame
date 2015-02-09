package com.makzk.games.util;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.geom.Rectangle;

import com.makzk.games.elements.Level;

public class Camera {
	private Rectangle rect;
	private Level level;
	private boolean keepOnLevel = true;
	private GameContainer gc;
	
	public Camera(Rectangle rect, Level level) {
		this.rect = rect;
		this.level = level;
	}
	
	public Camera(GameContainer gc, Level level) {
		this.rect = new Rectangle(0, 0, gc.getWidth(), gc.getHeight());
		this.level = level;
		this.gc = gc;
	}

	public Rectangle getRect() {
		return rect;
	}

	public void setRect(Rectangle rect) {
		this.rect = rect;
	}
	
	public void moveX(float x) {
		rect.setX(rect.getX() + x);
	}
	
	public void moveY(float x) {
		rect.setX(rect.getX() + x);
	}
	
	public void setX(float x) {
		rect.setX(x);
		if (keepOnLevel){
			if(rect.getX() < 0) {
				rect.setX(0);
			} else if(rect.getX() + rect.getWidth() > level.getWidth()) {
				if(level.getWidth() >= gc.getWidth()) {
					rect.setX(level.getWidth() - rect.getWidth());
				} else if(rect.getX() + rect.getWidth() > gc.getWidth()) {
					rect.setX(gc.getWidth() - rect.getWidth());
				}
			}
			
		}
	}
	public void setY(float y) {
		rect.setY(y);
		if (keepOnLevel){
			if(rect.getY() < 0) {
				rect.setY(0);
			} else if(rect.getY() + rect.getHeight() > level.getHeight()){
				if(level.getHeight() >= gc.getHeight()) {
					rect.setY(level.getHeight() - rect.getHeight());
				} else if(rect.getY() + rect.getHeight() > gc.getHeight()) {
					rect.setY(gc.getHeight() - rect.getHeight());
				}
			}
			
		}
	}

	public float getX() {
		return rect.getX();
	}
	
	public float getY() {
		return rect.getY();
	}
	
	public void autoMove(float x, float y) {
		setX(x-(rect.getWidth()/2));		
		setY(y-(rect.getHeight()/2));
	}
}
