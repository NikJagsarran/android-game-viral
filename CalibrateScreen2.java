package com.prosus.androidgames.viral;

import java.util.List;
import android.content.Context;

import com.prosus.androidgames.framework.Game;
import com.prosus.androidgames.framework.Graphics;
import com.prosus.androidgames.framework.Pixmap;
import com.prosus.androidgames.framework.Screen;
import com.prosus.androidgames.framework.Input.TouchEvent;

public class CalibrateScreen2 extends Screen {
	Button readyButton;
	Button backButton;
	
	int textX;
	int textY;
	int textHeight;
	Pixmap calibrateText = Assets.calibrateScreen2ThreeSeconds;
	
	int doneX;
	int doneY;
	Button continueButton;
	
	enum CalibrateState {
		Ready,
		Calibrating,
		Done
	}
	CalibrateState state = CalibrateState.Ready;
	
	float currAccelY;
	float lastAccelY;
	float accelZ;
	float currArctan;
	float lastArctan = Integer.MAX_VALUE;
	boolean positiveY = true;
	
	// Using a angleUpdated so the angle can only be displayed once it has been
	// updated. Otherwise, Integer.MAX_VALUE flashes on the screen due to present().
	boolean angleUpdated = false;
	
	// Wanted to use 0.03 tolerance, but requires very steady hands.
	float tolerance = 0.06f;
	
	float timeToHold = 3f;
	float timePassed;
	
	String angleString;
	int angleStringRight;
	int angleStringY;
	
	boolean faceUp = true;
	Pixmap faceText = Assets.calibrateScreen2FaceUp;
	int faceTextY;
	int faceTextX;
	
	private void initialize() {
		readyButton = new Button((utils.CANVAS_WIDTH - Assets.calibrateScreen2Ready.getWidth()) / 2,
				(utils.CANVAS_HEIGHT - Assets.calibrateScreen2Ready.getHeight()) / 2,
				Assets.calibrateScreen2Ready, Assets.calibrateScreen2ReadyClicked);
		backButton = new Button((utils.CANVAS_WIDTH - Assets.optionsScreenBackButton.getWidth()) / 2,
				(int) (utils.CANVAS_HEIGHT * 0.97) - Assets.optionsScreenBackButton.getHeight(),
				Assets.optionsScreenBackButton, Assets.optionsScreenBackButtonClicked);
		
		textY = (int) (utils.CANVAS_HEIGHT * 0.6);
		textHeight = readyButton.getHeight();
		
		doneX = (utils.CANVAS_WIDTH - Assets.calibrateScreen2Done.getWidth()) / 2;
		doneY = textY + ((textHeight - Assets.calibrateScreen2Done.getHeight()) / 2);
		
		continueButton = new Button((utils.CANVAS_WIDTH - Assets.calibrateScreen2Continue.getWidth()) / 2,
				doneY + (int) (utils.CANVAS_HEIGHT * 0.2), Assets.calibrateScreen2Continue,
				Assets.calibrateScreen2ContinueClicked);
		
		angleStringRight = utils.CANVAS_WIDTH / 2;
		angleStringY = (int) (utils.CANVAS_HEIGHT * 0.25);
		
		faceTextY = angleStringY + Assets.newScoreDigits.getHeight() + (int) (utils.CANVAS_HEIGHT * 0.05);
		faceTextX = (utils.CANVAS_WIDTH - faceText.getWidth()) / 2;
	}
	
	public CalibrateScreen2(Game game, Context context, Utils utils) {
		super(game, context, utils);
		initialize();
	}

