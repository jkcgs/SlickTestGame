package com.makzk.games.entities;

import com.makzk.games.Main;
import com.makzk.games.elements.Level;
import com.makzk.games.util.Animations;
import com.makzk.games.util.Camera;
import com.makzk.games.util.Direction;
import com.makzk.games.util.Utils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.newdawn.slick.*;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.util.Log;

import java.util.Arrays;

import static com.makzk.games.util.Animations.*;
import static com.makzk.games.util.Direction.*;

public class EntityRect extends Entity {
	protected Rectangle collisionBox;
	protected float[][] drawBoxes = new float[ANIMATION_TOTAL.ordinal()][4];
	protected float[][] drawBoxesFlipped = new float[ANIMATION_TOTAL.ordinal()][4];
	protected Animation[] animations = new Animation[ANIMATION_TOTAL.ordinal()];
	protected int actualAnimation = ANIMATION_STAND.ordinal();
	protected boolean spriteFlipHorizontal = false;

	public EntityRect(GameContainer gc, Main game, Rectangle collisionBox, Level level) {
		super(gc, game);
		this.collisionBox = collisionBox;
		this.level = level;
	}

	public EntityRect(GameContainer gc, Main game, Rectangle collisionBox) {
		this(gc, game, collisionBox, null);
	}

    public EntityRect(GameContainer gc, Main game, String entityConfigType) throws SlickException {
        this(gc, game, null, null);
        setupFromConfig(entityConfigType);
    }

    /**
     * Crea una instancia de EntityRect dado un JSONObject con propiedades.
     * @param json El JSONObject que contiene las propiedades para el EntityRect
     * @return El EntityRect generado
     */
    public static EntityRect createFromJSON(GameContainer gc, Main game, Level level, JSONObject json) {
        if(!json.has("type")) {
            Log.error("Entity object from level json does not have a type defined");
            return null;
        }

        EntityRect ejson;
        try {
            ejson = new EntityRect(gc, game, json.getString("type"));
            ejson.setLevel(level);
        } catch (SlickException e) {
            Log.error(String.format("Could not create entity type '%s'", json.getString("type")));
            Log.error(e);
            return null;
        }

        if(json.has("x")) ejson.setX((float) json.getDouble("x"));
        if(json.has("y")) ejson.setY((float) json.getDouble("y"));
        if(json.has("width")) ejson.setWidth((float) json.getDouble("width"));
        if(json.has("height")) ejson.setHeight((float) json.getDouble("height"));

        if(json.has("solid")) ejson.setSolid(json.getBoolean("solid"));
        if(json.has("zIndex")) ejson.setzIndex(json.getInt("zIndex"));

        return ejson;
    }

	/**
	 * Configurar una animación para una posición determinada, determinado
	 * por un rango de posiciones de la grilla de sprites del SpriteSheet
	 * @param sprite El SpriteSheet que contiene los sprites
	 * @param anim La posición a configurar
	 * @param x1 El primer elemento en X de la grilla de sprites del SpriteSheet
	 * @param y1 El primer elemento en Y de la grilla de sprites del SpriteSheet
	 * @param x2 El último elemento en X de la grilla de sprites del SpriteSheet
	 * @param y2 El último elemento en Y de la grilla de sprites del SpriteSheet
	 * @param duration La duraci�n de cada animaci�n
	 */
	public void setupAnimation(SpriteSheet sprite, Animations anim, 
			int x1, int y1, int x2, int y2, int duration, float[] drawBox, float[] drawBoxFlipped) {
		animations[anim.ordinal()] = new Animation(sprite, x1, y1, x2, y2, true, duration, true);
		this.drawBoxes[anim.ordinal()] = drawBox;
		this.drawBoxesFlipped[anim.ordinal()] = drawBoxFlipped;
	}

	public void setupAnimation(SpriteSheet sprite, Animations anim, 
			int x1, int y1, int x2, int y2, int duration, float[] drawBox) {
		setupAnimation(sprite, anim, x1, y1, x2, y2, duration, drawBox, null);
	}

