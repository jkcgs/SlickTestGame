package com.makzk.games.elements;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.newdawn.slick.*;
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

/**
 * Un nivel contiene todas las entidades que interactuan entre si, como el jugador y los enemigos
 */
public class Level {
	private GameContainer gc;
	private Main game;
	private List<Entity> entities = new ArrayList<>();
    private Player player;
	private float width;
	private float height;
	private float playerInitialX = 0;
	private float playerInitialY = 0;
	private Color backgroundColor = Color.black;
    private Image background = null;
    private Image backgroundScaled = null;
    private boolean bgRepeatX = true;
    private boolean bgRepeatY = true;
    private int bgWidth = 0;
    private int bgHeight = 0;

    /**
     * Crea un objeto Level según un tamaño
     * @param gc El contenedor del juego
     * @param game El juego que contiene carga al nivel
     * @param width El ancho del nivel
     * @param height El alto del nivel
     */
	public Level(GameContainer gc, Main game, float width, float height) {
		this.gc = gc;
		this.game = game;
		this.width = width;
		this.height = height;
    }

    /**
     * Crea un objeto Level con el tamaño del contenedor del juego
     * @param gc El contenedor del juego
     * @param game El juego que carga el nivel
     */
	public Level(GameContainer gc, Main game) {
		this(gc, game, gc.getWidth(), gc.getHeight());
	}
	
