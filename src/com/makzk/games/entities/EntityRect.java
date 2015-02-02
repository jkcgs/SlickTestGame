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
	protected Rectangle rect;
	protected Color color = Color.transparent;
	protected boolean keepOnScreen = false;
	protected boolean gravity = false;
	protected float gravityImpulse = .005f;
	protected boolean solid = true;
	protected boolean onGround = false;
	protected Animation[] animations = new Animation[ANIMATION_TOTAL.ordinal()];
	protected int actualAnimation = ANIMATION_STAND.ordinal();
	protected boolean spriteFlipHorizontal = false;

	public EntityRect(GameContainer gc, Rectangle rect) {
		super(gc);
		this.rect = rect;
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
	 * @param duration
	 */
	public void setupAnimation(SpriteSheet sprite, PlayerAnimations anim, int x1, int y1, int x2, int y2, int duration) {
		animations[anim.ordinal()] = new Animation(sprite, x1, y1, x2, y2, true, duration, true);
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
	public void setupAnimation(SpriteSheet sprite, PlayerAnimations anim, int[] xpositions, int duration) {
		int[] frames = new int[xpositions.length*2];
		int[] durations = new int[xpositions.length];
		
		Arrays.fill(durations, duration);
		for(int i = 0; i < xpositions.length; i++) {
			frames[i*2] = xpositions[i];
			frames[i*2+1] = 0;
		}

		animations[anim.ordinal()] = new Animation(sprite, frames, durations);
	}
	
	/**
	 * Dibuja el elemento sólo si no se trata de un rectángulo transparente
	 */
	public void draw() {
		Camera cam = null;
		draw(cam);
	}

	/**
	 * Dibuja el elemento sólo si no se trata de un rectángulo transparente
	 * en base a la posición de una cámara
	 */
	public void draw(Camera cam) {
		if(color != Color.transparent) {
			gc.getGraphics().setColor(color);
			if(cam == null) {
				gc.getGraphics().fill(rect);
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
			// La entity está saltando
			actualAnimation = ANIMATION_JUMP.ordinal();
		} else if(speedX != 0) {
			// La entity está yendo hacia los lados, pero no saltando (else if)
			actualAnimation = ANIMATION_RUN.ordinal();
			if(animations[actualAnimation] != null) {
				animations[actualAnimation].setSpeed(Math.abs(speedY));
			}
		} else if (!onGround && speedY > 0) {
			// La entity esta cayenda
			actualAnimation = ANIMATION_FALL.ordinal();
		} else {
			// La entity está detenida
			actualAnimation = ANIMATION_STAND.ordinal();
		}
	
		// Dibujar el sprite, con su volteo correspondiente si corresponde, y
		// según el rectángulo del elemento
		// TODO: Agregar un rectángulo distinto para el sprite, en vez del usado para colisiones
		if(animations[actualAnimation] != null) {
			if(cam == null) {
				animations[actualAnimation].getCurrentFrame().getFlippedCopy(spriteFlipHorizontal, false)
				.draw(getX(), getY(), getWidth(), getHeight());
			} else {
				animations[actualAnimation].getCurrentFrame().getFlippedCopy(spriteFlipHorizontal, false)
				.draw(getX() - cam.getX(), getY() - cam.getY(), getWidth(), getHeight());
			}
		}
	}

	/**
	 * Realiza los movimientos del elemento basado en los elementos de un nivel
	 * @param level El nivel que contiene otras entidades
	 * @param delta Delta de diferencia de rendimiento del juego para no variar la velocidad
	 */
	public void move(Level level, int delta) {
		// Guardar posiciones anteriores
		float prevX = getX();
		float prevY = getY();

		// Movimiento en eje X
		moveX(speedX * delta);
		if(solid && level != null) {
			// Revisar colisiones en el eje X con elementos del nivel
			for(EntityRect r: level.getRects()) {
				// Si el elemento no es sólido, no hay qué revisar
				if(!r.isSolid()) {
					continue;
				}

				if(collisionInX(r)) {
					if(collisionSide(r) == EAST) {
						onCollision(EAST, r);
					} else if (collisionSide(r) == WEST) {
						onCollision(WEST, r);
					}

					speedX = 0;
					setX(prevX);
				}
			}
		}
		
		// Movimiento en el eje Y
		if(gravity && !onGround) {
			speedY += gravityImpulse;
			// Limitar gravedad a 1
			if(speedY > 1) {
				speedY = 1;
			}
		}
		moveY(speedY * delta);
		// Movimiento hacia abajo según la gravedad sólo si no está en el suelo

		if(solid && level != null) {
			// Revisar si hay colisión por debajo
			boolean southCollision = false;
			
			// Revisar cada elemento del nivel
			for(EntityRect r: level.getRects()) {
				// Si el elemento no es sólido, no hay qué revisar
				if(!r.isSolid()) {
					continue;
				}

				if(collisionInY(r)) {
					// Si hay colisión por arriba (un "techo")
					if(collisionSide(r) == NORTH) {
						onCollision(NORTH, r);
						setY(prevY);
					} else if(collisionSide(r) == SOUTH) {
						southCollision = true;
						setY(r.getY() - getHeight());
						onCollision(SOUTH, r);
					}
					speedY = 0;
				}
			}
			
			// Como hay colisión por abajo, el elemento está en el "suelo"
			onGround = southCollision;
		}


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
	}
	
	public void move(int delta) {
		move(null, delta);
	}
	
	/**
	 * Obtener el lado de la colisión con otro objeto.
	 * Obtenido desde: http://gamedev.stackexchange.com/a/29796
	 * @param other El otro elemento a revisar
	 * @return La dirección de colisión. Si no hubo colisión se retorna Direction.NONE
	 */
	public Direction collisionSide(EntityRect other) {
		float w = (float) (0.5 * (getWidth() + other.getWidth()));
		float h = (float) (0.5 * (getHeight() + other.getHeight()));
		float dx = rect.getCenterX() - other.getRect().getCenterX();
		float dy = rect.getCenterY() - other.getRect().getCenterY();

		if (Math.abs(dx) <= w && Math.abs(dy) <= h)
		{
		    float wy = w * dy;
		    float hx = h * dx;

		    if (wy > hx) {
		        if (wy > -hx) return NORTH;
		        else return Direction.EAST;
		    } else {
		        if (wy > -hx) return WEST;
		        else return SOUTH;
		    }
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

	// Propiedades fisicas
	public boolean isGravityAffected() { return gravity; }
	public boolean isOnGround() { return onGround; }
	public boolean isSolid() { return solid; }

	// get rekt m7
	public Rectangle getRect() { return rect; }
	public void setRect(Rectangle rect) { this.rect = rect; }

	// Shorthands para posición y movimiento
	public float getX() { return rect.getX(); }
	public float getMaxX() { return rect.getMaxX(); }
	public float getY() { return rect.getY(); }
	public float getMaxY() { return rect.getMaxY(); }
	public void setX(float x) { rect.setX(x); }
	public void setY(float y) { rect.setY(y); }
	public void moveX(float x) { setX(getX() + x); }
	public void moveY(float y) { setY(getY() + y); }
	public void movePos(float x, float y) { moveX(x); moveY(y); }

	// Shorthands de tamaño
	public float getWidth() { return rect.getWidth(); }
	public float getHeight() { return rect.getHeight(); }
	public void setWidth(float w) { rect.setWidth(w); }
	public void setHeight(float h) { rect.setHeight(h); }
	public void setSize(float size){ setWidth(size); setHeight(size); }

	// Color
	public Color getColor() { return color; }
	public void setColor(Color color) { this.color = color; }
	
	public void onCollision(Direction dir, EntityRect other) {}
}
