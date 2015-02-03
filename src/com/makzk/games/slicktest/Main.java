package com.makzk.games.slicktest;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.util.Log;

public class Main {

	public static void main(String[] args) {
		int winWidth = 1000; // Ancho ventana
		int winHeight = 700; // Alto ventana
		String title = "Prueba Slick2D"; // Título de ventana
		BasicGame test = new PhysicsTest(title); // Clase a ejecutar
		
		try {
			AppGameContainer appgc;
			appgc = new AppGameContainer(test);
			appgc.setDisplayMode(winWidth, winHeight, false);
			appgc.setTargetFrameRate(60);
			appgc.start();
		} catch (SlickException e) {
			Log.error(null, e);
		}
	}

}
