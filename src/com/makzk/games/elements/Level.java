package com.makzk.games.elements;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.Log;

import com.makzk.games.Main;
import com.makzk.games.entities.Enemy;
import com.makzk.games.entities.Entity;
import com.makzk.games.entities.EntityRect;
import com.makzk.games.entities.Player;
import com.makzk.games.util.Camera;
import com.makzk.games.util.EntityType;
import com.makzk.games.util.Utils;

public class Level {
	private GameContainer gc;
	private Main game;
	private List<Entity> entities = new ArrayList<Entity>();
	private float width;
	private float height;
	private float playerInitialX = 0;
	private float playerInitialY = 0;
	private Color bgColor = Color.black;

	public Level(GameContainer gc, Main game, float width, float height, 
			float playerInitialX, float playerInitialY) {
		this.gc = gc;
		this.game = game;
		this.width = width;
		this.height = height;
		this.playerInitialX = playerInitialX;
		this.playerInitialY = playerInitialY;
	}
	
	public Level(GameContainer gc, Main game, float width, float height) {
		this(gc, game, width, height, 0, 0);
	}

	public Level(GameContainer gc, Main game, float width) {
		this(gc, game, width, gc.getHeight());
	}

	public Level(GameContainer gc, Main game) {
		this(gc, game, gc.getWidth(), gc.getHeight());
	}
	
	/**
	 * Crear un nivel desde un archivo json
	 * @param filepath La ubicaci�n del archivo. Normalmente estar�an en data/levels
	 * @param gc El contenedor del juego
	 * @return El nivel dise�ado en base al archivo json
	 */
	public static Level loadFromFile(String filepath, GameContainer gc, Main game) {
		Level level = null;
		try {
			String content = Utils.getResourceContent(filepath);
			JSONObject json = new JSONObject(content);

			float width = json.has("width") ? json.getInt("width") : gc.getWidth();
			float height = json.has("height") ? json.getInt("height") : gc.getHeight();
			level = new Level(gc, game, width, height);

			if(json.has("playerInitial")) {
				JSONObject pjInitial = json.getJSONObject("playerInitial");
				level.setPjInitialX(pjInitial.has("x") ? pjInitial.getInt("x") : 0);
				level.setPjInitialY(pjInitial.has("y") ? pjInitial.getInt("y") : 0);
			} else {
				level.setPjInitialX(0);
				level.setPjInitialY(0);
			}
			
			if(json.has("bgColor")) {
				int[] colorArr = json.getJSONArray("bgColor").getIntArray();
				level.setBgColor(new Color(colorArr[0], colorArr[1], colorArr[2]));
			}

			if(json.has("rects")) {
				level.addEntities(json.getJSONArray("rects"));
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
	
	public void addEntity(EntityType type, float x, float y, float width, float height, Color color) {
		switch(type) {
		case RECT:
			EntityRect r = new EntityRect(gc, game, new Rectangle(x, y, width, height), this);
			r.setColor(color);
			entities.add(r);
			break;
		case ENEMY:
			Enemy e = new Enemy(gc, new Rectangle(x, y, width, height), this);
			entities.add(e);
		}
	}
	public void addEntity(EntityType type, float x, float y, float width, float height) {
		addEntity(type, x, y, width, height, null);
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
	public void addEntities(float[][] rects) {
		for(float[] rect: rects) {
			if(rect.length == 4) {
				addEntity(EntityType.RECT, rect[0], rect[1], rect[2], rect[3]);
			} else if(rect.length == 7) {
				Color color = new Color((int)rect[4], (int)rect[5], (int)rect[6]);
				addEntity(EntityType.RECT, rect[0], rect[1], rect[2], rect[3], color);
			} else if(rect.length == 8 || rect.length == 5) {
				switch((int)rect[4]) {
				case 1: // Enemy
					addEntity(EntityType.ENEMY, rect[0], rect[1], rect[2], rect[3]); break;
				default: // EntityRect
					if(rect.length == 5) {
						addEntity(EntityType.RECT, rect[0], rect[1], rect[2], rect[3]);
					} else if(rect.length == 8) {
						Color color = new Color((int)rect[4], (int)rect[5], (int)rect[6]);
						addEntity(EntityType.RECT, rect[0], rect[1], rect[2], rect[3], color);
					}
				}
			}
		}
	}
	public void addEntities(JSONArray entities) {
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

	public void updateEntities(int delta) {
		for(Entity entity : entities) {
			entity.move(delta, this);

			if(entity instanceof Enemy) {
				if(entity.getX() < 0 || entity.getY() > this.getHeight()) {
					((Enemy) entity).reset();
				}
			}
		}
	}

	public void drawAll(Graphics g) {
		drawAll(g, null);
	}

	public void drawAll(Graphics g, Camera cam) {
		// Set color only if different
		if(!g.getBackground().equals(bgColor)) {
			g.setBackground(bgColor);
		}

		for(Entity entity: entities) {
			if(cam == null) {
				entity.draw();
			} else {
				entity.draw(cam);
			}
		}
	}
	
	/**
	 * Reposiciona los enemigos del nivel y al jugador a una posici�n
	 * inicial.
	 * @param player
	 */
	public void reset(Player player) {
		for(Enemy enemy: getEnemies()) {
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
		List<EntityRect> le = new ArrayList<EntityRect>();
		for(Entity e : entities) {
			if(e instanceof EntityRect) {
				le.add((EntityRect) e);
			}
		}
		return le;
	}
	public List<Enemy> getEnemies() {
		List<Enemy> le = new ArrayList<Enemy>();
		for(Entity e : entities) {
			if(e instanceof Enemy) {
				le.add((Enemy) e);
			}
		}
		return le;
	}

	public float getWidth() { return width; }
	public void setWidth(float width) { this.width = width; }
	public float getHeight() { return height; }
	public void setHeight(float height) { this.height = height; }

	public float getPjInitialX() { return playerInitialX; }
	public float getPjInitialY() { return playerInitialY; }
	public void setPjInitialX(float initialX) { playerInitialX = initialX; }
	public void setPjInitialY(float initialY) { playerInitialY = initialY; }

	public List<Entity> getEntities() { return entities; }
	public StateBasedGame getGame() { return game; }
	
	public Color getBgColor() { return bgColor; }
	public void setBgColor(Color bgColor) { this.bgColor = bgColor; }
}
