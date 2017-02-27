package com.prosus.androidgames.viral;

import java.util.List;
import android.annotation.SuppressLint;
import android.content.Context;

import com.prosus.androidgames.framework.Game;
import com.prosus.androidgames.framework.Graphics;
import com.prosus.androidgames.framework.Input.TouchEvent;
import com.prosus.androidgames.framework.Music;
import com.prosus.androidgames.framework.Screen;

public class MainMenuScreen extends Screen {
	int titleY;
    int titleX;
    Button playButton;
    Button helpButton;
    Button optionsButton;
    int scoreY;
    int highScoreTextX;
    Music menuMusic = Assets.menuMusic;
	
    private void initialize() {
    	if (Settings.soundEnabled && !menuMusic.isPlaying()) {
        	menuMusic.play();
        }
    	titleX = (int) ((utils.CANVAS_WIDTH - Assets.title.getWidth()) / 2);
        
        // Play button 56% down from the top.
        playButton = new Button((utils.CANVAS_WIDTH - Assets.mainMenuPlayButton.getWidth()) / 2,
        		(int) (utils.CANVAS_HEIGHT * 0.56), Assets.mainMenuPlayButton, Assets.mainMenuPlayButtonClicked);
        
        // 2% screen height distance between each button.
        helpButton = new Button((utils.CANVAS_WIDTH - Assets.mainMenuHelpButton.getWidth()) / 2,
        		playButton.getY() + playButton.getHeight() + (int) (utils.CANVAS_HEIGHT * 0.02), Assets.mainMenuHelpButton,
        		Assets.mainMenuHelpButtonClicked);
        optionsButton = new Button((utils.CANVAS_WIDTH - Assets.mainMenuOptionsButton.getWidth()) / 2,
        		helpButton.getY() + helpButton.getHeight() + (int) (utils.CANVAS_HEIGHT * 0.02), Assets.mainMenuOptionsButton,
        		Assets.mainMenuOptionsButtonClicked);
        
        scoreY = utils.CANVAS_HEIGHT - Assets.mainMenuHighScoreText.getHeight();
        highScoreTextX = utils.CANVAS_WIDTH - Assets.mainMenuHighScoreText.getWidth() -
        		(int) (Constants.SCORE_DIGIT_WIDTH * (("" + Settings.highscore).length() + 0.5));
    }
    
	public MainMenuScreen(Game game, Context context, Utils utils) {
        super(game, context, utils);
        initialize();
    }   

    public void update(float deltaTime) {
        List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
        int len = 0;
        if (touchEvents != null) {
        	len = touchEvents.size();
        }
        
        for(int i = 0; i < len; i++) {
            TouchEvent event = touchEvents.get(i);
            if (event != null && event.type == TouchEvent.TOUCH_UP) {
            	
                if (playButton.eventInBounds(event)) {
                    game.setScreen(new GameScreen(game, context, utils));
                    if (Settings.soundEnabled) {
                        Assets.click.play(1);
                    }
                    if (menuMusic.isPlaying()) {
                    	menuMusic.stop();
                    }
                    return;
                }
                
                if (helpButton.eventInBounds(event)) {
                    game.setScreen(new HelpScreen1(game, context, utils));
                    if (Settings.soundEnabled) {
                        Assets.click.play(1);
                    }
                    return;
                }
                
                if (optionsButton.eventInBounds(event)) {
                	game.setScreen(new OptionsScreen(game, context, utils));
                	if (Settings.soundEnabled){
                		Assets.click.play(1);
                	}
                	return;
                }
                
            // Else handle highlighting.
            } else if (event != null && (event.type == TouchEvent.TOUCH_DOWN || event.type == TouchEvent.TOUCH_DRAGGED)) {
                if (playButton.eventInBounds(event)) {
                    playButton.setHighlighted();
                } else {
                	playButton.removeHighlighted();
                }
                
                if (helpButton.eventInBounds(event)) {
                    helpButton.setHighlighted();
                } else {
                	helpButton.removeHighlighted();
                }
                
                if (optionsButton.eventInBounds(event)) {
                	optionsButton.setHighlighted();
                } else {
                	optionsButton.removeHighlighted();
                }
            }
        }
    }

    @SuppressLint("NewApi")
	public void present(float deltaTime) {
        Graphics g = game.getGraphics();
        g.clear(android.graphics.Color.rgb(Constants.BG_RED, Constants.BG_GREEN, Constants.BG_BLUE));
        g.drawPixmap(Assets.title, titleX, titleY);
       
        playButton.draw(g);
        helpButton.draw(g);
        optionsButton.draw(g);
        
        g.drawPixmap(Assets.mainMenuHighScoreText, highScoreTextX, scoreY);
        Constants.drawScore(g, Assets.newScoreDigits, "" + Settings.highscore, utils.CANVAS_WIDTH, scoreY);
    }

    // IMPORTANT: pause() used to be called on setScreen(), which causes music to stop
    // even when not desired. Removed pause() from being called in AndroidGame.setScreen().
    public void pause() { 
    	if (menuMusic.isPlaying()) {
    		menuMusic.stop();
    	}
        Settings.save(game.getFileIO());
    }

    public void resume() {
    	if (Settings.soundEnabled) {
    		menuMusic.play();
    	}
    }

    public void dispose() {}
}