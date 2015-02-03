package com.makzk.games.entities;

import static com.makzk.games.util.Direction.EAST;
import static com.makzk.games.util.Direction.NORTH;
import static com.makzk.games.util.Direction.SOUTH;
import static com.makzk.games.util.Direction.WEST;
import static com.makzk.games.util.PlayerAnimations.ANIMATION_FALL;
import static com.makzk.games.util.PlayerAnimations.ANIMATION_JUMP;
import static com.makzk.games.util.PlayerAnimations.ANIMATION_RUN;
import static com.makzk.games.util.PlayerAnimations.ANIMATION_STAND;
import static com.makzk.games.util.PlayerAnimations.ANIMATION_TOTAL;

import java.util.Arrays;
import java.util.Iterator;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Rectangle;

import com.makzk.games.Level;
import com.makzk.games.util.Camera;
import com.makzk.games.util.Direction;
import com.makzk.games.util.PlayerAnimations;

public class EntityRect extends Entity {
	protected Rectangle collisionBox;
	protected float[][] drawBoxes = new float[ANIMATION_TOTAL.ordinal()][4];
	protected float[][] drawBoxesFlipped = new float[ANIMATION_TOTAL.ordinal()][4];
	protected Level level;
	protected Color color = Color.transparent;
	protected boolean keepOnScreen = false;
	protected boolean gravity = false;
	protected float gravityImpulse = .03f;
	protected boolean solid = true;
	protected boolean onGround = false;
	protected boolean wall = false;
	protected Animation[] animations = new Animation[ANIMATION_TOTAL.ordinal()];
	protected int actualAnimation = ANIMATION_STAND.ordinal();
	protected boolean spriteFlipHorizontal = false;
	protected float nextX = 0;
	protected float nextY = 0;
	protected boolean enabled = true;

	public EntityRect(GameContainer gc, Rectangle collisionBox, Level level) {
		super(gc);
		this.collisionBox = collisionBox;
		this.level = level;
	}

	public EntityRect(GameContainer gc, Rectangle collisionBox) {
		this(gc, collisionBox, null);
	}

	/**
	 * Configurar una animaci贸n para una posici贸n determinada, determinado
	 * por un rango de posiciones de la grilla de sprites del SpriteSheet
	 * @param sprite El SpriteSheet que contiene los sprites
	 * @param anim La posici贸n a configurar
	 * @param x1 El primer elemento en X de la grilla de sprites del SpriteSheet
	 * @param y1 El primer elemento en Y de la grilla de sprites del SpriteSheet
	 * @param x2 El 煤ltimo elemento en X de la grilla de sprites del SpriteSheet
	 * @param y2 El 煤ltimo elemento en Y de la grilla de sprites del SpriteSheet
	 * @param duration La duraci髇 de cada animaci髇
	 */
	public void setupAnimation(SpriteSheet sprite, PlayerAnimations anim, 
			int x1, int y1, int x2, int y2, int duration, float[] drawBox, float[] drawBoxFlipped) {
		animations[anim.ordinal()] = new Animation(sprite, x1, y1, x2, y2, true, duration, true);
		this.drawBoxes[anim.ordinal()] = drawBox;
		this.drawBoxesFlipped[anim.ordinal()] = drawBoxFlipped;
	}

	public void setupAnimation(SpriteSheet sprite, PlayerAnimations anim, 
			int x1, int y1, int x2, int y2, int duration, float[] drawBox) {
		setupAnimation(sprite, anim, x1, y1, x2, y2, duration, drawBox, null);
	}

	public void setupAnimation(SpriteSheet sprite, PlayerAnimations anim, 
			int x1, int y1, int x2, int y2, int duration) {
		setupAnimation(sprite, anim, x1, y1, x2, y2, duration, null, null);
	}
	
