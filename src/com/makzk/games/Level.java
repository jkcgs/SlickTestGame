package com.makzk.games;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;

import com.makzk.games.entities.EntityRect;
import com.makzk.games.util.Camera;

public class Level {
	private GameContainer gc;
	private List<EntityRect> rects = new ArrayList<EntityRect>();
	private float width;
	private float height;

	public Level(GameContainer gc, float width, float height) {
		this.gc = gc;
		this.width = width;
		this.height = height;
	}

	public Level(GameContainer gc, float width) {
		this(gc, width, gc.getHeight());
	}

	public Level(GameContainer gc) {
		this(gc, gc.getWidth(), gc.getHeight());
	}
	
	public void addRect(EntityRect rect) {
		rects.add(rect);
	}
	
	public void addRect(float x, float y, float width, float height, Color color) {
		EntityRect r = new EntityRect(gc, new Rectangle(x, y, width, height));
		r.setColor(color);
		addRect(r);
	}
	
	public void addRect(float x, float y, float width, float height) {
		addRect(x, y, width, height, Color.white);
	}

	public void drawAll(Graphics g) {
		drawAll(g, null);
	}

	public void drawAll(Graphics g, Camera cam) {
		for(EntityRect r: rects) {
			if(cam == null) {
				r.draw();
			} else {
				r.draw(cam);
			}
		}
	}
	
	public List<EntityRect> getRects() {
		return rects;
	}

	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}
}
