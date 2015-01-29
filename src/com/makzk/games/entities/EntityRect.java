package com.makzk.games.entities;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.geom.Rectangle;

import com.makzk.games.Level;
import com.makzk.games.util.Direction;
import static com.makzk.games.util.Direction.*;

public class EntityRect extends Entity {
	protected Rectangle rect;
	protected Color color = Color.transparent;
	protected boolean keepOnScreen = true;
	protected boolean gravity = false;
	protected float gravityImpulse = .005f;
	protected boolean solid = true;
	protected boolean onGround = false;

	public EntityRect(GameContainer gc, Rectangle rect) {
		super(gc);
		this.rect = rect;
	}
	
	/**
	 * Dibuja el elemento sólo si no se trata de un rectángulo transparente
	 */
	public void draw() {
		if(color != Color.transparent) {
			gc.getGraphics().setColor(color);
			gc.getGraphics().fill(rect);
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
					} else if(collisionSide(r) == SOUTH) {
						// Si hay colisión por abajo, como con algún piso.
						speedY = 0; // Detener movimiento y colocar encima
						rect.setY(r.getRect().getY() - rect.getHeight());
						southCollision = true;
					}
					
				}
				if(collisionInX(r)) {
					// Si hay colisión por los lados, se detiene el movimiento lateral
					speedX = 0;
					if(collisionSide(r) == WEST) {
						rect.setX(r.getRect().getX() + r.getRect().getWidth());
					} else {
						rect.setX(r.getRect().getX() - rect.getWidth());
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
}
