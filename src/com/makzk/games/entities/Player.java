package com.makzk.games.entities;

import com.makzk.games.Main;
import com.makzk.games.elements.Level;
import com.makzk.games.util.Direction;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;

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
        isStatic = false;
		initialX = rect.getX();
		initialY = rect.getY();

        setupFromConfig("player");
		
		if(game != null) {
			game.sndManager.add("horn", "data/sounds/Jump.ogg");
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

	public int getControlLeft() { return controlLeft; }
	public int getControlRight() { return controlRight; }
	public int getControlJump() { return controlJump; }

    public float getInitialX() { return initialX; }
    public void setInitialX(float initialX) { this.initialX = initialX; }
    public float getInitialY() { return initialY; }
    public void setInitialY(float initialY) { this.initialY = initialY; }
}
