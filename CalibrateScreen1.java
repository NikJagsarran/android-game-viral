package com.prosus.androidgames.viral;

import java.util.LinkedList;
import java.util.List;

import android.content.Context;

import com.prosus.androidgames.framework.Game;
import com.prosus.androidgames.framework.Graphics;
import com.prosus.androidgames.framework.Pixmap;
import com.prosus.androidgames.framework.Screen;
import com.prosus.androidgames.framework.Input.TouchEvent;

public class CalibrateScreen1 extends Screen {
	int resetButtonY;
	int betweenNoAndYes;
	int noYesButtonsY;
	Button noButton;
	Button yesButton;

	int text1X;
	int text1Y;
	int spacingBetweenTextsAndImages;
	int headX;
	int headY;
	int deviceTextX;
	int deviceTextY;
	int deviceX;
	int deviceY;
	int text2X;
	int text2Y;

	int resetPopupBGX;
	int resetPopupBGY;
	int resetPopupNoYesButtonsY; 
	int resetPopupBetweenNoYes;
	Button resetPopupNoButton;
	Button resetPopupYesButton;

	// Need a value such that if the difference between the current neutral angle and default angle
	// is less than this value, then we disable the reset button. Value chosen arbitrarily small.
	float angleDifferenceTolerance = 0.00001f;
	boolean angleIsDefault = false;

	float neutralAngle = (float) Math.atan(Settings.neutralZAccel / Settings.neutralYAccel);
	Button resetButton;

	// Handle the fading stages and timers.
	enum CalibrateScreen1Stages {
		HeadFadeIn, DeviceFadeIn, DeviceTextFadeIn, Text1FadeIn, Text2FadeIn, FadeInComplete, ResetPopup
	}

	// Storing the order of stages in a LinkedList so we can just go to the next Node after each stage.
	LinkedList<CalibrateScreen1Stages> stages = new LinkedList<CalibrateScreen1Stages>();
	CalibrateScreen1Stages stage;

	int alpha = 0;
	float fadeInTimePerStage = 0.8f; // Seconds.
	float fadeInTimeRemaining = fadeInTimePerStage;

	float timeBeforeFading = 0.2f; // Seconds.
	float preFadingTimeRemaining = timeBeforeFading;
	
