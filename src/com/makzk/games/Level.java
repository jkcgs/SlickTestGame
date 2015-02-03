package com.makzk.games;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;

import com.makzk.games.entities.Enemy;
import com.makzk.games.entities.EntityRect;
import com.makzk.games.util.Camera;

public class Level {
	private GameContainer gc;
	private List<EntityRect> rects = new ArrayList<EntityRect>();
	private List<Enemy> enemies = new ArrayList<Enemy>();
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
		rect.setLevel(this);
		rects.add(rect);
	}
	
	public void addRect(float x, float y, float width, float height, Color color) {
		EntityRect r = new EntityRect(gc, new Rectangle(x, y, width, height), this);
		r.setColor(color);
		addRect(r);
	}
	
	public void addRect(float x, float y, float width, float height) {
		addRect(x, y, width, height, Color.white);
	}
	
	public void addRects(float[][] rects, Color color) {
		for(float[] rect : rects) {
			if(rect.length == 7) {
				color = new Color((int)rect[4], (int)rect[5], (int)rect[6]);
			}

			if(color != null) {
				addRect(rect[0], rect[1], rect[2], rect[3], color);
			} else {
				addRect(rect[0], rect[1], rect[2], rect[3]);
			}
		}
	}
	
	public void addEnemy(float x, float y, float width, float height) throws SlickException {
		Enemy e = new Enemy(gc, new Rectangle(x, y, width, height), this);
		enemies.add(e);
	}
	
	public void addEnemies(float[][] enemiesPos) throws SlickException {
		for(float[] rect: enemiesPos) {
			addEnemy(rect[0], rect[1], rect[2], rect[3]);
		}
	}
	
	/**
	 * Agrega una entidad según un mapa de posiciones y características.
	 * Los formatos aceptados son:
	 * 
	 * <pre>
	 * {x, y, width, height} - EntityRect de fondo blanco
	 * {x, y, width, height, color_red, color_green, color_blue} - EntityRect con sus colores
	 * {x, y, width, height, type}
	 * {x, y, width, height, type, color_red, color_green, color_blue}
	 * </pre>
	 * 
	 * Donde type puede ser:
	 * <ul>
	 * <li>0 - EntityRect</li>
	 * <li>1 - Enemy</li>
	 * </ul>
	 * 
	 * @param rects
	 * @throws SlickException
	 */
	public void addEntities(float[][] rects) throws SlickException {
		for(float[] rect: rects) {
			if(rect.length == 4) {
				addRect(rect[0], rect[1], rect[2], rect[3]);
			} else if(rect.length == 7) {
				Color color = new Color((int)rect[4], (int)rect[5], (int)rect[6]);
				addRect(rect[0], rect[1], rect[2], rect[3], color);
			} else if(rect.length == 8 || rect.length == 5) {
				switch((int)rect[4]) {
				case 1: // Enemy
					addEnemy(rect[0], rect[1], rect[2], rect[3]); break;
				default: // EntityRect
					if(rect.length == 5) {
						addRect(rect[0], rect[1], rect[2], rect[3]);
					} else if(rect.length == 8) {
						Color color = new Color((int)rect[4], (int)rect[5], (int)rect[6]);
						addRect(rect[0], rect[1], rect[2], rect[3], color);
					}
				}
			}
		}
	}
	
	public void updateEnemies(int delta) {
		for(Enemy enemy : enemies) {
			enemy.move(this, delta);
			if(enemy.getX() < 0 || enemy.getY() > this.getHeight()) {
				enemy.reset();
			}
		}
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

		for(Enemy enemy: enemies) {
			if(cam == null) {
				enemy.draw();
			} else {
				enemy.draw(cam);
			}
		}
	}

	public List<EntityRect> getRects() {
		return rects;
	}
	public List<Enemy> getEnemies() {
		return enemies;
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
