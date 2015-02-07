package com.makzk.games.entities;

import static com.makzk.games.util.Animations.ANIMATION_FALL;
import static com.makzk.games.util.Animations.ANIMATION_JUMP;
import static com.makzk.games.util.Animations.ANIMATION_RUN;
import static com.makzk.games.util.Animations.ANIMATION_STAND;
import static com.makzk.games.util.Animations.ANIMATION_TOTAL;
import static com.makzk.games.util.Direction.EAST;
import static com.makzk.games.util.Direction.NORTH;
import static com.makzk.games.util.Direction.SOUTH;
import static com.makzk.games.util.Direction.WEST;

import java.util.Arrays;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Rectangle;

import com.makzk.games.Level;
import com.makzk.games.Main;
import com.makzk.games.util.Animations;
import com.makzk.games.util.Camera;
import com.makzk.games.util.Direction;

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
		
		Animations anim = null;

		if(speedY < -.1) {
			// La entity está saltando
			anim = ANIMATION_JUMP;
		} else if (speedY > .2) {
			// La entity esta cayendo
			anim = ANIMATION_FALL;
		} else if (speedX != 0) {
			// La entity está yendo hacia los lados, pero no saltando (else if)
			anim = getTimeOffGround() < 100 ? ANIMATION_RUN : ANIMATION_FALL;
		} else {
			// La entity está detenida
			anim = getTimeOffGround() < 100 ? ANIMATION_STAND : ANIMATION_FALL;
		}
	
		actualAnimation = anim.ordinal();
		
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
	public void move(int delta, Level lv) {
		if(!enabled) {
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

	public void resolveCollisions(Entity e) {
		// Si el elemento no es sólido, no hay qué revisar
		if(!e.isSolid() || !e.isEnabled()) {
			return;
		}
		
		if(nextX <= e.getMaxX() && nextX + getWidth() > e.getX()) {
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
		
		if((getY() < e.getMaxY()) && (getY() + getHeight() > e.getY())) {
			float hdif = (getY() + getHeight()) - e.getY();
			boolean slope = hdif > 2;
			// Colisión derecha
			if(getX() + getWidth() <= e.getX() && nextX + getWidth() > e.getX()) {
				if(slope) {
					nextX = e.getX() - getWidth();
					speedX = 0;
					wall = true;
					onCollision(EAST, e);
				} else {
					nextY -= hdif;
				}
			}
			// Colisión izquierda
			else if(e.getMaxX() <= getX() && e.getMaxX() > nextX) {
				if(slope) {
					nextX = getX();
					speedX = 0;
					onCollision(WEST, e);
					wall = true;
				} else {
					nextY -= hdif;
				}
			}
		}
	}
	
	public void move(int delta) {
		move(delta, null);
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
	
	public int getActualAnimation() { return actualAnimation; }

	public long getTimeOffGround() {
		return onGround ? 0 : System.currentTimeMillis() - lastTimeOnGround;
	}

	@Override
	public void onCollision(Direction dir, Entity other) {};
}