	public void setupAnimation(SpriteSheet sprite, Animations anim, 
			int x1, int y1, int x2, int y2, int duration) {
		setupAnimation(sprite, anim, x1, y1, x2, y2, duration, null, null);
	}
	
	/**
	 * Configurar una animación para una posición determinada, determinado
	 * por una o varias posiciones de la grilla de sprites del SpriteSheet, 
	 * sólo en la primera fila de éste
	 * @param sprite El SpriteSheet que contiene los sprites
	 * @param anim La posición a configurar
	 * @param xpositions Las posiciones en X a usar
	 * @param duration La duración de cada posición
	 * @param drawBox La posici�n y tama�o de la animaci�n y sprite. Las coordenadas del
	 * rect�ngulo se usan como offset en base a la posici�n del objeto, y no como posici�n absoluta.
	 * @param drawBoxFlipped Lo mismo para drawBox, pero para su animaci�n invertida horizontalmente.
	 */
	public void setupAnimation(SpriteSheet sprite, Animations anim, int[] xpositions, 
			int duration, float[] drawBox, float[] drawBoxFlipped) {
		int[] frames = new int[xpositions.length*2];
		int[] durations = new int[xpositions.length];
		
		Arrays.fill(durations, duration);
		for(int i = 0; i < xpositions.length; i++) {
			frames[i*2] = xpositions[i];
			frames[i*2+1] = 0;
		}
		
		this.drawBoxes[anim.ordinal()] = drawBox;
		this.drawBoxesFlipped[anim.ordinal()] = drawBoxFlipped;
		animations[anim.ordinal()] = new Animation(sprite, frames, durations);
	}

	/**
	 * Configurar una animación para una posición determinada, determinado
	 * por una o varias posiciones de la grilla de sprites del SpriteSheet, 
	 * sólo en la primera fila de éste
	 * @param sprite El SpriteSheet que contiene los sprites
	 * @param anim La posición a configurar
	 * @param xpositions Las posiciones en X a usar
	 * @param duration La duración de cada posición
	 * @param drawBox La posici�n y tama�o de la animaci�n y sprite. Las coordenadas del
	 * rect�ngulo se usan como offset en base a la posici�n del objeto, y no como posici�n absoluta.
	 */
	public void setupAnimation(SpriteSheet sprite, Animations anim, 
			int[] xpositions, int duration, float[] drawBox) {
		setupAnimation(sprite, anim, xpositions, duration, drawBox, null);
	}
	
	/**
	 * Configurar una animación para una posición determinada, determinado
	 * por una o varias posiciones de la grilla de sprites del SpriteSheet, 
	 * sólo en la primera fila de éste
	 * @param sprite El SpriteSheet que contiene los sprites
	 * @param anim La posición a configurar
	 * @param xpositions Las posiciones en X a usar
	 * @param duration La duración de cada posición
	 */
	public void setupAnimation(SpriteSheet sprite, Animations anim, int[] xpositions, int duration) {
		setupAnimation(sprite, anim, xpositions, duration, null, null);
	}

