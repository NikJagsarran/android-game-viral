package com.prosus.androidgames.viral;

import android.graphics.Rect;

public class Virus {
	private int x;
	private int y;
	private int accelX;
	private int accelY;
	
	private enum Direction {
		DOWN, LEFT, UP, RIGHT
	}
	private Direction direction;
	private Utils utils;
	
	public Virus(int x, int y, int accelX, int accelY, Utils utils) {
		this.x = x;
		this.y = y;
		this.accelX = accelX;
		this.accelY = accelY;
		this.utils = utils;
	}
	
	public Virus(int speed, Utils utils) {
		this.utils = utils;
		
		/*
		 * Randomly choose a side to spawn.
		 * [0, 1): Top.
		 * [1, 2): Right.
		 * [2, 3): Bottom.
		 * [3, 4): Left.
		 */
		float sideChooser = (float) (Math.random() * utils.SCREEN_PERIMETER);
		
		if (sideChooser < utils.SCREEN_TOP_UPPER_BOUND) {
			x = (int) (Math.random() * (utils.CANVAS_WIDTH - Constants.VIRUS_WIDTH));
			y = 0 - Constants.VIRUS_HEIGHT;
			accelY = speed;
			direction = Direction.DOWN;
			
		} else if (sideChooser >= utils.SCREEN_TOP_UPPER_BOUND
				&& sideChooser < utils.SCREEN_RIGHT_UPPER_BOUND) {
			x = utils.CANVAS_WIDTH;
			y = (int) (Math.random() * (utils.CANVAS_HEIGHT - Constants.VIRUS_HEIGHT));
			accelX = -1 * speed;
			direction = Direction.LEFT;
			
		} else if (sideChooser >= utils.SCREEN_RIGHT_UPPER_BOUND
				&& sideChooser < utils.SCREEN_BOTTOM_UPPER_BOUND) {
			x = (int) (Math.random() * (utils.CANVAS_WIDTH - Constants.VIRUS_WIDTH));
			y = utils.CANVAS_HEIGHT;
			accelY = -1 * speed;
			direction = Direction.UP;
			
		} else {
			x = 0 - Constants.VIRUS_WIDTH;
			y = (int) (Math.random() * (utils.CANVAS_HEIGHT - Constants.VIRUS_HEIGHT));
			accelX = speed;
			direction = Direction.RIGHT;
		}
	}
	
	public void move() {
		x += accelX;
		y += accelY;
	}
	
	public boolean touchingAntiVirus(int virusX, int virusY) {
		Rect virusRectangle = new Rect(virusX, virusY, virusX + Assets.virus.getWidth(),
				virusY + Assets.virus.getHeight());
		Rect antiVirusRectangle = new Rect(x, y, x + Assets.antiVirus.getWidth(), y + Assets.antiVirus.getHeight());
		
		if (antiVirusRectangle.intersect(virusRectangle)) {
			return true;
		}
		return false;
	}
	
	public boolean isOutOfBounds() {
		if ((direction == Direction.DOWN && y >= utils.CANVAS_HEIGHT)
				|| (direction == Direction.LEFT && x <= (0 - Assets.virus.getWidth()))
				|| (direction == Direction.UP && y <= (0 - Assets.virus.getHeight()))
				|| (direction == Direction.RIGHT && x >= utils.CANVAS_WIDTH)) {
			return true;
		}
		return false;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
}