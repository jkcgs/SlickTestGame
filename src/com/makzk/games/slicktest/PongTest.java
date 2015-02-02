package com.makzk.games.slicktest;

import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.util.Log;

import com.makzk.games.entities.Ball;
import com.makzk.games.entities.Pad;
import com.makzk.games.util.Direction;

/**
 * Prueba de colisiones con pads y una pelota en movimiento. 
 * En resumen, el juego pong.
 * 
 * @author makzk
 */
public class PongTest extends BasicGame {

	Pad leftPad, rightPad;
	Ball ball;
	Input in;
	
	int scoreA, scoreB;

	public PongTest(String title) {
		super(title);
	}
	
	@Override
	public void init(GameContainer gc) throws SlickException {
		gc.setShowFPS(false);
		gc.getGraphics().drawString("Loading...", 10, 10);
		
		in = gc.getInput();

		leftPad = new Pad(gc);
		leftPad.setY(100);
		rightPad = new Pad(gc);
		rightPad.setY(100);
		rightPad.setX(gc.getWidth() - rightPad.getWidth());
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
	public void update(GameContainer gc, int delt) throws SlickException {
		leftPad.move(in, delt);
		rightPad.autoMove(ball, delt);
		ball.move(delt, leftPad, rightPad);
		
		if(ball.getX() <= 0) {
			ball.reset();
			scoreB++;
		}

		if(ball.getMaxX() >= gc.getWidth()) {
			ball.reset();
			scoreA++;
		}
	}

}