    /**
     * Loads entity bounds, background and animations from entity configuration file (data/entity_types.json)
     * @param name The name of the entity to load from configuration file
     * @throws SlickException
     * @throws JSONException
     */
    public void setupFromConfig(String name) throws SlickException, JSONException {
        String content = Utils.getResourceContent("data/entity_types.json");
        JSONObject json = new JSONObject(content);

        if(!json.has(name)) {
            throw new IllegalArgumentException("The entity name was not found on entities configuration file.");
        }

        if(collisionBox == null) {
            collisionBox = new Rectangle(0,0,0,0);
        }

        // JSONObject is a string
        if(!(json.get(name) instanceof JSONObject)) {
            bgImage = new Image(json.getString(name));
            collisionBox.setSize(bgImage.getWidth(), bgImage.getHeight());
            return;
        }

        json = json.getJSONObject(name);
        if(json.has("width") && json.has("height")) {
            collisionBox.setSize((float) json.getDouble("width"), (float) json.getDouble("height"));
        }

        if(json.has("posX")) {
            collisionBox.setX((float) json.getDouble("posX"));
        }

        if(json.has("posY")) {
            collisionBox.setY((float) json.getDouble("posY"));
        }

        if(json.has("solid")) {
            solid = json.getBoolean("solid");
        }

        SpriteSheet ss = null;
        // Load default sprite sheet if file is indicated on configuration file
        if(json.has("sprite_sheet")) {
            ss = new SpriteSheet(json.getString("sprite_sheet"), json.getInt("width"), json.getInt("height"));
        }

        if(json.has("background")) {
            if(json.get("background") instanceof JSONObject) {
                JSONObject bgObj = json.getJSONObject("background");
                try {
                    // If background image is set as "true" and SpriteSheet is loaded, use the first sprite
                    if (bgObj.has("image")) {
                        if (bgObj.getBoolean("image") && ss != null) {
                            bgImage = ss.getSubImage(0, 0);
                        } else {
                            bgImage = new Image(bgObj.getString("image"));
                            if (!json.has("width") && !json.has("height")) {
                                collisionBox.setSize(bgImage.getWidth(), bgImage.getHeight());
                            }
                        }
                    }

                    // Set background color if set
                    if (bgObj.has("color")) {
                        int[] colors = bgObj.getJSONArray("color").getIntArray();
                        color = new Color(colors[0], colors[1], colors[2]);
                    }
                } catch (JSONException e) {
                    Log.error("There was an error trying to set a background");
                    Log.error(e);
                }
            } else {
                bgImage = new Image(json.getString("background"));
                if (!json.has("width") && !json.has("height")) {
                    collisionBox.setSize(bgImage.getWidth(), bgImage.getHeight());
                }
            }
        }

        // Prepare animations
        if(json.has("animations")) {
            JSONObject anims = json.getJSONObject("animations");
            for(int i = 0; i < Animations.animations.length; i++) {
                String anim = Animations.animations[i];
                if(anims.has(Animations.animations[i])) {
                    JSONObject animJSON = anims.getJSONObject(anim);
                    int duration = animJSON.has("duration") ? animJSON.getInt("duration") : 10000;
                    int[] positions;
                    boolean rangedPos = false;
                    if(animJSON.get("positions") instanceof JSONArray) {
                        JSONArray posArray = animJSON.getJSONArray("positions");
                        if( !(posArray.get(0) instanceof JSONArray)) {
                            positions = posArray.getIntArray();
                        } else {
                            // [x1, y1], [x2, y2]
                            int[] pos1 = posArray.getJSONArray(0).getIntArray();
                            int[] pos2 = posArray.getJSONArray(1).getIntArray();
                            positions = new int[] {pos1[0], pos1[1], pos2[0], pos2[1]};
                            rangedPos = true;
                        }
                    } else {
                        positions = new int[]{animJSON.getInt("positions")};
                    }

                    if(ss != null) {
                        if(rangedPos) {
                            setupAnimation(ss, Animations.values()[i], positions[0], positions[1], positions[2], positions[3], duration);
                        } else {
                            setupAnimation(ss, Animations.values()[i], positions, duration);
                        }
                    } else if (animJSON.has("sprite")) {
                        JSONObject animSprite = animJSON.getJSONObject("sprite");
                        if (!animSprite.has("image") || !animSprite.has("width") || !animSprite.has("height")) {
                            throw new IllegalArgumentException(String.format("The animation %s sprite does not have all the required parameters (image, width, height)", anim));
                        }

                        SpriteSheet animSS = new SpriteSheet(animSprite.getString("image"), animSprite.getInt("width"), animSprite.getInt("height"));
                        setupAnimation(animSS, Animations.values()[i], positions, duration);
                    } else {
                        throw new IllegalStateException(String.format("The animation %s does not have an sprite sheet", anim));
                    }

                    // Setup animation draw boxes

                    if(animJSON.has("draw_rect")
                            && animJSON.get("draw_rect") instanceof JSONArray
                            && animJSON.getJSONArray("draw_rect").length() == 4) {
                        Log.info(String.format("Animation %s has draw_rect", anim));
                        double[] pbox = animJSON.getJSONArray("draw_rect").getDoubleArray();
                        drawBoxes[i] = new float[4];
                        for(int j = 0; j < 4; j++) {
                            drawBoxes[i][j] = (float) pbox[j];
                        }
                    }

                    if(animJSON.has("draw_rect_flipped")
                            && animJSON.get("draw_rect_flipped") instanceof JSONArray
                            && animJSON.getJSONArray("draw_rect_flipped").length() == 4) {
                        Log.info(String.format("Animation %s has draw_rect_flipped", anim));
                        drawBoxesFlipped[i] = new float[4];
                        double[] pbox = animJSON.getJSONArray("draw_rect_flipped").getDoubleArray();
                        for(int j = 0; j < 4; j++) {
                            drawBoxesFlipped[i][j] = (float) pbox[j];
                        }
                    }
                }
            }
        }
    }

