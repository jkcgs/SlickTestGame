package com.makzk.games.entities;

import static com.makzk.games.util.Animations.ANIMATION_FALL;
import static com.makzk.games.util.Animations.ANIMATION_JUMP;
import static com.makzk.games.util.Animations.ANIMATION_RUN;
import static com.makzk.games.util.Animations.ANIMATION_STAND;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Rectangle;

import com.makzk.games.Main;
import com.makzk.games.elements.Level;
import com.makzk.games.util.Direction;

public class Player extends EntityRect {

	private int controlLeft = Input.KEY_A;
	private int controlRight = Input.KEY_D;
	private int controlJump = Input.KEY_SPACE;
	private float speed = .5f;
	private float acceleration = .05f;
	private float jumpImpulse = 1f;
	private float initialX;
	private float initialY;

	public Player(GameContainer gc, Main game, Rectangle rect) throws SlickException {
		super(gc, game, rect);
		gravity = true;
		initialX = rect.getX();
		initialY = rect.getY();
		setColor(Color.gray);
		
		// Configurar animaciones
		float[] drawrect = new float[]{ 0, -5, 102, 110 };
		float[] drawrect2 = new float[]{ -34, -5, 102, 110 };
		float[] drawrect3 = new float[]{ -34, 0, 102, 105 };
		float[] drawrect4 = new float[]{ -10, 0, 102, 105 };
		
		SpriteSheet spriteIddle = new SpriteSheet("data/sprites/iddle1.png", 410, 425);
		SpriteSheet spriteJump = new SpriteSheet("data/sprites/jump.png",404,458);
		SpriteSheet spriteFall = new SpriteSheet("data/sprites/fall.png",458,461);
		SpriteSheet spriteRun = new SpriteSheet("data/sprites/Run.png",464,390);
		setupAnimation(spriteIddle, ANIMATION_STAND, new int[]{0,1}, 500, drawrect, drawrect2);
		setupAnimation(spriteRun, ANIMATION_RUN, new int[]{0,1,2,3,4}, 70, drawrect3, drawrect4);
		setupAnimation(spriteJump, ANIMATION_JUMP, new int[]{0}, 200, drawrect, drawrect2);
		setupAnimation(spriteFall, ANIMATION_FALL, new int[]{0}, 200, drawrect, drawrect2);
		
		if(game != null) {
			game.sndManager.add("horn", "data/sounds/horn.ogg");
		}
	}

	public Player(GameContainer gc, Main game, float initialX, float initialY) throws SlickException {
		this(gc, game, new Rectangle(initialX, initialY, 50, 105));
	}

	public Player(GameContainer gc, Main game) throws SlickException {
		this(gc, game, new Rectangle(0, 0, 50, 105));
	}

	public Player(GameContainer gc, Main game, Level level) throws SlickException {
		this(gc, game, new Rectangle(level.getPjInitialX(), level.getPjInitialY(), 50, 105));
		setLevel(level);
	}
	
	public Player(GameContainer gc, Level level) throws SlickException {
		this(gc, null, new Rectangle(level.getPjInitialX(), level.getPjInitialY(), 50, 105));
		setLevel(level);
	}
	
	/**
	 * Mover el Player dentro del nivel según los controles presionados
	 * y su posición actual
	 */
	public void move(int delta, Level lv) {
		super.move(delta, lv);
		
		// Reiniciar al caer hacia el olvido
		if(getY() > lv.getHeight()) {
			reset();
		}
		
		Input in = gc.getInput();
		
		if(in.isKeyDown(controlLeft)) {
			speedX -= acceleration;
			if(speedX < -speed) {
				speedX = -speed;
			}
		}
		if(in.isKeyDown(controlRight)) {
			speedX += acceleration;
			if(speedX > speed) {
				speedX = speed;
			}
		}

		if(in.isKeyDown(controlJump)) {
			if(speedY == 0 && onGround) {
				speedY = -jumpImpulse;
			}
		} else if(speedY < 0) {
			// Terminar impulso de salto si no está
			// presionada la tecla para saltar
			speedY -= speedY / 10;
		}

		if(!in.isKeyDown(controlLeft) 
				&& !in.isKeyDown(controlRight)){
			if(speedX != 0) {
				if(speedX > 0) {
					speedX -= acceleration;
					if(speedX < 0) {
						speedX = 0;
					}
				} else {
					speedX += acceleration;
					if(speedX > 0) {
						speedX = 0;
					}
				}
			}
		}
	}
	public void move(int delta) {
		this.move(delta, level);
	}
	
	/**
	 * Reiniciar al jugador a su posición y velocidad inicial (0)
	 */
	public void reset() {
		speedX = 0;
		speedY = 0;
		setX(initialX);
		setY(initialY);
	}
	
	@Override
	public void onCollision(Direction dir, Entity other) {
		if(other instanceof Enemy) {
			if(dir == Direction.SOUTH) {
				// Collision with enemy!
				speedY = gc.getInput().isKeyDown(controlJump) ? -.7f : -.5f;
				other.setEnabled(false);

				if(game != null) {
					game.sndManager.play("horn", .2f);
				}
			}
		}
	}
}