	private void initialize() {
		// Calculate and create the buttons.
		resetButtonY = (int) (utils.CANVAS_HEIGHT * 0.97)
				- Assets.calibrateScreen1ResetToDefault.getHeight();
		betweenNoAndYes = (utils.CANVAS_WIDTH - Assets.calibrateScreen1No.getWidth()
				- Assets.calibrateScreen1Yes.getWidth()) / 3;
		noYesButtonsY = resetButtonY - Assets.calibrateScreen1No.getHeight()
				- (int) (utils.CANVAS_HEIGHT * 0.03);

		noButton = new Button(betweenNoAndYes, noYesButtonsY, Assets.calibrateScreen1No,
				Assets.calibrateScreen1NoClicked);
		yesButton = new Button(2 * betweenNoAndYes + noButton.getWidth(), noYesButtonsY,
				Assets.calibrateScreen1Yes,	Assets.calibrateScreen1YesClicked);

		// Calculate the texts' and images' locations.
		text1X = (utils.CANVAS_WIDTH - Assets.calibrateScreen1Text1.getWidth()) / 2;
		text1Y = (int) (utils.CANVAS_HEIGHT * 0.05);

		spacingBetweenTextsAndImages = (noButton.getY() - (text1Y + Assets.calibrateScreen1Text1.getHeight()
				+ Assets.calibrateScreen1Head.getHeight() + Assets.calibrateScreen1Text2.getHeight())) / 3;

		headX = (int) (utils.CANVAS_WIDTH * 0.51);
		headY = text1Y + Assets.calibrateScreen1Text1.getHeight() + spacingBetweenTextsAndImages;

		deviceTextX = (int) (utils.CANVAS_WIDTH * 0.15);
		deviceTextY = headY + Assets.calibrateScreen1Head.getHeight()
				- Assets.calibrateScreen1DeviceText.getHeight();

		deviceX = (int) (utils.CANVAS_WIDTH * 0.2);
		deviceY = (int) (deviceTextY - Assets.calibrateScreen1Device.getHeight()
				- (int) (utils.CANVAS_HEIGHT * 0.03));

		text2X = (utils.CANVAS_WIDTH - Assets.calibrateScreen1Text2.getWidth()) / 2;
		text2Y = deviceTextY + Assets.calibrateScreen1DeviceText.getHeight() + spacingBetweenTextsAndImages;

		// Calculate and create the popup buttons.
		resetPopupBGX = (utils.CANVAS_WIDTH - Assets.calibrateScreen1ResetPopupBG.getWidth()) / 2;
		resetPopupBGY = (utils.CANVAS_HEIGHT - Assets.calibrateScreen1ResetPopupBG.getHeight()) / 2;
		
		resetPopupNoYesButtonsY = resetPopupBGY + (int) (0.9 * Assets.calibrateScreen1ResetPopupBG.getHeight())
				- noButton.getHeight(); 
		resetPopupBetweenNoYes = (Assets.calibrateScreen1ResetPopupBG.getWidth()
				- noButton.getWidth() - yesButton.getWidth()) / 3;
		
		resetPopupNoButton = new Button(resetPopupBGX + resetPopupBetweenNoYes, resetPopupNoYesButtonsY,
				Assets.calibrateScreen1No, Assets.calibrateScreen1NoClicked);
		resetPopupYesButton = new Button(resetPopupBGX + resetPopupNoButton.getWidth()
				+ 2 * resetPopupBetweenNoYes, resetPopupNoYesButtonsY,
				Assets.calibrateScreen1Yes, Assets.calibrateScreen1YesClicked);
	}

	public CalibrateScreen1(Game game, Context context, Utils utils) {
		super(game, context, utils);
		initialize();
		createResetButton();

		stages.add(CalibrateScreen1Stages.HeadFadeIn);
		stages.add(CalibrateScreen1Stages.DeviceFadeIn);
		stages.add(CalibrateScreen1Stages.DeviceTextFadeIn);
		stages.add(CalibrateScreen1Stages.Text1FadeIn);
		stages.add(CalibrateScreen1Stages.Text2FadeIn);
		stages.add(CalibrateScreen1Stages.FadeInComplete);
		stage = stages.remove();
	}

	// Creates the "Reset to Default" button by determining which of its images to use.
	private void createResetButton() {
		Pixmap resetButtonImage = Assets.calibrateScreen1ResetToDefault;
		Pixmap resetButtonClickedImage = Assets.calibrateScreen1ResetToDefaultClicked;

		// Setting angleIsDefault to true so we can disable the reset button.
		if (Math.abs(neutralAngle - Constants.DEFAULT_ANGLE) < angleDifferenceTolerance) {
			angleIsDefault = true;
			resetButtonImage = Assets.calibrateScreen1ResetToDefaultDisabled;
			resetButtonClickedImage = Assets.calibrateScreen1ResetToDefaultDisabled;
		}

		resetButton = new Button((utils.CANVAS_WIDTH - resetButtonImage.getWidth()) / 2,
				resetButtonY, resetButtonImage, resetButtonClickedImage);
	}

	@Override
	public void update(float deltaTime) {
		// Only want to fade in and check main buttons when the reset popup is not available.
		if (stage != CalibrateScreen1Stages.ResetPopup) {
			if (stage != CalibrateScreen1Stages.FadeInComplete) {
				// Pre-fading waiting time.
				if (preFadingTimeRemaining > 0) {
					preFadingTimeRemaining -= deltaTime;
				} else {
					alpha += (int) ((deltaTime / fadeInTimePerStage) * 255f);
					fadeInTimeRemaining -= deltaTime;

					if (fadeInTimeRemaining < 0) {
						stage = stages.remove();
						preFadingTimeRemaining = timeBeforeFading;
						fadeInTimeRemaining = fadeInTimePerStage;
						alpha = 0;
					}
				}
			}
			checkMainButtons();

		// Otherwise, we want to check the reset popup buttons.
		} else {
			checkResetPopupButtons();
		}
	}