    /**
     * Configura el EntityRect desde un JSONObject con sus propiedades
     * @param json
     */
    public void setupFromJSON(JSONObject json) {
        try {
            setupFromConfig(json.getString("type"));
        } catch (SlickException e) {
            Log.error(String.format("Could not create entity type '%s'", json.getString("type")));
            Log.error(e);
            return;
        }

        if(json.has("x")) {
            setX((float) json.getDouble("x"));
            if(this instanceof Player) {
                ((Player) this).setInitialX(json.getInt("x"));
            }
        }
        if(json.has("y")) {
            setY((float) json.getDouble("y"));
            if(this instanceof Player) {
                ((Player) this).setInitialY(json.getInt("y"));
            }
        }
        if(json.has("width")) setWidth((float) json.getDouble("width"));
        if(json.has("height")) setHeight((float) json.getDouble("height"));

        if(json.has("solid")) setSolid(json.getBoolean("solid"));
        if(json.has("zIndex")) setzIndex(json.getInt("zIndex"));
    }
	
	/**
	 * Actualizar la animación del jugador dependiendo de su estado actual
	 */
	public void updateAnimation() {
		if(speedY < -.1) {
			// La entity está saltando
			actualAnimation = ANIMATION_JUMP.ordinal();
		} else if (speedY > .2 && getTimeOffGround() > 150) {
			// La entity esta cayendo
			actualAnimation = ANIMATION_FALL.ordinal();
		} else if (speedX != 0) {
			// La entity está yendo hacia los lados, pero no saltando (else if)
			actualAnimation = (getTimeOffGround() < 150 ? ANIMATION_RUN : ANIMATION_FALL).ordinal();
		} else {
			// La entity está detenida
			actualAnimation = (onGround && getTimeOffGround() < 150 ? ANIMATION_STAND : ANIMATION_FALL).ordinal();
		}
	}
	
	/**
	 * Dibuja el elemento sólo si no se trata de un rectángulo transparente
	 */
	public void draw() {
		draw(null);
	}

