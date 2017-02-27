package com.prosus.androidgames.viral;

import java.util.Random;
import java.util.Stack;

public class World {
    int antiVirusStartY;
    int antiVirusStartX;

    public AntiVirus antiVirus;
    public Stack<Virus> viruses = new Stack<Virus>();
    public Stack<Data> data = new Stack<Data>();
    
    Utils utils;
    public boolean gameOver;
    Random random = new Random();
    
    float lastUpdate = System.nanoTime() / 1000000.0f;
    float currUpdate = System.nanoTime() / 1000000.0f;
    public int score;
    public boolean newScore;
    
    float tickTime;
    float dataTick;
    float virusTick;
    int virusSpeed = Constants.VIRUS_INITIAL_SPEED;
    float virusSpeedTick;
    
    boolean invincible;
    float invRemaining = Constants.INVINCIBILITY_MAX;
    boolean invDrained;

    public World(Utils utils) {
    	this.utils = utils;
    	antiVirusStartY = (utils.CANVAS_HEIGHT - Assets.antiVirus.getHeight()) / 2;
        antiVirusStartX = (utils.CANVAS_WIDTH - Assets.antiVirus.getWidth()) / 2;
        antiVirus = new AntiVirus(antiVirusStartX, antiVirusStartY, utils);
        viruses.push(new Virus(virusSpeed, utils));
        
        for (int i = 0; i < Constants.DATA_NUM_AT_START; i++) {
        	data.push(new Data(true, utils));
        }
    }

    public void update(float deltaTime) {
        if (gameOver) {
            return;
        }
        
        // Want the score to increase by the number of milliseconds passed.
        score += deltaTime * 1000;
        updateInvincibility(deltaTime);

        // Increase tickTime by the time since last update() call.
        tickTime += deltaTime;
        virusTick += deltaTime;
        virusSpeedTick += deltaTime;
        dataTick += deltaTime;

        // Perform 'frames' while tickTime > tick.
        while (tickTime > Constants.FRAME_TIME) {
            tickTime -= Constants.FRAME_TIME;
            antiVirus.move();
            if (virusTick >= Constants.VIRUS_GENERATION_TIME) {
            	virusTick = 0;
            	viruses.push(new Virus(virusSpeed, utils));
            }
            if (dataTick >= Constants.DATA_GENERATION_TIME) {
            	dataTick = 0;
            	data.push(new Data(utils));
            }
            if (virusSpeedTick >= Constants.VIRUS_SPEED_INCREASE_TIME) {
            	virusSpeedTick = 0;
            	if (virusSpeed < Constants.VIRUS_SPEED_MAX) {
            		virusSpeed += Constants.VIRUS_SPEED_INCREMENT;
            	}
            }
            if (!invincible) {
            	checkForCollision();
            }
            moveAndDeleteViruses();
            moveAndDeleteData();
        }
    }
    
    private void updateInvincibility(float deltaTime) {
    	if (invincible && !invDrained) {
    		
    		// Since we want the invincibility meter to last Constants.INVINCIBILITY_MAX seconds, and deltaTime
    		// is in seconds, the amount it's reduced is given by deltaTime * Constants.INVINCIBILITY_MAX. 
    		invRemaining -= deltaTime * Constants.INVINCIBILITY_MAX;
    		if (invRemaining <= 0) {
    			invincible = false;
    			invDrained = true;
    		}
    	// Else, we're not invincible, so we only want to update if invincibility is not full.
    	} else if (invRemaining != Constants.INVINCIBILITY_MAX) {
    		if (invDrained) {
    			// Refill half as fast.
    			invRemaining += deltaTime * Constants.INVINCIBILITY_MAX / 2;
    		} else {
    			invRemaining += deltaTime * Constants.INVINCIBILITY_MAX;
    		}
    		if (invRemaining >= Constants.INVINCIBILITY_MAX) {
    			invRemaining = Constants.INVINCIBILITY_MAX;
    			invDrained = false;
    		}
    	} else {
    		invDrained = false;
    	}
    }
    
    private void checkForCollision() {
    	Stack<Virus> tempViruses = new Stack<Virus>();
    	Virus currVirus;
    	
    	while (!viruses.empty()) {
    		currVirus = viruses.pop();
    		if (!gameOver && currVirus.touchingAntiVirus(antiVirus.getX(), antiVirus.getY())) {
    			if (score > Settings.highscore) {
            		Settings.highscore = score;
            		newScore = true;
            	}
    			gameOver = true;
    			GameScreen.collidedVirus = currVirus;
    		} else {
    			tempViruses.push(currVirus);
    		}
    	}
    	viruses = tempViruses;
    }
    
    private void moveAndDeleteViruses() {
    	Stack<Virus> tempViruses = new Stack<Virus>();
    	Virus currVirus;
    	
    	while (!viruses.empty()) {
    		currVirus = viruses.pop();
    		currVirus.move();
    		if (!currVirus.isOutOfBounds()) {
    			tempViruses.push(currVirus);
    		}
    	}
    	viruses = tempViruses;
    }
    
    private void moveAndDeleteData() {
    	Stack<Data> tempData = new Stack<Data>();
    	Data currData;
    	
    	while (!data.empty()) {
    		currData = data.pop();
    		currData.move();
    		
    		if (!currData.isOutOfBounds()) {
    			tempData.push(currData);
    		}
    	}
    	data = tempData;
    }
}