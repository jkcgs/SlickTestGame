package com.makzk.games.slicktest;

import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.util.Log;

import com.makzk.games.Level;
import com.makzk.games.entities.Player;
import com.makzk.games.util.Camera;

public class LevelTest extends BasicGame {
	Level level;
	Camera cam;
	Player player;
	
	public LevelTest(String title) {
		super(title);
	}

	@Override
	public void init(GameContainer gc) throws SlickException {
		Log.info("Init level");
		level = Level.loadFromFile("data/levels/level1.json", gc);

		cam = new Camera(gc, level);
		
		Log.info("Init player");
		player = new Player(gc, level);

		Log.info("Done");
	}

	@Override
	public void render(GameContainer container, Graphics g)
			throws SlickException {
		level.drawAll(g, cam);
		player.draw(cam);
		g.setColor(Color.white);g.drawString(String.format("[PJ] X: %.1f Y: %.1f sX: %.2f sY: %.2f g: %s w: %s", 
				player.getX(), player.getY(), player.getSpeedX(), player.getSpeedY(), player.isOnGround(), player.isTouchingWall()), 10, 30);
		g.drawString(String.format("[Position] X: %.1f Y: %.1f", cam.getX(), cam.getY()), 10, 50);
	}

	@Override
	public void update(GameContainer container, int delta)
			throws SlickException {
		player.move(level, delta);
		level.updateEnemies(delta);
		
		float pjXOnScreen = player.getCollisionBox().getCenterX();
		float pjYOnScreen = player.getCollisionBox().getCenterY();
		cam.autoMove(pjXOnScreen, pjYOnScreen);
	}

	public void keyPressed(int key, char c) {
		// Reiniciar la posici�n del jugador y los enemigos
		if(key == Input.KEY_R) {
			level.reset(player);
		}
	};
}
