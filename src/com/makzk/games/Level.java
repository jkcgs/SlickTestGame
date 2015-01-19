package com.makzk.games;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;

import com.makzk.games.entities.EntityRect;

public class Level {
	GameContainer gc;
	List<EntityRect> rects = new ArrayList<EntityRect>();
	public Level(GameContainer gc) {
		this.gc = gc;
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
		for(EntityRect r: rects) {
			g.setColor(r.getColor());
			g.fill(r.getRect());
		}
	}
}
