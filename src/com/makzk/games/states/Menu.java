package com.makzk.games.states;

import org.newdawn.slick.*;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import com.makzk.games.Main;
import com.makzk.games.elements.Button;
import com.makzk.games.util.ImageManager;

import java.util.ArrayList;
import java.util.List;

public class Menu extends BasicGameState {
	private int state, index = 0;
	private Rectangle rekt = new Rectangle(0,0,350,80);
    private Image imgWallpaper, imgLogo, imgEngine;
    private List<Button> buttons = new ArrayList<>();
	public Menu(int state) { this.state = state; }

	@Override
	public void init(final GameContainer gc, final StateBasedGame game)
			throws SlickException {
        ImageManager img = new ImageManager();
        imgLogo = img.getImage(0);
        imgWallpaper = img.getImage(4);
        imgEngine = img.getImage(5);

        buttons.add(new Button(img.getImage(1), gc.getWidth()/2, 250)); // new game
        buttons.add(new Button(img.getImage(2), gc.getWidth()/2, 350)); // load
        buttons.add(new Button(img.getImage(3), gc.getWidth()/2, 450)); // exit

        buttons.get(0).setRunnable(new Runnable() { // new game
            @Override
            public void run() {
                game.enterState(Main.play);
            }
        });
        buttons.get(2).setRunnable(new Runnable() { // exit
            @Override
            public void run() {
                gc.exit();
            }
        });
	}

	@Override
	public void render(GameContainer gc, StateBasedGame game, Graphics g)
			throws SlickException {
		
        imgWallpaper.drawCentered(gc.getWidth()/2, 350);
        imgLogo.drawCentered(gc.getWidth()/2, 100);
        imgEngine.draw(1000, 450);

        g.setColor(Color.white);
        g.fill(rekt);
        for(Button btn : buttons) {
            btn.drawCentered();
        }
	}

	@Override
	public void update(GameContainer gc, StateBasedGame game, int delta)
			throws SlickException {
		rekt.setCenterX(gc.getWidth()/2);
        rekt.setCenterY(buttons.get(index).getY());
	}

	public void keyPressed(int key, char c) {
		if(key == Input.KEY_UP) index--;
		if(key == Input.KEY_DOWN) index++;

		if(index < 0) index = buttons.size() - 1;
		if(index >= buttons.size()) index = 0;

		if (key == Input.KEY_ENTER) runAction();
	}

    public void mouseMoved(int oldx, int oldy, int newx, int newy) {
        for(int i = 0; i < buttons.size(); i++) {
            if(buttons.get(i).isOver(newx, newy)) {
                index = i;
            }
        }
    }

    public void mouseClicked(int button, int x, int y, int clickCount) {
        if(button != 0) return;

        // The active menu option is fired if the cursor is over one of them
        for(Button btn : buttons) {
            if(btn.isOver(x, y)) {
                runAction();
                break;
            }
        }
    }

    /**
     * Runs the actual menu action
     */
    public void runAction() {
        buttons.get(index).run();
    }

	@Override
	public int getID() {
		return state;
	}

}
