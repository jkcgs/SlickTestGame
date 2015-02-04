package com.makzk.games;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class ImageManager extends BasicGameState {
	private int state;

	protected Image title;
	protected Image load;
	protected Image exit;
	protected Image nGame;
	protected Image iluminati;

	public ImageManager(int state) { this.state = state; }

	@Override
	public void init(GameContainer gc, StateBasedGame game) throws SlickException {
		
		title = new Image("data/sprites/title1.png");
		load = new Image("data/sprites/load.png");
		exit = new Image("data/sprites/Exit.png");
		nGame = new Image("data/sprites/Newgame.png");
		iluminati = new Image("data/sprites/the-Illuminati.jpg");
		
	}
	
	@Override
	public void render(GameContainer gc, StateBasedGame game, Graphics g)
			throws SlickException {
			iluminati.draw(150,100);
			title.draw(100,10);
			load.draw(350, 250);
			nGame.draw(350, 350);
			exit.draw(350,450);
	}


	@Override
	public void update(GameContainer gc, StateBasedGame game, int delta)
			throws SlickException {
		
		
	}

	@Override
	public int getID() {
		return state;
	}

}
