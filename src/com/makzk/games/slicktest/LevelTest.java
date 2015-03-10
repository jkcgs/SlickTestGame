package com.makzk.games.slicktest;

import com.makzk.games.Main;
import com.makzk.games.elements.Level;
import com.makzk.games.util.Camera;
import com.makzk.games.util.Utils;
import org.newdawn.slick.*;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class LevelTest extends BasicGameState {
	private int state;
	
	Level level;
	Camera cam;
	StateBasedGame game;
	
	public LevelTest(int state) { this.state = state; }

	@Override
	public void init(GameContainer gc, StateBasedGame game) throws SlickException {
		gc.setShowFPS(true);
		level = Level.loadFromFile("data/levels/level3.json", gc, (Main) game);

		cam = new Camera(gc, level);
		this.game = game;
	}

	@Override
	public void render(GameContainer gc, StateBasedGame game, Graphics g)
			throws SlickException {
		level.drawAll(g, cam);
		g.setColor(Color.white);
		g.drawString(String.format("[PJ] X: %.1f Y: %.1f sX: %.2f sY: %.2f g: %s t: %s w: %s a: %s", 
				level.getPlayer().getX(), level.getPlayer().getY(), level.getPlayer().getSpeedX(), level.getPlayer().getSpeedY(),
				level.getPlayer().isOnGround(), level.getPlayer().getTimeOffGround(),
				level.getPlayer().isTouchingWall(), level.getPlayer().getActualAnimation()), 10, 30);
		g.drawString(String.format("[Cam] X: %.1f Y: %.1f", cam.getX(), cam.getY()), 10, 50);
		Utils.drawPlayerInput(gc.getInput(), g, level.getPlayer(), 10, 85);
		if(gc.getInput().isKeyDown(Input.KEY_T)) {
			Utils.drawTriforce(g, gc.getWidth() - 40, 10, 10);
		}
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

		level.updateEntities(delta);

		float pjXOnScreen = level.getPlayer().getCollisionBox().getCenterX();
		float pjYOnScreen = level.getPlayer().getCollisionBox().getCenterY();
		cam.autoMove(pjXOnScreen, pjYOnScreen);
	}

	public void keyPressed(int key, char c) {
		// Reiniciar la posición del jugador y los enemigos
		if(key == Input.KEY_R) {
			level.reset();
		}
	}

	@Override
	public int getID() {
		return state;
	}
}
