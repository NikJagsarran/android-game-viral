package com.prosus.androidgames.viral;

import com.prosus.androidgames.framework.Graphics;
import com.prosus.androidgames.framework.Input.TouchEvent;
import com.prosus.androidgames.framework.Pixmap;

public class Button {
	private int x;
	private int y;
	private int width;
	private int height;
	
	private Pixmap imageToDisplay;
	private Pixmap imageRegular;
	private Pixmap imageHighlighted;
	
	public Button(int x, int y, Pixmap imageRegular) {
		initialize(x, y, imageRegular, imageRegular);
	}
	
	public Button(int x, int y, Pixmap imageRegular, Pixmap imageHighlighted) {
		initialize(x, y, imageRegular, imageHighlighted);
	}
	
	private void initialize(int x, int y, Pixmap imageRegular, Pixmap imageHighlighted) {
		this.x = x;
		this.y = y;
		width = imageRegular.getWidth();
		height = imageRegular.getHeight();
		
		imageToDisplay = imageRegular;
		this.imageRegular = imageRegular;
		this.imageHighlighted = imageHighlighted;
	}
	
	public boolean eventInBounds(TouchEvent t) {
		return t.x >= x && t.x <= (x + width) && t.y >= y && t.y <= (y + height);
	}
	
	public void draw(Graphics g) {
		draw(g, 255);
	}
	
	public void draw(Graphics g, int alpha) {
		g.drawPixmap(imageToDisplay, x, y, alpha);
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public Pixmap getImage() {
		return imageToDisplay;
	}
	
	public void setHighlighted() {
		imageToDisplay = imageHighlighted;
	}
	
	public void removeHighlighted() {
		imageToDisplay = imageRegular;
	}
	
	public void setRegularImage(Pixmap image) {
		imageRegular = image;
	}
	
	public void setHighlightedImage(Pixmap image) {
		imageHighlighted = image;
	}
}
