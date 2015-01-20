package com.makzk.games.slicktest;

import java.util.logging.Logger;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;

import com.makzk.games.Level;
import com.makzk.games.entities.Player;

public class GravityTest extends BasicGame {
	Level level;
	Player player;

	public GravityTest() {
		super("SlickGravity");
	}
	
	@Override
	public void init(GameContainer gc) throws SlickException {
		level = new Level(gc);
		level.addRect(10f, 440f, 600f, 10f, Color.gray);
		level.addRect(200f, 350f, 20f, 100f);
		level.addRect(220f, 350f, 100f, 20f);
		level.addRect(320f, 270f, 20f, 100f);
		level.addRect(340f, 270f, 100f, 20f);
		
		Rectangle r = new Rectangle(10, 10, 20, 50);
		player = new Player(gc, r);
		player.setColor(Color.blue);
	}

	@Override
	public void render(GameContainer gc, Graphics g) throws SlickException {
		level.drawAll(g);
		player.draw();
	}

	@Override
	public void update(GameContainer gc, int delta) throws SlickException {
		player.move(level, delta);
	}

	public static void main(String[] args) {
		try
		{
			AppGameContainer appgc;
			appgc = new AppGameContainer(new GravityTest());
			appgc.setDisplayMode(640, 480, false);
			appgc.start();
		}
		catch (SlickException ex)
		{
			Logger.getLogger(GravityTest.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		}
	}

}
