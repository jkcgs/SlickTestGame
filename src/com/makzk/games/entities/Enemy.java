package com.makzk.games.entities;

import static com.makzk.games.util.PlayerAnimations.ANIMATION_FALL;
import static com.makzk.games.util.PlayerAnimations.ANIMATION_RUN;
import static com.makzk.games.util.PlayerAnimations.ANIMATION_STAND;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.util.Log;

import com.makzk.games.Level;
import com.makzk.games.util.Direction;

public class Enemy extends EntityRect {
	private float initialX;
	private float initialY;
	private float walkSpeed = .1f;

	public Enemy(GameContainer gc, Rectangle rect, Level level) {
		super(gc, rect, level);
		gravity = true;
		solid = true;
		keepOnScreen = false;

		// Configurar animaciones
		try {
			SpriteSheet sprite = new SpriteSheet("data/sprites/bugs_walk.png", 43, 82);
			setupAnimation(sprite, ANIMATION_STAND, new int[]{0}, 1000);
			setupAnimation(sprite, ANIMATION_RUN, 0, 0, 4, 0, 70);
			setupAnimation(sprite, ANIMATION_FALL, new int[]{0}, 1000);
		} catch (SlickException e) {
			Log.error(e);
		}
		
		speedX = walkSpeed;
		initialX = rect.getX();
		initialY = rect.getY();
	}
	public Enemy(GameContainer gc, Rectangle rect) throws SlickException {
		this(gc, rect, null);
	}

	@Override
	public void onCollision(Direction dir, Entity other) {
		if(other instanceof EntityRect) {
			if(dir == Direction.EAST) {
				speedX = -walkSpeed;
			}
			if(dir == Direction.WEST) {
				speedX = walkSpeed;
			}
		}
	}
	
	public void reset() {
		speedX = .1f;
		speedY = 0;
		collisionBox.setX(initialX);
		collisionBox.setY(initialY);
		enabled = true;
	}
}
