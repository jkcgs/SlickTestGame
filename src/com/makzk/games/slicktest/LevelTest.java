package com.makzk.games.slicktest;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import com.makzk.games.Level;
import com.makzk.games.Main;
import com.makzk.games.entities.Player;
import com.makzk.games.util.Camera;

public class LevelTest extends BasicGameState {
	private int state;
	
	Level level;
	Camera cam;
	Player player;
	StateBasedGame game;
	
	public LevelTest(int state) { this.state = state; }

	@Override
	public void init(GameContainer gc, StateBasedGame game) throws SlickException {
		level = Level.loadFromFile("data/levels/level3.json", gc);
		cam = new Camera(gc, level);
		player = new Player(gc, level);
		this.game = game;
	}

	@Override
	public void render(GameContainer gc, StateBasedGame game, Graphics g)
			throws SlickException {
		level.drawAll(g, cam);
		player.draw(cam);
		g.setColor(Color.white);
		g.drawString(System.currentTimeMillis()+"", 10, 10);
		g.drawString(String.format("[PJ] X: %.1f Y: %.1f sX: %.2f sY: %.2f g: %s t: %s w: %s a: %s", 
				player.getX(), player.getY(), player.getSpeedX(), player.getSpeedY(), 
				player.isOnGround(), player.getTimeOffGround(), 
				player.isTouchingWall(), player.getActualAnimation()), 10, 30);
		g.drawString(String.format("[Cam] X: %.1f Y: %.1f", cam.getX(), cam.getY()), 10, 50);
	}

	@Override
	public void update(GameContainer gc, StateBasedGame game, int delta)
			throws SlickException {
		if(gc.getInput().isKeyDown(Input.KEY_P)) {
			game.enterState(Main.pong);
			return;
		}
		if(gc.getInput().isKeyDown(Input.KEY_ESCAPE)) {
			game.enterState(Main.menu);
			return;
		}

		player.move(delta);
		level.updateEntities(delta);

		float pjXOnScreen = player.getCollisionBox().getCenterX();
		float pjYOnScreen = player.getCollisionBox().getCenterY();
		cam.autoMove(pjXOnScreen, pjYOnScreen);
	}

	public void keyPressed(int key, char c) {
		// Reiniciar la posición del jugador y los enemigos
		if(key == Input.KEY_R) {
			level.reset(player);
		}
	}

	@Override
	public int getID() {
		return state;
	};
}
