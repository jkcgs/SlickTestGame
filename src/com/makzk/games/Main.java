package com.makzk.games;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.Log;

import com.makzk.games.slicktest.LevelTest;
import com.makzk.games.slicktest.PongTest;
import com.makzk.games.states.Menu;
import com.makzk.games.util.SoundManager;

public class Main extends StateBasedGame {
	public static final String name = "Prueba Slick2D";
	public static final int winWidth = 1200;
	public static final int winHeight = 700;
	
	public static final int menu = 0;
	public static final int play = 1;
	public static final int pong = 2;
	
	public SoundManager sndManager;
	
	public Main() {
		super(name);
		sndManager = new SoundManager();

		addState(new Menu(menu));
		addState(new LevelTest(play));
		addState(new PongTest(pong));
	}

	@Override
	public void initStatesList(GameContainer gc) throws SlickException {
		enterState(menu);
	}

	public static void main(String[] args) {		
		try {
			AppGameContainer appgc;
			appgc = new AppGameContainer(new Main());
			appgc.setDisplayMode(winWidth, winHeight, false);
			appgc.setTargetFrameRate(60);
			appgc.start();
		} catch (SlickException e) {
			Log.error(null, e);
		}
	}


}
