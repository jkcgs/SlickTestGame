package com.makzk.games.states;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import com.makzk.games.Main;
import com.makzk.games.elements.Button;
import com.makzk.games.util.ImageManager;


public class Menu extends BasicGameState {
	private int state, index = 1;
	private Rectangle rekt = new Rectangle(0,0,350,80);
	private ImageManager img;
	private StateBasedGame game;
	private Button logo,news,load,exit,wallpaper,engilogo;
	public Menu(int state) { this.state = state; }
	
	
	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
			img = new ImageManager();
			logo = new Button();
			news = new Button();
			load = new Button();
			exit = new Button();
			wallpaper = new Button();
			engilogo = new Button();
			wallpaper.setImage(img.getImage(4));
			logo.setImage(img.getImage(0));
			news.setImage(img.getImage(1));
			load.setImage(img.getImage(2));
			exit.setImage(img.getImage(3));
			engilogo.setImage(img.getImage(5));
			this.game = game;
	}

	@Override
	public void render(GameContainer gc, StateBasedGame game, Graphics g)
			throws SlickException {
		
			wallpaper.getImage().drawCentered(gc.getWidth()/2, 350);
			g.fill(rekt);
			logo.getImage().drawCentered(gc.getWidth()/2, 100);
			news.getImage().drawCentered(gc.getWidth()/2, 250);
			load.getImage().drawCentered(gc.getWidth()/2, 350);
			exit.getImage().drawCentered(gc.getWidth()/2, 450);
			engilogo.getImage().draw(1000, 450);

		
	}

	@Override
	public void update(GameContainer gc, StateBasedGame game, int delta)
			throws SlickException {
		rekt.setCenterX(gc.getWidth()/2);
		switch (index){
		
			case 1: 
				rekt.setCenterY(249);
				break;
			case 2: 
				rekt.setCenterY(349);
				break;
			case 3:
				rekt.setCenterY(449);
				break;
		
		}
		
	}
	public void keyPressed(int key, char c) {
		if(key == Input.KEY_UP) {
			index--;
		}
		
		if(key == Input.KEY_DOWN){
			index++;
		}
		
		if(index<1){
			index=3;
		}
		
		if(index>3){
			index=1;
		}
		if (key == Input.KEY_ENTER){
			switch (index) {
			
			case 1:
				game.enterState(Main.play);
				break;
			case 2:
				
				break;
			case 3:
				System.exit(0);
				break;
			}
		}
	}

	@Override
	public int getID() {
		return state;
	}
	

}
