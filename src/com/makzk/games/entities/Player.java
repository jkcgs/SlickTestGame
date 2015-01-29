package com.makzk.games.entities;

import static com.makzk.games.util.PlayerAnimations.ANIMATION_STAND;
import static com.makzk.games.util.PlayerAnimations.ANIMATION_TOTAL;

import java.util.Arrays;

import org.newdawn.slick.Animation;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Rectangle;

import com.makzk.games.Level;
import com.makzk.games.util.PlayerAnimations;

public class Player extends EntityRect {

	private int controlLeft = Input.KEY_A;
	private int controlRight = Input.KEY_D;
	private int controlJump = Input.KEY_SPACE;
	private int controlReset = Input.KEY_R;
	private float speed = 0.5f;
	private float jumpImpulse = 1.0f;
	private float initialX;
	private float initialY;
	private Animation[] animations = new Animation[ANIMATION_TOTAL.ordinal()];
	private int actualAnimation = ANIMATION_STAND.ordinal();

	public Player(GameContainer gc, Rectangle rect) throws SlickException {
		super(gc, rect);
		gravity = true;
		keepOnScreen = false;
		initialX = rect.getX();
		initialY = rect.getY();
		
		SpriteSheet sprite = new SpriteSheet("data/sprites/dave.png", 20, 40);
		setupAnimation(sprite, ANIMATION_STAND, new int[]{0,1,2,3,2,1}, 200);
	}
	
	public void setupAnimation(SpriteSheet sprite, PlayerAnimations anim, int x1, int y1, int x2, int y2, int duration) {
		animations[anim.ordinal()] = new Animation(sprite, x1, y1, x2, y2, true, duration, true);
	}
	
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
	
	public void move(Level level, int delta) {
		super.move(level, delta);
		
		if(rect.getY() > gc.getHeight()) {
			reset();
		}
		if(rect.getX() < 0) {
			rect.setX(gc.getWidth() - 1);
		}
		if(rect.getX() >= gc.getWidth()) {
			rect.setX(0);
		}
		
		Input in = gc.getInput();
		
		if(in.isKeyDown(controlReset)) {
			speedX = 0f;
			reset();
		}
		
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
	
	public void reset() {
		rect.setX(initialX);
		rect.setY(initialY);
	}
	
	public void draw() {
		super.draw();
		
		animations[actualAnimation].draw(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
	}
}
