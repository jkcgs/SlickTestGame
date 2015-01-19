package com.makzk.games.slicktest;

import java.util.logging.Logger;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import com.makzk.games.Level;

public class GravityTest extends BasicGame {
	Level level;

	public GravityTest() {
		super("SlickGravity");
	}
	
	@Override
	public void init(GameContainer gc) throws SlickException {
		level = new Level(gc);
		level.addRect(10.0f, 440.0f, 600.0f, 10.0f, Color.gray);
		level.addRect(200.0f, 350.0f, 20.0f, 100.0f);
		level.addRect(220.0f, 350.0f, 100.0f, 20.0f);
		level.addRect(320.0f, 270.0f, 20.0f, 100.0f);
		level.addRect(340.0f, 270.0f, 100.0f, 20.0f);
	}

	@Override
	public void render(GameContainer gc, Graphics g) throws SlickException {
		level.drawAll(g);
	}


	@Override
	public void update(GameContainer gc, int delt) throws SlickException {

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
