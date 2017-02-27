package com.prosus.androidgames.viral;

import java.util.List;
import android.content.Context;

import com.prosus.androidgames.framework.Game;
import com.prosus.androidgames.framework.Graphics;
import com.prosus.androidgames.framework.Music;
import com.prosus.androidgames.framework.Screen;
import com.prosus.androidgames.framework.Input.TouchEvent;

public class OptionsScreen extends Screen {
  int titleX;
  int titleY = (int) (utils.CANVAS_HEIGHT * 0.05);
  int buttonVerticalSpacing;
  Button calibrateButton;
  Button soundButton;
  Button backButton;
  Music menuMusic = Assets.menuMusic;

  private void initialize() {
    titleX = (utils.CANVAS_WIDTH - Assets.optionsScreenTitle.getWidth()) / 2;

    buttonVerticalSpacing = ((int) (utils.CANVAS_HEIGHT * 0.95) - (Assets.optionsScreenTitle
        .getHeight()
        + Assets.optionsScreenNeutralButton.getHeight()
        + Assets.optionsScreenSoundButton.getHeight() + Assets.optionsScreenBackButton.getHeight())) / 4;

    calibrateButton = new Button(
        (utils.CANVAS_WIDTH - Assets.optionsScreenNeutralButton.getWidth()) / 2,
        Assets.optionsScreenTitle.getHeight() + (int) (utils.CANVAS_HEIGHT * 0.05)
            + buttonVerticalSpacing, Assets.optionsScreenNeutralButton,
        Assets.optionsScreenNeutralButtonClicked);

    soundButton = new Button((utils.CANVAS_WIDTH - Assets.optionsScreenSoundButton.getWidth()) / 2,
        calibrateButton.getY() + calibrateButton.getHeight() + buttonVerticalSpacing,
        Assets.optionsScreenSoundButton, Assets.optionsScreenSoundButtonClicked);

    backButton = new Button((utils.CANVAS_WIDTH - Assets.optionsScreenBackButton.getWidth()) / 2,
        soundButton.getY() + soundButton.getHeight() + buttonVerticalSpacing,
        Assets.optionsScreenBackButton, Assets.optionsScreenBackButtonClicked);

    if (Settings.soundEnabled) {
      soundButton.setRegularImage(Assets.optionsScreenSoundButton);
      soundButton.setHighlightedImage(Assets.optionsScreenSoundButtonClicked);
    } else {
      soundButton.setRegularImage(Assets.optionsScreenSoundButtonDisabled);
      soundButton.setHighlightedImage(Assets.optionsScreenSoundButtonDisabled);
    }
    soundButton.removeHighlighted();
  }

  public OptionsScreen(Game game, Context context, Utils utils) {
    super(game, context, utils);
    initialize();
  }

  @Override
  public void update(float deltaTime) {
    List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
    int len = 0;
    if (touchEvents != null) {
      len = touchEvents.size();
    }

    for (int i = 0; i < len; i++) {
      TouchEvent event = touchEvents.get(i);
      if (event != null && event.type == TouchEvent.TOUCH_UP) {

        // Calibrate button.
        if (calibrateButton.eventInBounds(event)) {
          game.setScreen(new CalibrateScreen1(game, context, utils));
          if (Settings.soundEnabled) {
            Assets.click.play(1);
          }
          return;

          // Sound button.
        } else if (soundButton.eventInBounds(event)) {
          Settings.soundEnabled = !Settings.soundEnabled;
          if (Settings.soundEnabled) {
            Assets.click.play(1);
            menuMusic.play();
            soundButton.setRegularImage(Assets.optionsScreenSoundButton);
            soundButton.setHighlightedImage(Assets.optionsScreenSoundButtonClicked);
            soundButton.removeHighlighted();
          } else if (menuMusic.isPlaying()) {
            menuMusic.stop();
            soundButton.setRegularImage(Assets.optionsScreenSoundButtonDisabled);
            soundButton.setHighlightedImage(Assets.optionsScreenSoundButtonDisabled);
            soundButton.removeHighlighted();
          }
          Settings.save(game.getFileIO());
          return;

          // Back button.
        } else if (backButton.eventInBounds(event)) {
          game.setScreen(new MainMenuScreen(game, context, utils));
          if (Settings.soundEnabled) {
            Assets.click.play(1);
          }
          return;
        }

        // Else handle highlighting.
      } else if (event != null
          && (event.type == TouchEvent.TOUCH_DOWN || event.type == TouchEvent.TOUCH_DRAGGED)) {

        // Calibrate button.
        if (calibrateButton.eventInBounds(event)) {
          calibrateButton.setHighlighted();
        } else {
          calibrateButton.removeHighlighted();
        }

        // Sound button.
        if (soundButton.eventInBounds(event)) {
          soundButton.setHighlighted();
        } else if (Settings.soundEnabled) {
          soundButton.removeHighlighted();
        }

        // Back button.
        if (backButton.eventInBounds(event)) {
          backButton.setHighlighted();
        } else {
          backButton.removeHighlighted();
        }
      }
    }
  }

  @Override
  public void present(float deltaTime) {
    Graphics g = game.getGraphics();
    g.clear(android.graphics.Color.rgb(Constants.BG_RED, Constants.BG_GREEN, Constants.BG_BLUE));

    g.drawPixmap(Assets.optionsScreenTitle, titleX, titleY);
    calibrateButton.draw(g);
    soundButton.draw(g);
    backButton.draw(g);
  }

  @Override
  public void pause() {
    if (menuMusic.isPlaying()) {
      menuMusic.stop();
    }
  }

  @Override
  public void resume() {
    if (Settings.soundEnabled) {
      menuMusic.play();
    }
  }

  @Override
  public void dispose() {}
}