	/**
	 * Configurar una animaci贸n para una posici贸n determinada, determinado
	 * por una o varias posiciones de la grilla de sprites del SpriteSheet, 
	 * s贸lo en la primera fila de 茅ste
	 * @param sprite El SpriteSheet que contiene los sprites
	 * @param anim La posici贸n a configurar
	 * @param xpositions Las posiciones en X a usar
	 * @param duration La duraci贸n de cada posici贸n
	 * @param drawBox La posici髇 y tama駉 de la animaci髇 y sprite. Las coordenadas del
	 * rect醤gulo se usan como offset en base a la posici髇 del objeto, y no como posici髇 absoluta.
	 * @param drawBoxFlipped Lo mismo para drawBox, pero para su animaci髇 invertida horizontalmente.
	 */
	public void setupAnimation(SpriteSheet sprite, PlayerAnimations anim, int[] xpositions, 
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
	 * Configurar una animaci贸n para una posici贸n determinada, determinado
	 * por una o varias posiciones de la grilla de sprites del SpriteSheet, 
	 * s贸lo en la primera fila de 茅ste
	 * @param sprite El SpriteSheet que contiene los sprites
	 * @param anim La posici贸n a configurar
	 * @param xpositions Las posiciones en X a usar
	 * @param duration La duraci贸n de cada posici贸n
	 * @param drawBox La posici髇 y tama駉 de la animaci髇 y sprite. Las coordenadas del
	 * rect醤gulo se usan como offset en base a la posici髇 del objeto, y no como posici髇 absoluta.
	 */
	public void setupAnimation(SpriteSheet sprite, PlayerAnimations anim, 
			int[] xpositions, int duration, float[] drawBox) {
		setupAnimation(sprite, anim, xpositions, duration, drawBox, null);
	}
	
	/**
	 * Configurar una animaci贸n para una posici贸n determinada, determinado
	 * por una o varias posiciones de la grilla de sprites del SpriteSheet, 
	 * s贸lo en la primera fila de 茅ste
	 * @param sprite El SpriteSheet que contiene los sprites
	 * @param anim La posici贸n a configurar
	 * @param xpositions Las posiciones en X a usar
	 * @param duration La duraci贸n de cada posici贸n
	 */
	public void setupAnimation(SpriteSheet sprite, PlayerAnimations anim, int[] xpositions, int duration) {
		setupAnimation(sprite, anim, xpositions, duration, null, null);
	}
	
	/**
	 * Dibuja el elemento s贸lo si no se trata de un rect谩ngulo transparente
	 */
	public void draw() {
		draw(null);
	}

	/**
	 * Dibuja el elemento s贸lo si no se trata de un rect谩ngulo transparente
	 * en base a la posici贸n de una c谩mara
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
		
		if(speedY < 0) {
			// La entity est谩 saltando
			actualAnimation = ANIMATION_JUMP.ordinal();
		} else if (!onGround && speedY > 0) {
			// La entity esta cayenda
			actualAnimation = ANIMATION_FALL.ordinal();
		} else if(speedX != 0) {
			// La entity est谩 yendo hacia los lados, pero no saltando (else if)
			actualAnimation = ANIMATION_RUN.ordinal();
		} else {
			// La entity est谩 detenida
			actualAnimation = ANIMATION_STAND.ordinal();
		}
	
		// Dibujar el sprite, con su volteo correspondiente si corresponde, y
		// seg煤n el rect谩ngulo del elemento
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
			} else if(dbox != null || 
				( spriteFlipHorizontal && dbf == null && dbox != null )
			) {
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
	 * @param level El nivel que contiene otras entidades
	 * @param delta Delta de diferencia de rendimiento del juego para no variar la velocidad
	 */
	public void move(Level level, int delta) {
		if(!enabled) {
			return;
		}

		nextY = speedY * delta + getY();
		nextX = speedX * delta + getX();
		onGround = false;

		if(solid && level != null) {
			wall = false;

			// Revisar colisiones con elementos del nivel
			for (EntityRect r : level.getRects()) {
			    resolveCollisions(r);
			}
			for (Enemy r : level.getEnemies()) {
			    resolveCollisions(r);
			}
		}

		setY(nextY);
		setX(nextX);

		// Si es necesario mantener el objeto en pantalla, cualquier movimiento
		// fuera de esta, har谩 que se retorne al 煤ltimo borde alcanzado.
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

		// Gravedad
		if(gravity && !onGround) {
			speedY += gravityImpulse;
			// Limitar gravedad a 1
			if(speedY > 1) {
				speedY = 1;
			}
		}

		if(animations[actualAnimation] != null) {
			animations[actualAnimation].update(delta);
		}
	}

	public void resolveCollisions(EntityRect r) {
		// Si el elemento no es s贸lido, no hay qu茅 revisar
		if(!r.isSolid() || !r.isEnabled()) {
			return;
		}
		
		if(getX() < r.getX() + r.getWidth() && getX() > r.getX() - getWidth()) {
			// Colisi贸n abajo
			if(getY() + getHeight() <= r.getY() && nextY + getHeight() >= r.getY()) {
				nextY = r.getY() - getHeight();
				speedY = 0;
				onGround = true;
				onCollision(SOUTH, r);
			}
			// Colisi贸n arriba
			else if(r.getY() + r.getHeight() <= getY() && r.getY() + r.getHeight() > nextY) {
				nextY = r.getY() + r.getHeight();
				speedY = 0;
				onCollision(NORTH, r);
			}
		}
		
		if((getY() < r.getY() + r.getHeight()) && (getY() + getHeight() > r.getY())) {
			// Colisi贸n derecha
			if(getX() + getWidth() <= r.getX() && nextX + getWidth() > r.getX()) {
				nextX = r.getX() - getWidth();
				speedX = 0;
				wall = true;
				onCollision(EAST, r);
			}
			// Colisi贸n izquierda
			else if(r.getX() + r.getWidth() <= getX() && r.getX() + r.getWidth() > nextX) {
				nextX = r.getX() + r.getWidth();
				speedX = 0;
				onCollision(WEST, r);
				wall = true;
			}
		}
	}
	
	public void move(int delta) {
		move(null, delta);
	}
	
	/**
	 * Obtener el lado de la colisi贸n con otro objeto.
	 * Obtenido desde: http://gamedev.stackexchange.com/a/29796
	 * @param other El otro elemento a revisar
	 * @return La direcci贸n de colisi贸n. Si no hubo colisi贸n se retorna Direction.NONE
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

	public void destroy() {
		if(level != null) {
			level.getEnemies().remove(this);
		}
	}

	// Propiedades fisicas
	public boolean isGravityAffected() { return gravity; }
	public boolean isOnGround() { return onGround; }
	public boolean isSolid() { return solid; }
	public boolean isTouchingWall() { return wall; }

	// get rekt m7
	public Rectangle getCollisionBox() { return collisionBox; }
	public void setCollisionBox(Rectangle collisionBox) { this.collisionBox = collisionBox; }

	// Shorthands para posici贸n y movimiento
	public float getX() { return collisionBox.getX(); }
	public float getMaxX() { return collisionBox.getMaxX(); }
	public float getY() { return collisionBox.getY(); }
	public float getMaxY() { return collisionBox.getMaxY(); }
	public void setX(float x) { collisionBox.setX(x); }
	public void setY(float y) { collisionBox.setY(y); }
	public void moveX(float x) { setX(getX() + x); }
	public void moveY(float y) { setY(getY() + y); }
	public void movePos(float x, float y) { moveX(x); moveY(y); }

	// Shorthands de tama帽o
	public float getWidth() { return collisionBox.getWidth(); }
	public float getHeight() { return collisionBox.getHeight(); }
	public void setWidth(float w) { collisionBox.setWidth(w); }
	public void setHeight(float h) { collisionBox.setHeight(h); }
	public void setSize(float size){ setWidth(size); setHeight(size); }

	// Color
	public Color getColor() { return color; }
	public void setColor(Color color) { this.color = color; }
	
	public void setLevel(Level level) { this.level = level; }
	public Level getLevel(){ return level; } 
	
	public void setEnabled(boolean enabled) { this.enabled = enabled; }
	public boolean isEnabled() { return enabled; }

	@SuppressWarnings("rawtypes")
	public void onCollision(Direction dir, EntityRect other, Iterator it) {}
	public void onCollision(Direction dir, EntityRect other) { onCollision(dir, other, null); }
}
