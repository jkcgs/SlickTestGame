package com.makzk.games;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.util.Log;


public class ImageManager {
	
	private Image [] buttons;
	
	 public ImageManager() {
		 
			 	
					try {
						buttons = new Image[]{
							new Image("data/sprites/title1.png"),
							new Image("data/sprites/Newgame.png"), 
							new Image("data/sprites/load.png"), 
							new Image("data/sprites/Exit.png"), 
							new Image("data/sprites/the-Illuminati.jpg")
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
	 *  	3.- Illuminati  
	 * 
	 */
	

}
