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
    
    public Button(Image img) {
    	this(img,0,0);
    }

    public void draw() {
        img.draw(x, y);
    }

    public boolean isOver(int mx, int my) {
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
    
    public float getHeight () {
    	return img.getHeight();
    }
    
    public float getWidth () {
    	return img.getWidth();
    }
	public void setX(float x) {
		this.x = x;
	}
	public void setY(float y) {
		this.y = y;
	}
    
    
}