	@Override
	public void update(float deltaTime) {
		// Handle the input for the Ready button during the Ready state.
		if (state == CalibrateState.Ready) {
			updateReady(deltaTime);
	        
	    // Handle calibrating the vertical axes.
		} else if (state == CalibrateState.Calibrating) {
			updateCalibrating(deltaTime);
			
		// Handle the final Done state.
		} else if (state == CalibrateState.Done) {
			List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
	        int len = 0;
	        if (touchEvents != null) {
	        	len = touchEvents.size();
	        }
	        
	        for (int i = 0; i < len; i++) {
	            TouchEvent event = touchEvents.get(i);
	            
	            // Handle Continue button clicking.
	            if (event != null && event.type == TouchEvent.TOUCH_UP) {
	            	if (continueButton.eventInBounds(event)) {
	            		game.setScreen(new OptionsScreen(game, context, utils));
	            		if (Settings.soundEnabled) {
	        				Assets.click.play(1);
	        			}
	            	}
            			
	            // Else handle Continue button highlighting.
	            } else if (event != null && (event.type == TouchEvent.TOUCH_DOWN
	            		|| event.type == TouchEvent.TOUCH_DRAGGED)) {
	            	if (continueButton.eventInBounds(event)) {
            			continueButton.setHighlighted();
            		} else {
            			continueButton.removeHighlighted();
            		}
	            }
	        }
		}
	}
	
	private void updateReady(float deltaTime) {
		List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
		int len = 0;
		if (touchEvents != null) {
			len = touchEvents.size();
		}
		
        for (int i = 0; i < len; i++) {
            TouchEvent event = touchEvents.get(i);
            
            // Handle Ready button clicking.
            if (event != null && event.type == TouchEvent.TOUCH_UP) {
            	if (readyButton.eventInBounds(event)) {
            		state = CalibrateState.Calibrating;
            		if (Settings.soundEnabled) {
        				Assets.click.play(1);
        			}
            	} else if (backButton.eventInBounds(event)) {
            		game.setScreen(new CalibrateScreen1(game, context, utils));
            		if (Settings.soundEnabled) {
        				Assets.click.play(1);
        			}
            	}
        			
            // Else handle Ready button highlighting.
            } else if (event != null && (event.type == TouchEvent.TOUCH_DOWN || event.type == TouchEvent.TOUCH_DRAGGED)) {
            	if (readyButton.eventInBounds(event)) {
        			readyButton.setHighlighted();
        		} else {
        			readyButton.removeHighlighted();
        		}
            	
            	if (backButton.eventInBounds(event)) {
        			backButton.setHighlighted();
        		} else {
        			backButton.removeHighlighted();
        		}
            }
        }
	}
	
	private void updateCalibrating(float deltaTime) {
		List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
		int len = 0;
		if (touchEvents != null) {
			len = touchEvents.size();
		}
		
        for (int i = 0; i < len; i++) {
            TouchEvent event = touchEvents.get(i);
            if (event != null && event.type == TouchEvent.TOUCH_UP && backButton.eventInBounds(event)) {
        		game.setScreen(new CalibrateScreen1(game, context, utils));
        		if (Settings.soundEnabled) {
    				Assets.click.play(1);
    			}
            } else if (event != null && (event.type == TouchEvent.TOUCH_DOWN
            		|| event.type == TouchEvent.TOUCH_DRAGGED)) {
            	if (backButton.eventInBounds(event)) {
        			backButton.setHighlighted();
        		} else {
        			backButton.removeHighlighted();
        		}
            }
        }
		
        // Handle angle calculations.
		if (timePassed < timeToHold) {
			timePassed += deltaTime;
			lastAccelY = currAccelY;
			currAccelY = game.getInput().getAccelY();
			accelZ = game.getInput().getAccelZ();
			
			// This condition should only occur if this is the first time the loop
			// is running. In which case, need to update lastAccelY otherwise its
			// original value of 0 might mess up the calibration.
			if (lastAccelY == 0) {
				lastAccelY = currAccelY;
			}
			
			if (currAccelY >= 0) {
				positiveY = true;
			} else {
				positiveY = false;
			}
			
			// Setting faceUp and its text to use.
			if (accelZ >= 0 && !faceUp) {
				faceUp = true;
				faceText = Assets.calibrateScreen2FaceUp;
				faceTextX = (utils.CANVAS_WIDTH - Assets.calibrateScreen2FaceUp.getWidth()) / 2;
			} else if (accelZ < 0 && faceUp) {
				faceUp = false;
				faceText = Assets.calibrateScreen2FaceDown;
				faceTextX = (utils.CANVAS_WIDTH - Assets.calibrateScreen2FaceDown.getWidth()) / 2;
			}
			
	        currArctan = (float) Math.atan(accelZ / currAccelY);
			
	        // Set currArctan to 0 if it's NaN, meaning currAccelY is 0.
			if (Float.isNaN(currArctan)) {
				currArctan = 0f;
			}
			
			// If the sign of accelY has changed and one of the arctans'
			// absolute values is greater than (Math.PI / 2) - tolerance.
			if ((lastAccelY * currAccelY < 0)
				&& (Math.abs(lastArctan) > ((Math.PI / 2) - tolerance)
				|| Math.abs(currArctan) > ((Math.PI / 2) - tolerance))) {

				if (((Math.PI / 2) - Math.abs(lastArctan)) + ((Math.PI / 2) - Math.abs(currArctan)) >= tolerance) {
					lastAccelY = currAccelY;
					lastArctan = currArctan;
					timePassed = 0;
								
					if (!angleUpdated) {
						angleUpdated = true;
					}
				}
				
			// Otherwise, if they're not within tolerance of each other.
			} else if (Math.abs(lastArctan - currArctan) >= tolerance) {
				lastArctan = currArctan;
				timePassed = 0;
			
				if (!angleUpdated) {
					angleUpdated = true;
				}
			}
			
			// Update the text to display based on the amount of time remaining.
			if (timePassed <= 1) {
				calibrateText = Assets.calibrateScreen2ThreeSeconds;
			} else if (timePassed > 1 && timePassed <= 2) {
				calibrateText = Assets.calibrateScreen2TwoSeconds;
			} else {
				calibrateText = Assets.calibrateScreen2OneSecond;
			}
			
		// Else, timePassed >= timeToHold, and update the state to Done.
		} else {
			Settings.neutralYAccel = lastAccelY;
			Settings.neutralZAccel = accelZ;
			Settings.neutralPositiveY = positiveY;
			Settings.save(game.getFileIO());
			state = CalibrateState.Done;
		}
	}

