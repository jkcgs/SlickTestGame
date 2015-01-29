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
		System.out.println("Init level");
		level = new Level(gc);
		level.addRect(0, 440, gc.getWidth(), 10, Color.gray);
		level.addRect(0, 0, gc.getWidth(), 10, Color.gray);
		level.addRect(0, 200, gc.getWidth(), 10, Color.gray);
		level.addRect(200, 350, 20, 100);
		level.addRect(220, 350, 100, 20);
		level.addRect(320, 270, 20, 100);
		level.addRect(340, 270, 100, 20);
		
		System.out.println("Init player");
		Rectangle r = new Rectangle(50, 300, 20, 50);
		player = new Player(gc, r);
		player.setColor(Color.blue);
		
		System.out.println("Done loading");
	}

	@Override
	public void render(GameContainer gc, Graphics g) throws SlickException {
		level.drawAll(g);
		player.draw();
		g.setColor(Color.white);
		g.drawString(String.format("OnGround: %s", player.isOnGround()), 10, 30);
		g.drawString(String.format("[Speed] X: %s Y: %s", player.getSpeedX(), player.getSpeedY()), 10, 55);
		g.drawString(String.format("[Position] X: %s Y: %s", player.getRect().getX(), player.getRect().getY()), 10, 70);
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
