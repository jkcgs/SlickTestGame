package com.makzk.games.entities;

import static com.makzk.games.util.PlayerAnimations.ANIMATION_FALL;
import static com.makzk.games.util.PlayerAnimations.ANIMATION_RUN;
import static com.makzk.games.util.PlayerAnimations.ANIMATION_STAND;

import java.util.Iterator;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Rectangle;

import com.makzk.games.Level;
import com.makzk.games.util.Direction;

public class Enemy extends EntityRect {
	private float initialX;
	private float initialY;
	private float walkSpeed = .1f;

	public Enemy(GameContainer gc, Rectangle rect, Level level) throws SlickException {
		super(gc, rect, level);
		gravity = true;
		solid = true;

		// Configurar animaciones
		SpriteSheet sprite = new SpriteSheet("data/sprites/bugs_walk.png", 43, 82);
		setupAnimation(sprite, ANIMATION_STAND, new int[]{0}, 1000);
		setupAnimation(sprite, ANIMATION_RUN, 0, 0, 4, 0, 50);
		setupAnimation(sprite, ANIMATION_FALL, new int[]{0}, 1000);
		
		speedX = walkSpeed;
		initialX = rect.getX();
		initialY = rect.getY();
	}
	public Enemy(GameContainer gc, Rectangle rect) throws SlickException {
		this(gc, rect, null);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void onCollision(Direction dir, EntityRect other, Iterator it) {
		if(dir == Direction.EAST) {
			speedX = -walkSpeed;
		}
		if(dir == Direction.WEST) {
			speedX = walkSpeed;
		}
	}
	
	public void reset() {
		speedX = .1f;
		speedY = 0;
		rect.setX(initialX);
		rect.setY(initialY);
	}
}
