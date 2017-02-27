package com.prosus.androidgames.viral;

import java.util.Stack;
import android.content.Context;
import com.prosus.androidgames.framework.Game;
import com.prosus.androidgames.framework.Graphics;

public class HelpScreen2 extends HelpScreen {
	int textY;
	int textX;
	int antiVirusStartX;
	int antiVirusStartY;
	int antiVirusLeftEndX;
	int antiVirusLeftEndY;
	int antiVirusDownLeftEndX;
	int antiVirusDownLeftEndY;
	int antiVirusRightEndX;
	int antiVirusRightEndY;
	
	// Storing these as floats since they may lose accuracy as ints.
	float antiVirusX;
	float antiVirusY;
	
	enum MotionStages {
		MotionPause,
		AntiVirusLeft,
		AntiVirusDownLeft,
		AntiVirusRight,
		AntiVirusUpRight
	}
	
	MotionStages motionStage = MotionStages.MotionPause;
	float motionStageLength = 1f;
	float motionStageLengthRemaining = motionStageLength;
	
	Stack<Virus> viruses = new Stack<Virus>();
	float virusMovementTickTime;
	int virusSpeed = 6;
	int topVirusX;
	boolean topVirusCreated;
	int rightVirusY;
	
	private void initialize() {
		stages.add(HelpScreenStages.AntiVirusFadeIn);
		stages.add(HelpScreenStages.HelpScreen2TextFadeIn);
		stages.add(HelpScreenStages.FadeInComplete);
		stage = stages.remove();
		
		textY = (int) (utils.CANVAS_HEIGHT * 0.05); // 5% down, calculated with a 720p layout.
		textX = (utils.CANVAS_WIDTH - Assets.helpScreen2Text.getWidth()) / 2;
		antiVirusStartX = (int) (utils.CANVAS_WIDTH * 0.8); // 80% over.
		antiVirusStartY = textY + Assets.helpScreen2Text.getHeight() + (int) (utils.CANVAS_HEIGHT * 0.2);
		
		// Calculating the coordinates for the end of each stage.
		antiVirusLeftEndX = antiVirusStartX - (int) (utils.CANVAS_WIDTH * 0.35);
		antiVirusLeftEndY = antiVirusStartY;

		antiVirusDownLeftEndX = antiVirusLeftEndX - (int) (utils.CANVAS_WIDTH * 0.35);
		antiVirusDownLeftEndY = antiVirusStartY + (int) (utils.CANVAS_HEIGHT * 0.2);
		
		antiVirusRightEndX = antiVirusLeftEndX;
		antiVirusRightEndY = antiVirusDownLeftEndY;
		
		antiVirusX = antiVirusStartX;
		antiVirusY = antiVirusStartY;
		
		topVirusX = (int) (utils.CANVAS_WIDTH * 0.35);
		rightVirusY = antiVirusStartY;
	}

	public HelpScreen2(Game game, Context context, Utils utils) {
		super(game, context, utils);
		initialize();
	}

	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		if (stage == HelpScreenStages.FadeInComplete) {
			updateAntiVirusMotion(deltaTime);
			updateViruses(deltaTime);
		}
		
