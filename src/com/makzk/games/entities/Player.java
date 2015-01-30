package com.makzk.games.entities;

import static com.makzk.games.util.PlayerAnimations.ANIMATION_JUMP;
import static com.makzk.games.util.PlayerAnimations.ANIMATION_RUN;
import static com.makzk.games.util.PlayerAnimations.ANIMATION_STAND;
import static com.makzk.games.util.PlayerAnimations.ANIMATION_TOTAL;
import static com.makzk.games.util.PlayerAnimations.ANIMATION_FALL;

import java.util.Arrays;

import org.newdawn.slick.Animation;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Rectangle;

import com.makzk.games.Level;
import com.makzk.games.util.Camera;
import com.makzk.games.util.PlayerAnimations;

public class Player extends EntityRect {

	private int controlLeft = Input.KEY_A;
	private int controlRight = Input.KEY_D;
	private int controlJump = Input.KEY_SPACE;
	private float speed = 0.5f;
	private float jumpImpulse = 1.6f;
	private float initialX;
	private float initialY;
	private Animation[] animations = new Animation[ANIMATION_TOTAL.ordinal()];
	private int actualAnimation = ANIMATION_STAND.ordinal();
	private boolean spriteFlipHorizontal = false;

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
	 * Mover el Player dentro del nivel según los controles presionados
	 * y su posición actual
	 */
	public void move(Level level, int delta) {
		super.move(level, delta);
		animations[actualAnimation].update(delta);
		
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
	
	/**
	 * Dibuja al jugador en pantalla, incluyendo sus animaciones que son
	 * determinadas según la velocidad del jugador
	 */
	public void draw() {
		draw(null);
	}

	public void draw(Camera cam) {
		super.draw(cam);
		
		// Se determina si el jugador ha ido a la izquierda o no, para 
		// voltear al sprite
		if(speedX > 0) {
			spriteFlipHorizontal = true;
		} else if(speedX < 0) {
			spriteFlipHorizontal = false;
		}
		
		if(speedY < 0) {
			// El jugador está saltando
			actualAnimation = ANIMATION_JUMP.ordinal();
		} else if(speedX != 0) {
			// El jugador está yendo hacia los lados, pero no saltando (else if)
			actualAnimation = ANIMATION_RUN.ordinal();
			animations[actualAnimation].setSpeed(Math.abs(speedY));
		} else if (!isOnGround() && speedY > 0) {
			//El jugador esta cayendo
			actualAnimation = ANIMATION_FALL.ordinal();
		} else {
			// El jugador está detenido
			actualAnimation = ANIMATION_STAND.ordinal();
		}

		// Dibujar el sprite, con su volteo correspondiente si corresponde, y
		// según el rectángulo del elemento
		// TODO: Agregar un rectángulo distinto para el sprite, en vez del usado para colisiones
		if(cam == null) {
			animations[actualAnimation].getCurrentFrame().getFlippedCopy(spriteFlipHorizontal, false)
			.draw(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
		} else {
			animations[actualAnimation].getCurrentFrame().getFlippedCopy(spriteFlipHorizontal, false)
			.draw(rect.getX() - cam.getX(), rect.getY() - cam.getY(), rect.getWidth(), rect.getHeight());
		}
	}
}
