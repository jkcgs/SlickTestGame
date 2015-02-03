package com.makzk.games;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.util.Log;

import com.makzk.games.entities.Enemy;
import com.makzk.games.entities.EntityRect;
import com.makzk.games.entities.Player;
import com.makzk.games.util.Camera;

public class Level {
	private GameContainer gc;
	private List<EntityRect> rects = new ArrayList<EntityRect>();
	private List<Enemy> enemies = new ArrayList<Enemy>();
	private float width;
	private float height;
	private float playerInitialX = 0;
	private float playerInitialY = 0;

	public Level(GameContainer gc, float width, float height, 
			float playerInitialX, float playerInitialY) {
		this.gc = gc;
		this.width = width;
		this.height = height;
		this.playerInitialX = playerInitialX;
		this.playerInitialY = playerInitialY;
	}
	
	public Level(GameContainer gc, float width, float height) {
		this(gc, width, height, 0, 0);
	}

	public Level(GameContainer gc, float width) {
		this(gc, width, gc.getHeight());
	}

	public Level(GameContainer gc) {
		this(gc, gc.getWidth(), gc.getHeight());
	}
	
	/**
	 * Crear un nivel desde un archivo json
	 * @param filepath La ubicación del archivo. Normalmente estarían en data/levels
	 * @param gc El contenedor del juego
	 * @return El nivel diseñado en base al archivo json
	 */
	public static Level loadFromFile(String filepath, GameContainer gc) {
		Level level = null;
		try {
			byte[] lines = Files.readAllBytes(Paths.get(filepath));
			String content = new String(lines, Charset.defaultCharset());
			JSONObject json = new JSONObject(content);

			float width = json.has("width") ? json.getInt("width") : gc.getWidth();
			float height = json.has("height") ? json.getInt("height") : gc.getHeight();
			level = new Level(gc, width, height);

			if(json.has("playerInitial")) {
				JSONObject pjInitial = json.getJSONObject("playerInitial");
				level.setPjInitialX(pjInitial.has("x") ? pjInitial.getInt("x") : 0);
				level.setPjInitialY(pjInitial.has("y") ? pjInitial.getInt("y") : 0);
			} else {
				level.setPjInitialX(0);
				level.setPjInitialY(0);
			}

			if(json.has("rects")) {
				level.addRects(json.getJSONArray("rects"));
			}
			if(json.has("enemies")) {
				level.addEnemies(json.getJSONArray("enemies"));
			}
			if(json.has("entities")) {
				level.addEntities(json.getJSONArray("entities"));
			}
		} catch (Throwable e) {
			Log.error("Error while parsing JSON file", e);
			return null;
		}
		
		return level;
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
	public void addRects(JSONArray rects) {
		for(int i = 0; i < rects.length(); i++) {
			JSONArray rect = rects.getJSONArray(i);
			this.addRect(
					rect.getInt(0), rect.getInt(1), rect.getInt(2), rect.getInt(3), 
					new Color(rect.getInt(4), rect.getInt(5), rect.getInt(6)));
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
	public void addEnemies(JSONArray enemies) throws JSONException, SlickException {
		for(int i = 0; i < enemies.length(); i++) {
			JSONArray enemy = enemies.getJSONArray(i);
			this.addEnemy(
					enemy.getInt(0), enemy.getInt(1), enemy.getInt(2), enemy.getInt(3));
		}
	}
	
	/**
	 * Agrega una entidad segÃºn un mapa de posiciones y caracterÃ­sticas.
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
	public void addEntities(JSONArray entities) throws SlickException {
		float[][] rects = new float[entities.length()][];
		for(int i = 0; i < entities.length(); i++) {
			double[] rect = entities.getJSONArray(i).getDoubleArray();
			rects[i] = new float[rect.length];
			for(int j = 0; j < rect.length; j++) {
				rects[i][j] = (float) rect[j];
			}
		}
		addEntities(rects);
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
	
	/**
	 * Reposiciona los enemigos del nivel y al jugador a una posición
	 * inicial.
	 * @param player
	 */
	public void reset(Player player) {
		for(Enemy enemy: enemies) {
			enemy.reset();
		}
		if(player != null) {
			player.reset();
			player.setPos(playerInitialX, playerInitialY);
		}
	}
	
	public void reset() {
		reset(null);
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

	public float getPjInitialX() {
		return playerInitialX;
	}
	
	public float getPjInitialY() {
		return playerInitialY;
	}

	public void setPjInitialX(float initialX) {
		playerInitialX = initialX;
	}
	
	public void setPjInitialY(float initialY) {
		playerInitialY = initialY;
	}
}
