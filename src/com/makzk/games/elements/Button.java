package com.makzk.games.elements;

import org.newdawn.slick.Image;

public class Button {
	
	private Image img;
    private float x;
    private float y;
    private Runnable runnable = null;

    public Button(Image img, float x, float y, Runnable runnable) {
        this.img = img;
        this.x = x;
        this.y = y;
        this.runnable = runnable;
    }
    public Button(Image img, float x, float y) {
        this(img, x, y, null);
    }

    public void draw() {
        img.draw(x, y);
    }

    public void drawCentered() {
        img.drawCentered(x, y);
    }

    public boolean isOver(int mx, int my) {
        float x = this.x - (img.getWidth() / 2);
        float y = this.y - (img.getHeight() / 2);
        return (mx >= x && mx < x + img.getWidth())
                && (my >= y && my < y + img.getHeight());
    }

    public void setRunnable(Runnable runnable) {
        this.runnable = runnable;
    }

    public void run() {
        if(runnable != null) {
            runnable.run();
        }
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
}