	@Override
	public void present(float deltaTime) {
		Graphics g = game.getGraphics();
		g.clear(android.graphics.Color.rgb(Constants.BG_RED, Constants.BG_GREEN, Constants.BG_BLUE));
		
		if (state == CalibrateState.Ready) {
			readyButton.draw(g);
			backButton.draw(g);
		} else if (state == CalibrateState.Calibrating) {
			presentCalibrating(g);
		} else if (state == CalibrateState.Done) {
			presentDone(g);
		}
	}
	
	private void presentCalibrating(Graphics g) {
		int angle = Math.abs((int) (180 * lastArctan / Math.PI));
		
		if (angleUpdated) {
			angleString = "" + angle;
			
			// Simplified calculation dependent on number of digits.
			angleStringRight = (utils.CANVAS_WIDTH
					+ (angleString.length() - 1) * Constants.SCORE_DIGIT_WIDTH) / 2;
			
			Constants.drawScore(g, Assets.newScoreDigits, angleString, angleStringRight, angleStringY);
			g.drawPixmap(Assets.calibrateScreen2DegreeSymbol, angleStringRight, angleStringY);
		}
		
		g.drawPixmap(faceText, faceTextX, faceTextY);
		textX = (utils.CANVAS_WIDTH - calibrateText.getWidth()) / 2;
		g.drawPixmap(calibrateText, textX, textY);
		backButton.draw(g);
	}
	
	private void presentDone(Graphics g) {
		Constants.drawScore(g, Assets.newScoreDigits, angleString, angleStringRight, angleStringY);
		g.drawPixmap(Assets.calibrateScreen2DegreeSymbol, angleStringRight, angleStringY);
		g.drawPixmap(faceText, faceTextX, faceTextY);
		g.drawPixmap(Assets.calibrateScreen2Done, doneX, doneY);
		continueButton.draw(g);
	}

	@Override
	public void pause() {
		if (Assets.menuMusic.isPlaying()) {
			Assets.menuMusic.stop();
    	}
	}

	@Override
	public void resume() {
		if (Settings.soundEnabled) {
			Assets.menuMusic.play();
    	}
	}

	@Override
	public void dispose() {}
}