	// Checks the main no, yes, and reset buttons.
	private void checkMainButtons() {
		List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
        int len = 0;
        if (touchEvents != null) {
        	len = touchEvents.size();
        }
        
        for (int i = 0; i < len; i++) {
            TouchEvent event = touchEvents.get(i);
            
            // Handle button clicking.
            if (event != null && event.type == TouchEvent.TOUCH_UP) {
        		if (noButton.eventInBounds(event)) {
        			game.setScreen(new OptionsScreen(game, context, utils));
        			if (Settings.soundEnabled) {
        				Assets.click.play(1);
        			}
        			return;
        		} else if (yesButton.eventInBounds(event)) {
        			game.setScreen(new CalibrateScreen2(game, context, utils));
        			if (Settings.soundEnabled) {
        				Assets.click.play(1);
        			}
        			return;
        		} else if (!angleIsDefault && resetButton.eventInBounds(event)) {
					stage = CalibrateScreen1Stages.ResetPopup;

					if (Settings.soundEnabled) {
						Assets.click.play(1);
					}
				} else if (stage != CalibrateScreen1Stages.FadeInComplete) {
					stage = CalibrateScreen1Stages.FadeInComplete;
				}
            	
            // Else handle highlighting.
            } else if (event != null && (event.type == TouchEvent.TOUCH_DOWN
            		|| event.type == TouchEvent.TOUCH_DRAGGED)) {
        		if (noButton.eventInBounds(event)) {
        			noButton.setHighlighted();
        		} else {
        			noButton.removeHighlighted();
        		}
        		
        		if (yesButton.eventInBounds(event)) {
        			yesButton.setHighlighted();
        		} else {
        			yesButton.removeHighlighted();
        		}
        		
        		if (!angleIsDefault && resetButton.eventInBounds(event)) {
        			resetButton.setHighlighted();
        		} else {
        			resetButton.removeHighlighted();
        		}
            }
        }
	}

	// Checks the popup's no and yes buttons.
	private void checkResetPopupButtons() {
		List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
        int len = 0;
        if (touchEvents != null) {
        	len = touchEvents.size();
        }
        
        for (int i = 0; i < len; i++) {
            TouchEvent event = touchEvents.get(i);
            
            // Handle button clicking.
            if (event != null && event.type == TouchEvent.TOUCH_UP) {
            	
            	// If the popup's "No" button is clicked, we just want to go  back to the previous stage.
        		if (resetPopupNoButton.eventInBounds(event)) {
        			stage = CalibrateScreen1Stages.FadeInComplete;
        			resetPopupNoButton.removeHighlighted();
        			resetButton.removeHighlighted();
					if (Settings.soundEnabled) {
						Assets.click.play(1);
					}
        			return;
        			
        		} else if (resetPopupYesButton.eventInBounds(event)) {
        			// If the popup's "Yes" button is clicked, we need to update the calibration and stage.
					stage = CalibrateScreen1Stages.FadeInComplete;
					resetPopupYesButton.removeHighlighted();

					neutralAngle = Constants.DEFAULT_ANGLE;
					Settings.neutralYAccel = Constants.DEFAULT_Y_ACCEL;
					Settings.neutralZAccel = Constants.DEFAULT_Z_ACCEL;
					Settings.save(game.getFileIO());
					angleIsDefault = true;

					resetButton.setRegularImage(Assets.calibrateScreen1ResetToDefaultDisabled);
					resetButton.setHighlightedImage(Assets.calibrateScreen1ResetToDefaultDisabled);

					// Have to remove highlighted because in the reset
					// button object, the reference
					// to the button to currently display is still pointing
					// at the highlighted image.
					resetButton.removeHighlighted();

					if (Settings.soundEnabled) {
						Assets.click.play(1);
					}
        			return;
        		}
            	
            // Else handle highlighting.
            } else if (event != null && (event.type == TouchEvent.TOUCH_DOWN
            		|| event.type == TouchEvent.TOUCH_DRAGGED)) {
        		if (resetPopupNoButton.eventInBounds(event)) {
        			resetPopupNoButton.setHighlighted();
        		} else {
        			resetPopupNoButton.removeHighlighted();
        		}
        		
        		if (resetPopupYesButton.eventInBounds(event)) {
        			resetPopupYesButton.setHighlighted();
        		} else {
        			resetPopupYesButton.removeHighlighted();
        		}
            }
        }
	}

