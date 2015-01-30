package com.makzk.games.entities;

import static com.makzk.games.util.PlayerAnimations.ANIMATION_FALL;
import static com.makzk.games.util.PlayerAnimations.ANIMATION_JUMP;
import static com.makzk.games.util.PlayerAnimations.ANIMATION_RUN;
import static com.makzk.games.util.PlayerAnimations.ANIMATION_STAND;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Rectangle;

import com.makzk.games.Level;

public class Player extends EntityRect {

	private int controlLeft = Input.KEY_A;
	private int controlRight = Input.KEY_D;
	private int controlJump = Input.KEY_SPACE;
	private float speed = 0.5f;
	private float jumpImpulse = 1.6f;
	private float initialX;
	private float initialY;

	public Player(GameContainer gc, Rectangle rect) throws SlickException {
		super(gc, rect);
		gravity = true;
		keepOnScreen = false;
		initialX = rect.getX();
		initialY = rect.getY();
		
		// Configurar animaciones
		SpriteSheet spriteIddle = new SpriteSheet("data/sprites/iddle1.png", 410, 425);
		SpriteSheet spriteJump = new SpriteSheet("data/sprites/jump.png",404,458);
		SpriteSheet spriteFall = new SpriteSheet("data/sprites/fall.png",458,461);
		setupAnimation(spriteIddle, ANIMATION_STAND, new int[]{0,1}, 500);
		setupAnimation(spriteIddle, ANIMATION_RUN, new int[]{0,1}, 200);
		setupAnimation(spriteJump, ANIMATION_JUMP, new int[]{0}, 200);
		setupAnimation(spriteFall, ANIMATION_FALL, new int[]{0}, 200);
	}
	
	/**
	 * Mover el Player dentro del nivel según los controles presionados
	 * y su posición actual
	 */
	public void move(Level level, int delta) {
		super.move(level, delta);
		
		if(rect.getY() > gc.getHeight()) {
			reset();
		}
		
		Input in = gc.getInput();
		
		if(in.isKeyDown(controlLeft)) {
			speedX -= .01f;
			if(speedX < -speed) {
				speedX = -speed;
			}
		}
		if(in.isKeyDown(controlRight)) {
			speedX += .01f;
			if(speedX > speed) {
				speedX = speed;
			}
		}

		if(in.isKeyDown(controlJump)) {
			if(speedY == 0 && onGround) {
				speedY = -jumpImpulse;
			}
		}

		if(!in.isKeyDown(controlLeft) 
				&& !in.isKeyDown(controlRight)){
			if(speedX != 0) {
				if(speedX > 0) {
					speedX -= .01f;
					if(speedX < 0) {
						speedX = 0;
					}
				} else {
					speedX += .01f;
					if(speedX > 0) {
						speedX = 0;
					}
				}
			}
		}
	}
	
	/**
	 * Reiniciar al jugador a su posición y velocidad inicial (0)
	 */
	public void reset() {
		speedX = 0;
		speedY = 0;
		rect.setX(initialX);
		rect.setY(initialY);
	}
}
