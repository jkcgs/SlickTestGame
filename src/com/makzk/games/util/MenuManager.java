package com.makzk.games.util;


import java.util.ArrayList;

import org.newdawn.slick.GameContainer;

import com.makzk.games.elements.Button;

public class MenuManager {
	
	/*FALTA:
	 * 	1.- Hacer funcionar el sistema de KeyPressed
	 *  2.- Hacer funcionar MouseOnClick y Mouse Moved
	 *  3.- Activar accion del menu al hacer enter o click
	 */
	
	//Super Menu Manager Made by Jorge Solis do not Steal this is my original work :D.
	private int index = 0;
	private ArrayList<Button> buttons;
	
		public MenuManager()  {
			buttons = new ArrayList<Button>();
		}
		
	public void addButton(Button b, GameContainer gc) {
		buttons.add(b);
		float lastY = 0;
		for (int i = 0;  i < buttons.size(); i++) { 
			if (i>0) {
				lastY += 100f + (buttons.get(i-1).getHeight()/2);
			} 
			buttons.get(i).setX((float)gc.getWidth()/2-(buttons.get(i).getWidth()/2));
			buttons.get(i).setY(lastY);
		}
	}
	
	public void draw() {
		for (Button button: buttons) {
			button.draw();
		}
	}
	
	public ArrayList<Button> getButtons() {
		return buttons;
	}
	
	
}
