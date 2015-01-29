package com.makzk.games.slicktest;

import java.util.logging.Logger;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.util.Log;

import com.makzk.games.Level;
import com.makzk.games.entities.Player;

/**
 * Prueba de gravedad y colisiones de un elemento con otros en un nivel
 * Consiste en que un jugador (Player) que puede moverse y saltar se le
 * aplique gravedad y la capacidad de colisionar con otros elementos sólidos
 * 
 * @author makzk
 * @author japple
 *
 */
public class GravityTest extends BasicGame {
	Level level;
	Player player;

	public GravityTest() {
		super("SlickGravity");
	}
	
	@Override
	public void init(GameContainer gc) throws SlickException {
		Log.info("Init level");
		// Preparar nivel con unos cuantos rectángulos caguais
		// TODO: Hacer esto en un archivo externo para poder manejar varios niveles
		level = new Level(gc);
		level.addRect(0, 440, gc.getWidth(), 10, Color.gray);
		level.addRect(0, 0, gc.getWidth(), 10, Color.gray);
		level.addRect(0, 200, gc.getWidth(), 10, Color.gray);
		level.addRect(200, 350, 20, 100);
		level.addRect(220, 350, 100, 20);
		level.addRect(320, 270, 20, 100);
		level.addRect(340, 270, 100, 20);
		
		// Preparar jugador con su rectángulo (dimensiones y posición)
		Log.info("Init player");
		Rectangle r = new Rectangle(50, 300, 20, 50);
		player = new Player(gc, r);
		
		Log.info("Done loading");
	}
	
	public void keyPressed(int key, char c) {
		// Reiniciar la posición del jugador
		if(key == Input.KEY_R) {
			player.reset();
		}
	};

	@Override
	public void render(GameContainer gc, Graphics g) throws SlickException {
		level.drawAll(g);
		player.draw();
		g.setColor(Color.white);
		g.drawString(String.format("OnGround: %s", player.isOnGround()), 10, 30);
		g.drawString(String.format("[Speed] X: %.2f Y: %.2f", player.getSpeedX(), player.getSpeedY()), 10, 55);
		g.drawString(String.format("[Position] X: %.1f Y: %.1f", player.getRect().getX(), player.getRect().getY()), 10, 70);
	}

	@Override
	public void update(GameContainer gc, int delta) throws SlickException {
		player.move(level, delta);
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
