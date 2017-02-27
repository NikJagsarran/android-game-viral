package com.prosus.androidgames.viral;

import java.util.LinkedList;
import java.util.List;
import android.content.Context;

import com.prosus.androidgames.framework.Game;
import com.prosus.androidgames.framework.Graphics;
import com.prosus.androidgames.framework.Screen;
import com.prosus.androidgames.framework.Input.TouchEvent;

public class HelpScreen extends Screen {
  Button nextButton = null;

  enum CurrHelpScreenEnum {
    HelpScreen1, HelpScreen2, HelpScreen3
  }

  enum HelpScreenStages {
    AntiVirusFadeIn, VirusFadeIn, DataFadeIn, AntiVirusTextFadeIn, VirusTextFadeIn, DataTextFadeIn,
    HelpScreen2TextFadeIn, InvMeterFadeIn, HelpScreen3UpperTextFadeIn, HelpScreen3LowerTextFadeIn,
    FadeInComplete
  }

  // Storing the order of stages in a LinkedList so we can just go to the next Node after each stage.
  LinkedList<HelpScreenStages> stages = new LinkedList<HelpScreenStages>();
  HelpScreenStages stage;

  int alpha;
  float fadeInTimePerStage = 0.8f; // Seconds.
  float fadeInTimeRemaining = fadeInTimePerStage;

  float timeBeforeFading = 0.2f; // Seconds.
  float preFadingTimeRemaining = timeBeforeFading;

  public HelpScreen(Game game, Context context, Utils utils) {
    super(game, context, utils);
    nextButton = new Button((utils.CANVAS_WIDTH - Assets.helpNextButton.getWidth()) / 2,
        (int) ((utils.CANVAS_HEIGHT * 0.97) - Assets.helpNextButton.getHeight()),
        Assets.helpNextButton, Assets.helpNextButtonClicked);
  }

  @Override
  public void update(float deltaTime) {
    if (stage != HelpScreenStages.FadeInComplete) {
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
  }

  protected void checkTapping(CurrHelpScreenEnum currHelpScreen) {
    // Handle clicking.
    List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
    int len = 0;
    if (touchEvents != null) {
      len = touchEvents.size();
    }

    for (int i = 0; i < len; i++) {
      TouchEvent event = touchEvents.get(i);
      if (event != null && event.type == TouchEvent.TOUCH_UP) {
        if (nextButton.eventInBounds(event)) {
          if (Settings.soundEnabled) {
            Assets.click.play(1);
          }

          if (currHelpScreen == CurrHelpScreenEnum.HelpScreen1) {
            game.setScreen(new HelpScreen2(game, context, utils));
          } else if (currHelpScreen == CurrHelpScreenEnum.HelpScreen2) {
            game.setScreen(new HelpScreen3(game, context, utils));
          } else {
            game.setScreen(new MainMenuScreen(game, context, utils));
          }
        } else if (stage != HelpScreenStages.FadeInComplete) {
          stage = HelpScreenStages.FadeInComplete;
        }

      } else if (event != null
          && (event.type == TouchEvent.TOUCH_DOWN || event.type == TouchEvent.TOUCH_DRAGGED)) {
        if (nextButton.eventInBounds(event)) {
          nextButton.setHighlighted();
        } else {
          nextButton.removeHighlighted();
        }
      }
    }
  }

  @Override
  public void present(float deltaTime) {
    Graphics g = game.getGraphics();
    g.clear(android.graphics.Color.rgb(Constants.BG_RED, Constants.BG_GREEN, Constants.BG_BLUE));
    nextButton.draw(g);
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