		// Handle clicking of Next button.
		super.checkTapping(CurrHelpScreenEnum.HelpScreen2);
	}
	
	// Updates the AntiVirus' motion as necessary.
	private void updateAntiVirusMotion(float deltaTime) {
		motionStageLengthRemaining -= deltaTime;
		
		// If motionStageLengthRemaining runs out, we need to update the motion stage,
		// and the AntiVirus' position since it may be slightly inaccurate due to (lag)?.
		if (motionStageLengthRemaining <= 0) {
			motionStageLengthRemaining = motionStageLength;
			if (motionStage == MotionStages.MotionPause) {
				motionStage = MotionStages.AntiVirusLeft;
				
			} else if (motionStage == MotionStages.AntiVirusLeft) {
				motionStage = MotionStages.AntiVirusDownLeft;
				antiVirusX = antiVirusLeftEndX;
				antiVirusY = antiVirusLeftEndY;
				
			} else if (motionStage == MotionStages.AntiVirusDownLeft) {
				motionStage = MotionStages.AntiVirusRight;
				antiVirusX = antiVirusDownLeftEndX;
				antiVirusY = antiVirusDownLeftEndY;
				
			} else if (motionStage == MotionStages.AntiVirusRight) {
				motionStage = MotionStages.AntiVirusUpRight;
				antiVirusX = antiVirusRightEndX;
				antiVirusY = antiVirusRightEndY;
				
			} else if (motionStage == MotionStages.AntiVirusUpRight) {
				motionStage = MotionStages.MotionPause;
				antiVirusX = antiVirusStartX;
				antiVirusY = antiVirusStartY;
			}
		}
		
		// Update the AntiVirus position by calculating it's percentage between
		// the end of the previous stage and the end of the current stage.
		if (motionStage == MotionStages.AntiVirusLeft) {
			antiVirusX -= deltaTime * (antiVirusStartX - antiVirusLeftEndX);
			
		} else if (motionStage == MotionStages.AntiVirusDownLeft) {
			antiVirusX -= deltaTime * (antiVirusLeftEndX - antiVirusDownLeftEndX);
			antiVirusY += deltaTime * (antiVirusDownLeftEndY - antiVirusLeftEndY);
			
		} else if (motionStage == MotionStages.AntiVirusRight) {
			antiVirusX += deltaTime * (antiVirusRightEndX - antiVirusDownLeftEndX);
			
		} else if (motionStage == MotionStages.AntiVirusUpRight) {
			antiVirusX += deltaTime * (antiVirusStartX - antiVirusRightEndX);
			antiVirusY -= deltaTime * (antiVirusRightEndY - antiVirusStartY);
		}
	}
	
	private void updateViruses(float deltaTime) {
		// Create the upper Virus just before the end of the AntiVirus moving left.
		// Need to check if it was created since multiple may be created if these
		// conditions are satisfied more than once in a cycle.
		if (motionStage == MotionStages.AntiVirusLeft && motionStageLengthRemaining <= 0.1 && !topVirusCreated) {
			viruses.push(new Virus(topVirusX, (0 - Assets.virus.getHeight()), 0, virusSpeed, utils));
			topVirusCreated = true;
		}
		
		if (motionStage == MotionStages.AntiVirusLeft && motionStageLengthRemaining == motionStageLength) {
			viruses.push(new Virus(utils.CANVAS_WIDTH, rightVirusY,
					(-1 * virusSpeed), 0, utils));
		}
		
		// Reset topVirusCreated.
		if (topVirusCreated && motionStage != MotionStages.AntiVirusLeft) {
			topVirusCreated = false;
		}
		
		virusMovementTickTime += deltaTime;
		while (virusMovementTickTime > Constants.FRAME_TIME) {
            virusMovementTickTime -= Constants.FRAME_TIME;
            moveAndDeleteViruses();
		}
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

	@Override
	public void present(float deltaTime) {
		// Not using super.present() here because we want the viruses to be drawn under the Next button.
		Graphics g = game.getGraphics();
		g.clear(android.graphics.Color.rgb(Constants.BG_RED, Constants.BG_GREEN, Constants.BG_BLUE));
		
		drawViruses(g);
		nextButton.draw(g);
		
		if (stage == HelpScreenStages.AntiVirusFadeIn) {
			g.drawPixmap(Assets.antiVirus, (int) antiVirusX, (int) antiVirusY, alpha);
			
		} else if (stage == HelpScreenStages.HelpScreen2TextFadeIn) {
			g.drawPixmap(Assets.antiVirus, (int) antiVirusX, (int) antiVirusY);
			g.drawPixmap(Assets.helpScreen2Text, textX, textY, alpha);
			
		} else {
			g.drawPixmap(Assets.antiVirus, (int) antiVirusX, (int) antiVirusY);
			g.drawPixmap(Assets.helpScreen2Text, textX, textY);
		}
	}
	
	private void drawViruses(Graphics g) {
    	Stack<Virus> tempViruses = new Stack<Virus>();
    	Virus currVirus;
    	while (!viruses.empty()) {
    		currVirus = viruses.pop();
    		g.drawPixmap(Assets.virus, currVirus.getX(), currVirus.getY());
    		tempViruses.push(currVirus);
    	}
    	viruses = tempViruses;
    }

	// pause() and resume() handled by HelpScreen.java.
}