	@Override
	public void present(float deltaTime) {
		Graphics g = game.getGraphics();
		g.clear(android.graphics.Color.rgb(Constants.BG_RED,
				Constants.BG_GREEN, Constants.BG_BLUE));

		noButton.draw(g);
		yesButton.draw(g);
		resetButton.draw(g);

		if (stage == CalibrateScreen1Stages.HeadFadeIn) {
			g.drawPixmap(Assets.calibrateScreen1Head, headX, headY, alpha);
			
		} else if (stage == CalibrateScreen1Stages.DeviceFadeIn) {
			g.drawPixmap(Assets.calibrateScreen1Head, headX, headY);
			g.drawPixmap(Assets.calibrateScreen1Device, deviceX, deviceY, alpha);
			
		} else if (stage == CalibrateScreen1Stages.DeviceTextFadeIn) {
			g.drawPixmap(Assets.calibrateScreen1Head, headX, headY);
			g.drawPixmap(Assets.calibrateScreen1Device, deviceX, deviceY);
			g.drawPixmap(Assets.calibrateScreen1DeviceText, deviceTextX, deviceTextY, alpha);
			
		} else if (stage == CalibrateScreen1Stages.Text1FadeIn) {
			g.drawPixmap(Assets.calibrateScreen1Head, headX, headY);
			g.drawPixmap(Assets.calibrateScreen1Device, deviceX, deviceY);
			g.drawPixmap(Assets.calibrateScreen1DeviceText, deviceTextX, deviceTextY);
			g.drawPixmap(Assets.calibrateScreen1Text1, text1X, text1Y, alpha);
			
		} else if (stage == CalibrateScreen1Stages.Text2FadeIn) {
			g.drawPixmap(Assets.calibrateScreen1Head, headX, headY);
			g.drawPixmap(Assets.calibrateScreen1Device, deviceX, deviceY);
			g.drawPixmap(Assets.calibrateScreen1DeviceText, deviceTextX, deviceTextY);
			g.drawPixmap(Assets.calibrateScreen1Text1, text1X, text1Y);
			g.drawPixmap(Assets.calibrateScreen1Text2, text2X, text2Y, alpha);
			
		} else if (stage == CalibrateScreen1Stages.FadeInComplete || stage == CalibrateScreen1Stages.ResetPopup) {
			g.drawPixmap(Assets.calibrateScreen1Head, headX, headY);
			g.drawPixmap(Assets.calibrateScreen1Device, deviceX, deviceY);
			g.drawPixmap(Assets.calibrateScreen1DeviceText, deviceTextX, deviceTextY);
			g.drawPixmap(Assets.calibrateScreen1Text1, text1X, text1Y);
			g.drawPixmap(Assets.calibrateScreen1Text2, text2X, text2Y);
		}

		if (stage == CalibrateScreen1Stages.ResetPopup) {
			drawPopupUI(g);
		}
	}

	private void drawPopupUI(Graphics g) {
		g.drawPixmap(Assets.calibrateScreen1ResetPopupBG, resetPopupBGX, resetPopupBGY);
		resetPopupNoButton.draw(g);
		resetPopupYesButton.draw(g);
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