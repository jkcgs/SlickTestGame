package com.makzk.games.slicktest;

import org.newdawn.slick.BasicGame;
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
	Enemy enemy;
	
	public PhysicsTest(String title) {
		super(title);
	}

	@Override
	public void init(GameContainer gc) throws SlickException {
		
		Log.info("Init level");
		level = new Level(gc, 2000);
		float[][] rects = new float[][]{
				{212, 338, 266, 44, 128, 74, 0},
				{261, 338, 52, 44, 255, 247, 23},
				{381, 338, 49, 43, 255, 247, 23},
				{321, 198, 56, 46, 255, 247, 23},
				{774, 447, 76, 75, 56, 252, 0},
				{1199, 381, 95, 144, 56, 252, 0},
				{1713, 379, 96, 146, 56, 252, 0},
				{-3, 521, 2180, 121, 192, 131, 9},
				{761, 414, 104, 34, 2, 235, 0},
				{1185, 347, 124, 37, 2, 235, 0},
				{1697, 341, 128, 38, 2, 235, 0}
			};

		level.addRects(rects, null);

		cam = new Camera(gc, level);
		
		Log.info("Init player");
		Rectangle r = new Rectangle(50, 300, 102, 110);
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
	}

	@Override
	public void update(GameContainer container, int delta)
			throws SlickException {
		player.move(level, delta);
		level.updateEnemies(delta);
		
		float pjXOnScreen = player.getRect().getCenterX();
		cam.autoMove(pjXOnScreen);
		
	}

	public void keyPressed(int key, char c) {
		// Reiniciar la posición del jugador
		if(key == Input.KEY_R) {
			player.reset();
		}
	};
}