	/**
	 * Dibuja el elemento sólo si no se trata de un rectángulo transparente
	 * en base a la posición de una cámara
	 */
	public void draw(Camera cam) {
		if(!enabled) {
			return;
		}

        if(color != Color.transparent) {
            gc.getGraphics().setColor(color);
            if(cam == null) {
                gc.getGraphics().fill(collisionBox);
            } else {
                gc.getGraphics().fillRect(getX() - cam.getX(), getY() - cam.getY(),
                        getWidth(), getHeight());
            }
        }

		// Se determina si la entity ha ido a la izquierda o no, para 
		// voltear al sprite
		if(speedX < 0) {
			spriteFlipHorizontal = true;
		} else if(speedX > 0) {
			spriteFlipHorizontal = false;
		}

        if(bgImage != null) {
            Image bgif = spriteFlipHorizontal ? bgImage.getFlippedCopy(true, false) : bgImage;
            if(cam == null) {
                bgif.draw(getX(), getY(), getWidth(), getHeight());
            } else {
                bgif.draw(getX() - cam.getX(), getY() - cam.getY(),
                        getWidth(), getHeight());
            }
        }
		
		updateAnimation(); // Actualizar la animación actual		
		
		// Dibujar el sprite, con su volteo correspondiente si corresponde, y
		// según el rectángulo del elemento
		if(animations[actualAnimation] != null) {
			float finalX = getX();
			float finalY = getY();
			float width = getWidth();
			float height = getHeight();

			if(cam != null) {
				finalX -= cam.getX();
				finalY -= cam.getY();
			}
			
			float[] dbf = drawBoxesFlipped[actualAnimation];
			float[] dbox = drawBoxes[actualAnimation];

			if(spriteFlipHorizontal && dbf != null) {
				finalX += dbf[0];
				finalY += dbf[1];
				width = dbf[2];
				height = dbf[3];
			} else if( dbox != null ) {
				finalX += dbox[0];
				finalY += dbox[1];
				width = dbox[2];
				height = dbox[3];
			}
			
			animations[actualAnimation].getCurrentFrame().getFlippedCopy(spriteFlipHorizontal, false)
			.draw(finalX, finalY, width, height);
		}
	}

	/**
	 * Realiza los movimientos del elemento basado en los elementos de un nivel
	 * @param lv El nivel que contiene otras entidades
	 * @param delta Delta de diferencia de rendimiento del juego para no variar la velocidad
	 */
	public void move(int delta, Level lv) {
		if(!enabled || isStatic) {
			return;
		}

		// Gravedad
		if(gravity && !onGround) {
			speedY += gravityImpulse;
			// Limitar gravedad a 1
			if(speedY > 1) {
				speedY = 1;
			}
		}

		nextY = speedY * delta + getY();
		nextX = speedX * delta + getX();
		onGround = false;

		if(solid && level != null) {
			wall = false;

			// Revisar colisiones con elementos del nivel
			for (Entity e : level.getEntities()) {
				resolveCollisions(e);
			}
		}

		setY(nextY);
		setX(nextX);

		// Si es necesario mantener el objeto en pantalla, cualquier movimiento
		// fuera de esta, hará que se retorne al último borde alcanzado.
		if(keepOnScreen) {
			if (getX() < 0) {
				setX(0);
			} else if(getX() > (gc.getWidth() - getWidth())) {
				setX(gc.getWidth() - getWidth());
			}
			
			if (getY() < 0) {
				setY(0);
			} else if(getY() > (gc.getHeight() - getHeight())) {
				setY(gc.getHeight() - getHeight());
			}
		}

		if(animations[actualAnimation] != null) {
			animations[actualAnimation].update(delta);
		}

		if(onGround) {
			lastTimeOnGround = System.currentTimeMillis();
		}
	}

    public void move(int delta) {
        move(delta, null);
    }

