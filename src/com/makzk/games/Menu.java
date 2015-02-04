package com.makzk.games;

import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class Menu extends BasicGame {
	

	protected Image title;
	protected Image load;
	protected Image exit;
	protected Image nGame;
	protected Image iluminati;
	
	public Menu(String title) {
		super(title);
		
	}

	@Override
	public void init(GameContainer container) throws SlickException {
		
		title = new Image("data/sprites/title1.png");
		load = new Image("data/sprites/load.png");
		exit = new Image("data/sprites/Exit.png");
		nGame = new Image("data/sprites/Newgame.png");
		iluminati = new Image("data/sprites/the-Illuminati.jpg");
		
	}
	
	@Override
	public void render(GameContainer container, Graphics g)
			throws SlickException {
			iluminati.draw(150,100);
			title.draw(100,10);
			load.draw(350, 250);
			nGame.draw(350, 350);
			exit.draw(350,450);
	}


	@Override
	public void update(GameContainer container, int delta)
			throws SlickException {
		
		
	}

}
