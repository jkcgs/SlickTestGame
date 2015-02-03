package com.makzk.games.slicktest;

import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.util.Log;

import com.makzk.games.Level;
import com.makzk.games.entities.Enemy;
import com.makzk.games.entities.Player;
import com.makzk.games.util.Camera;

public class PhysicsTest extends BasicGame {
	Level level;
	Camera cam;
	Player player;
	
	public PhysicsTest(String title) {
		super(title);
	}

	@Override
	public void init(GameContainer gc) throws SlickException {
		Log.info("Init level");
		level = new Level(gc, 440, 2000);
		float[][] rects = new float[][]{
				
				{8, 1935, 407, 25, 241, 7, 7},
				{216, 1824, 148, 12, 241, 7, 7},
				{13, 1674, 180, 12, 241, 7, 7},
				{221, 1498, 179, 13, 241, 7, 7},
				{8, 1304, 191, 11, 241, 7, 7},
				{259, 1046, 153, 9, 241, 7, 7},
				{11, 805, 209, 9, 241, 7, 7},
				{209, 544, 197, 9, 241, 7, 7},
				{10, 263, 207, 10, 241, 7, 7},
				{125, 107, 14, 13, 241, 7, 7},
				{138, 92, 21, 28, 241, 7, 7},
				{159, 102, 14, 17, 241, 7, 7},
				{141, 79, 11, 13, 241, 7, 7},
				{125, 94, 13, 13, 241, 7, 7},
				{150, 93, 21, 17, 241, 7, 7},
				{150, 85, 13, 16, 241, 7, 7},
				{130, 86, 12, 12, 241, 7, 7},
				{108, 107, 19, 13, 241, 7, 7},
				{115, 99, 13, 12, 241, 7, 7},
				{171, 108, 24, 11, 241, 7, 7},
				{169, 100, 15, 10, 241, 7, 7},
				{149, 115, 48, 5, 241, 7, 7},
				{134, 93, 25, 23, 22, 201, 45},
				{304, 77, 22, -30, 0, 0, 0},
				{141, 100, 10, 11, 0, 0, 0}
				/*
				{212, 358, 266, 44, 128, 74, 0},
				{261, 358, 52, 44, 255, 247, 23},
				{381, 358, 49, 43, 255, 247, 23},
				{321, 218, 56, 46, 255, 247, 23},
				{774, 467, 76, 75, 56, 252, 0},
				{1199, 401, 95, 144, 56, 252, 0},
				{1713, 399, 96, 146, 56, 252, 0},
				{-3, 541, 2180, 121, 192, 131, 9},
				{761, 434, 104, 34, 2, 235, 0},
				{1185, 367, 124, 37, 2, 235, 0},
				{1697, 361, 128, 38, 2, 235, 0}
				*/
			};

		level.addRects(rects, null);

		cam = new Camera(gc, level);
		
		Log.info("Init player");
		Rectangle r = new Rectangle(50, 10, 50, 105);
		player = new Player(gc, r);
		
		Log.info("Init enemies");
		level.addEnemies(new float[][]{
				{700, 100, 38, 82},
				{1200, 100, 38, 82},
				{1300, 100, 38, 82},
		});

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
		// Reiniciar la posición del jugador y los enemigos
		if(key == Input.KEY_R) {
			player.reset();
			for(Enemy e : level.getEnemies()) {
				e.reset();
			}
		}
	};
}
