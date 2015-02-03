package com.makzk.games.util;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.geom.Rectangle;

import com.makzk.games.Level;

public class Camera {
	private Rectangle rect;
	private Level level;
	private boolean keepOnLevel = true;
	
	public Camera(Rectangle rect, Level level) {
		this.rect = rect;
		this.level = level;
	}
	
	public Camera(GameContainer gc, Level level) {
		this.rect = new Rectangle(0, 0, gc.getWidth(), gc.getHeight());
		this.level = level;
	}

	public Rectangle getRect() {
		return rect;
	}

	public void setRect(Rectangle rect) {
		this.rect = rect;
	}
	
	public void moveX(float x) {
		rect.setX(rect.getX() + x);
	}
	
	public void moveY(float x) {
		rect.setX(rect.getX() + x);
	}
	
	public void setX(float x) {
		rect.setX(x);
		if (keepOnLevel){
			if(rect.getX() < 0){
				rect.setX(0);
			}else if(rect.getX() + rect.getWidth() > level.getWidth()){
				rect.setX(level.getWidth() - rect.getWidth());
			}
			
		}
	}
	public void setY(float y) {
		rect.setY(y);
		if (keepOnLevel){
			if(rect.getY() < 0){
				rect.setY(0);
			}else if(rect.getY() + rect.getHeight() > level.getHeight()){
				rect.setY(level.getHeight() - rect.getHeight());
			}
			
		}
	}

	public float getX() {
		return rect.getX();
	}
	
	public float getY() {
		return rect.getY();
	}
	
	public void autoMove(float x, float y) {

		setX(x-(rect.getWidth()/2));
		
		setY(y-(rect.getHeight()/2));

		/*                ***Notas***
		 * Crear Mapas con la propiedad Tall, si el nivel tiene dicha propiedad en false, no ajustar el eje y
		 * Si el nivel tiene dicha propiedad en true, ajustar la posicion en Y al pasar la mitad de la pantalla.
		 * 
		 * Luego de elevar la camara esta se debe ajustar es decir luego de que el jugador alcanze la velocidad en
		 * Y a 0, la camara debera decender de manera proporsional con el jugador hasta que este toque piso.
		 * 
		 * 
		 */
	}
}
