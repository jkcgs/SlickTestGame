package com.makzk.games.elements;

import org.newdawn.slick.Image;

public class Button {
	
	private Image img;
    private float x;
    private float y;

    public Button(Image img, float x, float y) {
        this.img = img;
        this.x = x;
        this.y = y;
    }

    public void draw() {
        img.draw(x, y);
    }

    public void drawCentered() {
        img.drawCentered(x, y);
    }
}
