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
	
	private Image [] buttons;
	
	 public ImageManager() throws SlickException {
		 
			 	buttons = new Image[]{
					new Image("data/sprites/title1.png"),
					new Image("data/sprites/Newgame.png"), 
					new Image("load.png"), new Image("Exit.png"), 
					new Image("the-Illuminati.jpg")
				};
			 	
	 }

	public Image getImage(int index){
		return buttons[index];
	}
	
	/*      ***Image Index***
	 *		0.- Titulo del juego
	 *		1.- Boton New Game
	 *		2.- Boton Load
	 *  	3.- Illuminati  
	 * 
	 */
	
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
