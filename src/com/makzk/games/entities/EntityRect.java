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
	protected boolean keepOnScreen = true;
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
				gc.getGraphics().fillRect(rect.getX() - cam.getX(), rect.getY() - cam.getY(), 
						rect.getWidth(), rect.getHeight());
			}
		}

		// Se determina si la entity ha ido a la izquierda o no, para 
		// voltear al sprite
		if(speedX > 0) {
			spriteFlipHorizontal = true;
		} else if(speedX < 0) {
			spriteFlipHorizontal = false;
		}
		
		if(speedY < 0) {
			// La entity está saltando
			actualAnimation = ANIMATION_JUMP.ordinal();
		} else if(speedX != 0) {
			// La entity está yendo hacia los lados, pero no saltando (else if)
			actualAnimation = ANIMATION_RUN.ordinal();
			animations[actualAnimation].setSpeed(Math.abs(speedY));
		} else if (!isOnGround() && speedY > 0) {
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
				.draw(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
			} else {
				animations[actualAnimation].getCurrentFrame().getFlippedCopy(spriteFlipHorizontal, false)
				.draw(rect.getX() - cam.getX(), rect.getY() - cam.getY(), rect.getWidth(), rect.getHeight());
			}
		}
	}

	/**
	 * Realiza los movimientos del elemento basado en los elementos de un nivel
	 * @param level El nivel que contiene otras entidades
	 * @param delta Delta de diferencia de rendimiento del juego para no variar la velocidad
	 */
	public void move(Level level, int delta) {
		// Movimiento básico
		rect.setX(rect.getX() + (speedX * delta));
		rect.setY(rect.getY() + (speedY * delta));

		// Movimiento hacia abajo según la gravedad
		if(gravity) {
			speedY += gravityImpulse;
			// Limitar gravedad a 1
			if(speedY > 1) {
				speedY = 1;
			}
		}

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
						speedY = gravityImpulse; // empujar un poco hacia abajo
						rect.setY(r.getRect().getY() + r.getRect().getHeight());
						onCollision(NORTH, r);
					} else if(collisionSide(r) == SOUTH) {
						// Si hay colisión por abajo, como con algún piso.
						speedY = 0; // Detener movimiento y colocar encima
						rect.setY(r.getRect().getY() - rect.getHeight());
						southCollision = true;
						onCollision(SOUTH, r);
					}
					
				}
				if(collisionInX(r)) {
					// Si hay colisión por los lados, se detiene el movimiento lateral
					speedX = 0;
					if(collisionSide(r) == WEST) {
						rect.setX(r.getRect().getX() + r.getRect().getWidth());
						onCollision(WEST, r);
					} else {
						rect.setX(r.getRect().getX() - rect.getWidth());
						onCollision(EAST, r);
					}
				}
			}
			
			// Como hay colisión por abajo, el elemento está en el "suelo"
			onGround = southCollision;
		}

		// Si es necesario mantener el objeto en pantalla, cualquier movimiento
		// fuera de esta, hará que se retorne al último borde alcanzado.
		if(keepOnScreen) {
			if (rect.getX() < 0) {
				rect.setX(0);
			} else if(rect.getX() > (gc.getWidth() - rect.getWidth())) {
				rect.setX(gc.getWidth() - rect.getWidth());
			}
			
			if (rect.getY() < 0) {
				rect.setY(0);
			} else if(rect.getY() > (gc.getHeight() - rect.getHeight())) {
				rect.setY(gc.getHeight() - rect.getHeight());
			}
		}

		if(animations[actualAnimation] != null) {
			animations[actualAnimation].update(delta);
		}
	}
	
	public void move(int delta) {
		move(null, delta);
	}

	public Rectangle getRect() {
		return rect;
	}

	public void setRect(Rectangle rect) {
		this.rect = rect;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}
	
	/**
	 * Obtener el lado de la colisión con otro objeto.
	 * Obtenido desde: http://gamedev.stackexchange.com/a/29796
	 * @param other El otro elemento a revisar
	 * @return La dirección de colisión. Si no hubo colisión se retorna Direction.NONE
	 */
	public Direction collisionSide(EntityRect other) {
		float w = (float) (0.5 * (rect.getWidth() + other.getRect().getWidth()));
		float h = (float) (0.5 * (rect.getHeight() + other.getRect().getHeight()));
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

	public boolean isOnGround() {
		return onGround;
	}
	
	public boolean isSolid() {
		return solid;
	}
	
	public void onCollision(Direction dir, EntityRect other) {}
}
