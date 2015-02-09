package com.makzk.games.elements;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.util.Log;

public class Button {
	
	private Image buttonImage;
	
	public Button() {
		try {
			//solucion temporal
			buttonImage = new Image("data/sprites/title1.png");
		} catch (SlickException e) {
			Log.error("error muy malvado");
			e.printStackTrace();
		}
	}
	
	public void setImage(Image img){
		buttonImage = img;
	}
	
	public Image getImage(){
		return buttonImage;
	}
	
}