	public void resolveCollisions(Entity e) {
		// Si el elemento no es sólido, no hay qué revisar
		if(!e.isSolid() || !e.isEnabled()) {
			return;
		}
		
		// Colisión vertical
		if(getX() <= e.getMaxX() && getX() + getWidth() > e.getX()) {
			// Colisión abajo
			if(getY() + getHeight() <= e.getY() && nextY + getHeight() >= e.getY()) {
				nextY = e.getY() - getHeight();
				speedY = 0;
				onGround = true;
				onCollision(SOUTH, e);
			}
			// Colisión arriba
			else if(e.getMaxY() <= getY() && e.getMaxY() >= nextY) {
				nextY = e.getMaxY();
				speedY = 0;
				onCollision(NORTH, e);
			}
		}
		
		// Colisión horizontal
		if((nextY < e.getMaxY()) && (nextY + getHeight() > e.getY())) {
			// Slope: subir al jugador al Entity colisionador si choca horizontalmente
			// a cierta altura. 
			boolean slope = ((getY() + getHeight()) - e.getY()) <= 2;
			// Se conserva la posición siguiente en caso de chocar con slope
			float auxX = nextX;

			// Colisión derecha
			if(getX() + getWidth() <= e.getX() && nextX + getWidth() >= e.getX()) {
				nextX = e.getX() - getWidth(); // Colisión, se retrocede al jugador
				if(slope && nextX != getX()) {
					// Se detecta slope sólo si el personaje puede moverse en X
					nextX = auxX; // Se permite avanzar al jugador
					nextY = e.getY() - getHeight(); // Se sube al jugador a la sgte plataforma
				} else {
					// Si no hay slope, se procede con la detención y el evento de colisión
					speedX = 0;
					wall = true;
					onCollision(EAST, e);
				}
			}
			// Colisión izquierda
			else if(e.getMaxX() <= getX() && e.getMaxX() > nextX) {
				nextX = getX();
				// Las condiciones de slope aquí son iguales a las de colisión derecha
				if(slope && nextX != getX()) {
					nextX = auxX;
					nextY = e.getY() - getHeight();
				} else {
					speedX = 0;
					wall = true;
					onCollision(WEST, e);
				}
			}

			
		}
	}
	
	/**
	 * Obtener el lado de la colisión con otro objeto.
	 * Obtenido desde: http://gamedev.stackexchange.com/a/29796
	 * @param other El otro elemento a revisar
	 * @return La dirección de colisión. Si no hubo colisión se retorna Direction.NONE
	 */
	public Direction collisionSide(EntityRect other) {
		if(collisionBox.intersects(other.getCollisionBox())) {
			if(speedX < 0) return Direction.WEST;
			else if(speedX >= 0) return Direction.EAST;
			else if(speedY < 0) return Direction.NORTH;
			else if(speedY >= 0) return Direction.SOUTH;
		}
		
		return Direction.NONE;
	}

	public boolean collisionInX(EntityRect other) {
		Direction side = collisionSide(other);
		return side == EAST || side == WEST;
	}

	public boolean collisionInY(EntityRect other) {
		Direction side = collisionSide(other);
		return side == NORTH || side == SOUTH;
	}

	// get rekt m7
	public Rectangle getCollisionBox() { return collisionBox; }
	public void setCollisionBox(Rectangle collisionBox) { this.collisionBox = collisionBox; }

	// Shorthands para posición y movimiento
	public float getX() { return collisionBox.getX(); }
	public float getMaxX() { return collisionBox.getMaxX(); }
	public float getY() { return collisionBox.getY(); }
	public float getMaxY() { return collisionBox.getMaxY(); }
	public void setX(float x) { collisionBox.setX(x); }
	public void setY(float y) { collisionBox.setY(y); }
	public void moveX(float x) { setX(getX() + x); }
	public void moveY(float y) { setY(getY() + y); }
	public void movePos(float x, float y) { moveX(x); moveY(y); }
	public void setPos(float x, float y) { setX(x); setY(y); }

	// Shorthands de tamaño
	public float getWidth() { return collisionBox.getWidth(); }
	public float getHeight() { return collisionBox.getHeight(); }
	public void setWidth(float w) { collisionBox.setWidth(w); }
	public void setHeight(float h) { collisionBox.setHeight(h); }
    public void setSize(float size){ setWidth(size); setHeight(size); }
    public void setSize(float w, float h){ setWidth(w); setHeight(h); }
	
	public int getActualAnimation() { return actualAnimation; }

	public long getTimeOffGround() {
		return onGround ? 0 : System.currentTimeMillis() - lastTimeOnGround;
	}

	public void onCollision(Direction dir, Entity other) {}
    public void reset(){}
}