	/**
	 * Crear un nivel desde un archivo json
	 * @param filepath La ubicación del archivo. Normalmente estarían en "data/levels"
	 * @param gc El contenedor del juego
	 * @return El nivel diseñado en base al archivo json
	 */
	public static Level loadFromFile(String filepath, GameContainer gc, Main game) {
		Level level;
		try {
			String content = Utils.getResourceContent(filepath);
			JSONObject json = new JSONObject(content);

			float width = json.has("width") ? json.getInt("width") : gc.getWidth();
			float height = json.has("height") ? json.getInt("height") : gc.getHeight();

            level = new Level(gc, game, width, height);

            if(json.has("player")) {
                Player player = new Player(gc, game, level);
                player.setupFromJSON(json.getJSONObject("player"));
                level.setPlayer(player);
            }

            if(json.has("background")) {
                JSONObject bgObj = json.getJSONObject("background");
                if(bgObj.has("color")) {
                    try {
                        int[] colorArr = bgObj.getJSONArray("color").getIntArray();
                        level.setBackgroundColor(new Color(colorArr[0], colorArr[1], colorArr[2]));
                    } catch (Exception e) {
                        Log.error("Could not set a background color from json", e);
                    }
                }
                if(bgObj.has("image")) {
                    try {
                        Image bg;
                        bg = new Image(bgObj.getString("image"));
                        level.setBackground(bg);
                        level.setBackgroundWidth(bgObj.has("width") ? bgObj.getInt("width") : bg.getWidth());
                        level.setBackgroundHeight(bgObj.has("height") ? bgObj.getInt("height") : bg.getHeight());
                        if(bgObj.has("repeatX")) level.setBackgroundRepeatX(bgObj.getBoolean("repeatX"));
                        if(bgObj.has("repeatY")) level.setBackgroundRepeatY(bgObj.getBoolean("repeatY"));
                    } catch(SlickException e) {
                        Log.error("Could not load background image", e);
                    }
                }
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

        level.sortEntities();
		return level;
	}
	
	public void addEntity(EntityType type, float x, float y, float width, float height, Color color) {
		switch(type) {
		case RECT:
			EntityRect r = new EntityRect(gc, game, new Rectangle(x, y, width, height), this);
			r.setColor(color);
            r.setLevel(this);
			entities.add(r);
			break;
		case ENEMY:
			Enemy e = new Enemy(gc, new Rectangle(x, y, width, height), this);
            e.setLevel(this);
			entities.add(e);
		}
	}
	public void addEntity(EntityType type, float x, float y, float width, float height) {
		addEntity(type, x, y, width, height, null);
	}

    /**
     * Agrega un Entity al nivel
     * @param entity El Entity a agregar
     */
    public void addEntity(Entity entity) {
        entities.add(entity);
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
	 * @param rects Las entidades rectangles a añadir
	 */
	public void addEntities(float[][] rects) {
		for(float[] rect: rects) {
			addEntity(rect);
		}
	}
	public void addEntities(JSONArray entities) {
		for(int i = 0; i < entities.length(); i++) {
            // If the entity is an array of values [x,y,w,h,type]
            if(entities.get(i) instanceof JSONArray) {
                double[] rect = entities.getJSONArray(i).getDoubleArray();
                float[] frect = new float[rect.length];
                for (int j = 0; j < rect.length; j++) {
                    frect[j] = (float) rect[j];
                }
                addEntity(frect);
            } else if(entities.get(i) instanceof JSONObject) {
                // If the entity is an object {type,x,y,width,height,solid}
                addEntity((JSONObject) entities.get(i));
            }
		}

        Collections.sort(this.entities);
	}

    public void addEntity(JSONObject entity) {
        addEntity(EntityRect.createFromJSON(gc, game, this, entity));
    }

    public void addEntity(float[] entity) {
        if(entity.length == 7) {
            Color color = new Color((int)entity[4], (int)entity[5], (int)entity[6]);
            addEntity(EntityType.RECT, entity[0], entity[1], entity[2], entity[3], color);
        } else if(entity.length == 5) {
            switch((int)entity[4]) {
                case 1: // Enemy
                    addEntity(EntityType.ENEMY, entity[0], entity[1], entity[2], entity[3]); break;
                default: // EntityRect
                    Color color = new Color((int)entity[4], (int)entity[5], (int)entity[6]);
                    addEntity(EntityType.RECT, entity[0], entity[1], entity[2], entity[3], color);
            }
        }
    }

	public void updateEntities(int delta) {
		for(Entity entity : entities) {
			entity.move(delta);

			if(entity instanceof Enemy) {
				if(entity.getX() < 0 || entity.getY() > this.getHeight()) {
					entity.reset();
				}
			}
		}
	}

	public void drawAll(Graphics g) {
		drawAll(g, null);
	}

	public void drawAll(Graphics g, Camera cam) {
		// Set color only if different
		if(!g.getBackground().equals(backgroundColor)) {
			g.setBackground(backgroundColor);
		}

        if(background != null) {
            g.fillRect(0, 0, bgRepeatX ? width : bgWidth, bgRepeatY ? height : bgHeight, backgroundScaled,
                    cam != null ? cam.getX() : 0, cam != null ? cam.getY() : 0);
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
	 */
	public void reset() {
		for(Entity entity: entities) {
            if(!entity.isStatic()) {
                entity.reset();
            }
		}
	}

	public List<EntityRect> getRects() {
		List<EntityRect> le = new ArrayList<>();
		for(Entity e : entities) {
			if(e instanceof EntityRect) {
				le.add((EntityRect) e);
			}
		}
		return le;
	}
	public List<Enemy> getEnemies() {
		List<Enemy> le = new ArrayList<>();
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
	
	public Color getBackgroundColor() { return backgroundColor; }
	public void setBackgroundColor(Color bgColor) { this.backgroundColor = bgColor; }
    public Player getPlayer() { return player; }

    /**
     * Sets the player for the level. It's added/replaced to the level's entities.
     * @param player The player to add.
     */
    public void setPlayer(Player player) {
        this.player = player;
        for(int i = 0; i < entities.size(); i++) {
            if(entities.get(i) instanceof Player) {
                entities.set(i, player);
                return;
            }
        }

        entities.add(player);
        sortEntities();
    }

    /**
     * Sets the player from a JSONObject, readed as an entity. Adds it first to the entities list
     * then sets the field player from that list.
     * @param json The JSONObject that contains the Player properties
     */
    public void setPlayer(JSONObject json) throws SlickException {
        Player player = new Player(gc, game);
        player.setupFromConfig(json.getString("type"));
    }

    public void sortEntities() { Collections.sort(entities); }

    public void setBackground(Image img) { background = img; updateBackground(); }
    public void setBackgroundRepeatX(boolean repeat) { bgRepeatX = repeat; }
    public void setBackgroundRepeatY(boolean repeat) { bgRepeatY = repeat; }
    public void setBackgroundWidth(int width) { bgWidth = width; updateBackground(); }
    public void setBackgroundHeight(int height) { bgHeight = height; updateBackground(); }

    // Updates the scaled background instead of copying it every draw
    public void updateBackground() {
        backgroundScaled = background.getScaledCopy(bgWidth == 0 ? (int) width : bgWidth, bgHeight == 0 ? (int) height : bgHeight);
    }
}
