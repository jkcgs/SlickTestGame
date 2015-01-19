package com.makzk.games.entities;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.geom.Rectangle;

import com.makzk.games.util.Direction;

public class EntityRect extends Entity {
	protected Rectangle rect;
	protected Color color;
	protected boolean keepOnScreen = true;

	public EntityRect(GameContainer gc, Rectangle rect) {
		super(gc);
		this.rect = rect;
		color = Color.white;
	}
	
	public void draw() {
		gc.getGraphics().setColor(color);
		gc.getGraphics().fill(rect);
	}

	public void move(int delta) {
		rect.setX(rect.getX() + (speedX * delta));
		rect.setY(rect.getY() + (speedY * delta));
		
		if(keepOnScreen) {
			if (rect.getX() < 0) {
				rect.setX(0);
			} else if(rect.getX() > (gc.getWidth() - rect.getWidth())) {
				rect.setX(gc.getWidth() - rect.getWidth());
			}
			
			if (rect.getY() < 0) {
				rect.setY(0);
			} else if(rect.getY() > (gc.getHeight() - rect.getHeight())) {
				rect.setY(gc.getHeight() - rect.getHeight());
			}
		}
	}

	public Rectangle getRect() {
		return rect;
	}

	public void setRect(Rectangle rect) {
		this.rect = rect;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}
	
	// http://gamedev.stackexchange.com/a/29796
	public Direction collisionSide(EntityRect other) {
		float w = (float) (0.5 * (rect.getWidth() + other.getRect().getWidth()));
		float h = (float) (0.5 * (rect.getHeight() + other.getRect().getHeight()));
		float dx = rect.getCenterX() - other.getRect().getCenterX();
		float dy = rect.getCenterY() - other.getRect().getCenterY();

		if (Math.abs(dx) <= w && Math.abs(dy) <= h)
		{
		    float wy = w * dy;
		    float hx = h * dx;

		    if (wy > hx) {
		        if (wy > -hx) return Direction.NORTH;
		        else return Direction.WEST;
		    } else {
		        if (wy > -hx) return Direction.EAST;
		        else return Direction.SOUTH;
		    }
		}
		
		return Direction.NONE;
	}

	public boolean collisionInX(EntityRect other) {
		Direction side = collisionSide(other);
		return side == Direction.EAST || side == Direction.WEST;
	}

	public boolean collisionInY(EntityRect other) {
		Direction side = collisionSide(other);
		return side == Direction.NORTH || side == Direction.SOUTH;
	}
}
