package com.makzk.games.util;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.util.Log;


public class ImageManager {
	
	private Image [] buttons;
	
	 public ImageManager() {
		try {
			buttons = new Image[]{
				new Image("data/sprites/title.png"),
				new Image("data/sprites/Newgame.png"), 
				new Image("data/sprites/LoadGame.png"), 
				new Image("data/sprites/Exit.png"), 
				new Image("data/sprites/Wallpaper.png"),
				new Image("data/sprites/clocksmall.png"),
				new Image("data/sprites/options.png")
			};
		} catch (SlickException e) {
			Log.error("Error Malvado");
			e.printStackTrace();
		}
	 }

	public Image getImage(int index){
		return buttons[index];
	}
	
	/*      ***Image Index***
	 *		0.- Titulo del juego
	 *		1.- Boton New Game
	 *		2.- Boton Load
	 *		3.- Exit
	 *  	4.- Wallpaper
	 *  	5.- Clockwork Logo 
	 *  	6.- Options
	 * 
	 */
	

}
