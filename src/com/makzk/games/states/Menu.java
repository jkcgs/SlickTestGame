package com.makzk.games.states;

import org.newdawn.slick.*;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import com.makzk.games.Main;
import com.makzk.games.elements.Button;
import com.makzk.games.util.ImageManager;

public class Menu extends BasicGameState {
	private int state, index = 1;
	private Rectangle rekt = new Rectangle(0,0,350,80);
    private Image imgWallpaper, imgLogo, imgEngine;
	private Button btnNewGame, btnLoad, btnExit;
	public Menu(int state) { this.state = state; }

	@Override
	public void init(GameContainer gc, StateBasedGame game)
			throws SlickException {
        ImageManager img = new ImageManager();
        imgLogo = img.getImage(0);
        imgWallpaper = img.getImage(4);
        imgEngine = img.getImage(5);

        btnNewGame = new Button(img.getImage(1), gc.getWidth()/2, 250);
        btnLoad = new Button(img.getImage(2), gc.getWidth()/2, 350);
        btnExit = new Button(img.getImage(3), gc.getWidth()/2, 450);
	}

	@Override
	public void render(final GameContainer gc, final StateBasedGame game, Graphics g)
			throws SlickException {
		
        imgWallpaper.drawCentered(gc.getWidth()/2, 350);
        imgLogo.drawCentered(gc.getWidth()/2, 100);
        imgEngine.draw(1000, 450);

        g.setColor(Color.white);
        g.fill(rekt);
        btnNewGame.drawCentered();
        btnLoad.drawCentered();
        btnExit.drawCentered();
        btnNewGame.setRunnable(new Runnable() {
            @Override
            public void run() {
                game.enterState(Main.play);
            }
        });
        btnExit.setRunnable(new Runnable() {
            @Override
            public void run() {
                gc.exit();
            }
        });
	}

	@Override
	public void update(GameContainer gc, StateBasedGame game, int delta)
			throws SlickException {
		rekt.setCenterX(gc.getWidth()/2);
		switch (index) {
			case 1: 
				rekt.setCenterY(249);
				break;
			case 2: 
				rekt.setCenterY(349);
				break;
			case 3:
				rekt.setCenterY(449);
				break;
		}
	}

	public void keyPressed(int key, char c) {
		if(key == Input.KEY_UP) index--;
		if(key == Input.KEY_DOWN) index++;

		if(index < 1) index = 3;
		if(index > 3) index = 1;

		if (key == Input.KEY_ENTER) runAction();
	}

    public void mouseMoved(int oldx, int oldy, int newx, int newy) {
        if(btnNewGame.isOver(newx, newy)) {
            index = 1;
        } else if(btnLoad.isOver(newx, newy)) {
            index = 2;
        } else if(btnExit.isOver(newx, newy)) {
            index = 3;
        }
    }

    public void mouseClicked(int button, int x, int y, int clickCount) {
        if(button != 0) return;

        // The active menu option is fired if the cursor is over one of them
        if(btnNewGame.isOver(x, y)
                || btnLoad.isOver(x, y)
                || btnExit.isOver(x, y)) {
            runAction();
        }
    }

    /**
     * Runs the actual menu action
     */
    public void runAction() {
        switch (index) {
            case 1: btnNewGame.run(); break;
            case 2: btnLoad.run(); break;
            case 3: btnExit.run(); break;
        }
    }

	@Override
	public int getID() {
		return state;
	}

}
