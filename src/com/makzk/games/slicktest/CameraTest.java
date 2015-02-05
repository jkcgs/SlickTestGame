package com.makzk.games.slicktest;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.Log;

import com.makzk.games.Level;
import com.makzk.games.entities.Enemy;
import com.makzk.games.entities.Player;
import com.makzk.games.util.Camera;

public class CameraTest extends BasicGameState {
	private int state;
	
	Level level;
	Camera cam;
	Player player;
	Enemy enemy;
	
	public CameraTest(int state) { this.state = state; }

	@Override
	public void init(GameContainer gc, StateBasedGame game) throws SlickException {
		
		Log.info("Init level");
		level = new Level(gc, 2000);
		level.addRect(0, 500, level.getWidth(), 10, Color.gray);
		level.addRect(200, 350, 20, 100);
		level.addRect(220, 350, 100, 20);
		level.addRect(320, 270, 20, 100);
		level.addRect(340, 270, 100, 20);
		level.addRect(1600, 340, 100, 15);
		level.addRect(0, 480, 10, 20);

		level.addRect(800, 270, 300, 20);
		level.addRect(1100, 350, 300, 15);
		level.addRect(1200, 100, 150, 10);
		level.addRect(1100, 200, 150, 10);
		level.addRect(1400, 400, 300, 10);
		level.addRect(1500, 300, 300, 10);

		cam = new Camera(gc, level);
		
		Log.info("Init player");
		Rectangle r = new Rectangle(50, 300, 102, 110);
		player = new Player(gc, r);
		
		Log.info("Init an enemy");
		enemy = new Enemy(gc, new Rectangle(200, 100, 38, 82));

		Log.info("Done");
	}

	@Override
	public void render(GameContainer gc, StateBasedGame game, Graphics g)
			throws SlickException {
		level.drawAll(g, cam);
		enemy.draw(cam);
		player.draw(cam);
		
		g.setColor(Color.white);
		g.drawString(String.format("[PJ] X: %.1f Y: %.1f sX: %.2f sY: %.2f g: %s w: %s", 
				player.getX(), player.getY(), player.getSpeedX(), player.getSpeedY(), player.isOnGround(), player.isTouchingWall()), 10, 50);
		g.drawString(String.format("[CamPos] X: %.2f Y: %.2f", cam.getX(), cam.getY()), 10, 30);
	}

	@Override
	public void update(GameContainer gc, StateBasedGame game, int delta)
			throws SlickException {
		//This is bullshit
		Input in = gc.getInput();
		if(in.isKeyDown(Input.KEY_LEFT)) {
			cam.moveX(-.5f * delta);
		} else if(in.isKeyDown(Input.KEY_RIGHT)) {
			cam.moveX(.5f * delta);
		}
		//
		player.move(delta, level);
		
		float pjXOnScreen = player.getCollisionBox().getCenterX();
		float pjYOnScreen = player.getCollisionBox().getCenterY();
		cam.autoMove(pjXOnScreen, pjYOnScreen);
		
		enemy.move(delta, level);
		if(enemy.getX() < 0 || enemy.getY() > gc.getHeight()) {
			enemy.reset();
		}
	}

	public void keyPressed(int key, char c) {
		// Reiniciar la posición del jugador
		if(key == Input.KEY_R) {
			player.reset();
		}
	}

	@Override
	public int getID() {
		return state;
	};
}
