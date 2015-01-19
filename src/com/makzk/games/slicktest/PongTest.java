package com.makzk.games.slicktest;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.util.Log;

import com.makzk.games.entities.Ball;
import com.makzk.games.entities.Pad;
import com.makzk.games.util.Direction;

public class PongTest extends BasicGame {

	Pad leftPad, rightPad;
	Ball ball;
	Input in;
	
	int scoreA, scoreB;

	public PongTest() {
		super("SlickPong");
	}
	
	@Override
	public void init(GameContainer gc) throws SlickException {
		gc.setShowFPS(false);
		gc.getGraphics().drawString("Loading...", 10, 10);
		
		in = gc.getInput();

		leftPad = new Pad(gc);
		leftPad.getRect().setY(100);
		rightPad = new Pad(gc);
		rightPad.getRect().setY(100);
		rightPad.getRect().setX(gc.getWidth() - rightPad.getRect().getWidth());
		rightPad.setMovementSpeed(.5f);

		ball = new Ball(gc);
		ball.reset();
		ball.setDirection(Direction.SOUTH_WEST);
		
		scoreA = 0;
		scoreB = 0;

		Log.info("Loading done!");
	}

	@Override
	public void render(GameContainer gc, Graphics g) throws SlickException {
		g.drawString(scoreA+"", 10, 10);
		g.drawString(scoreB+"", gc.getWidth() - 40, 10);
		
		leftPad.draw();
		rightPad.draw();
		ball.draw();
	}


	@Override
	public void update(GameContainer gc, int Δ) throws SlickException {
		leftPad.move(in, Δ);
		rightPad.autoMove(ball);
		ball.move(Δ, leftPad, rightPad);
		
		if(ball.getRect().getX() <= 0) {
			ball.reset();
			scoreB++;
		}
		
		if(ball.getRect().getX() + ball.getRect().getWidth() >= gc.getWidth()) {
			ball.reset();
			scoreA++;
		}
	}

	public static void main(String[] args) {
		try
		{
			AppGameContainer appgc;
			appgc = new AppGameContainer(new PongTest());
			appgc.setDisplayMode(640, 480, false);
			appgc.start();
		}
		catch (SlickException ex)
		{
			Logger.getLogger(PongTest.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

}
