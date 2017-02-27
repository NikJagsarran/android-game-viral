package com.prosus.androidgames.viral;

import java.util.List;
import java.util.Stack;
import android.content.Context;
import android.os.PowerManager;

import com.prosus.androidgames.framework.Game;
import com.prosus.androidgames.framework.Graphics;
import com.prosus.androidgames.framework.Input.TouchEvent;
import com.prosus.androidgames.framework.Music;
import com.prosus.androidgames.framework.Pixmap;
import com.prosus.androidgames.framework.Screen;
import com.prosus.androidgames.viral.Utils.GamePlayStateFlagEnum;

public class GameScreen extends Screen {
  enum GameState {
    Ready, CountDown, Running, GameOver, Dying
  }

  int gamePlayBGX;
  GameState state = GameState.Ready;
  boolean paused;
  World world;

  boolean gameRunning;
  float countDownTotalTime = 3f;
  float countDownTimePassed;
  Pixmap countDownText = Assets.countDownThree;
  int countDownTextX;
  int countDownTextY;

  Button readyButton;
  Button readyMenuButton;

  // 20 pixels from each edge.
  Button pauseButton = new Button(20, 20, Assets.pauseButton);

  int invBorderTop = pauseButton.getY() + (pauseButton.getHeight() - Assets.invBorder.getHeight())
      / 2;
  int invBorderLeft = 2 * pauseButton.getX() + pauseButton.getWidth();
  int invMeterLeft = invBorderLeft + 4;

  int pausePopupBGX;
  int pausePopupBGY;

  int pausePopupButtonsHorizSpacing = (Assets.pausePopupBG.getWidth() - (Assets.pausePopupResume
      .getWidth() + Assets.pausePopupMenu.getWidth())) / 3;
  int pausePopupButtonsY;

  Button pausePopupResumeButton;
  Button pausePopupMenuButton;

  int gameOverBGY;
  int gameOverBGX;
  int gameOverCurrScoreY;
  int gameOverBestScoreY;
  int gameOverScoresRight;

  int gameOverButtonsY;
  Button gameOverReplayButton;
  Button gameOverMenuButton;

  // On death, want to set wakeLock back to PARTIAL_WAKE_LOCK for the gameOverPopup.
  // Changing from FULL_WAKE_LOCK to PARTIAL doesn't reset the timer of lack of user
  // input, so the device may immediately sleep when gameOverPopup pops up. Delaying
  // the wakeLock change by fifteen seconds to prevent this immediate sleep.
  float gameOverWakeLockDelay = 15;
  boolean gameOverWakeLockChanged;

  boolean deathSoundPlayed;
  float fadeOutTime = 1.5f; // Seconds; Slightly longer than the death sound.
  float fadeOutTimeRemaining = fadeOutTime;
  static Virus collidedVirus;
  int alpha = 255;

  Music gameMusic = Assets.gameMusic;

  private void initialize() {
    utils.curr = GamePlayStateFlagEnum.GamePlayReady;
    world = new World(utils);

    gamePlayBGX = (utils.CANVAS_WIDTH - Assets.background.getWidth()) / 2;

    countDownTextX = (utils.CANVAS_WIDTH - countDownText.getWidth()) / 2;
    countDownTextY = (utils.CANVAS_HEIGHT - countDownText.getHeight()) / 2;

    readyButton = new Button((utils.CANVAS_WIDTH - Assets.calibrateScreen2Ready.getWidth()) / 2,
        (utils.CANVAS_HEIGHT - Assets.calibrateScreen2Ready.getHeight()) / 2,
        Assets.calibrateScreen2Ready, Assets.calibrateScreen2ReadyClicked);
    readyMenuButton = new Button(
        (utils.CANVAS_WIDTH - Assets.gamePlayReadyMenuButton.getWidth()) / 2,
        (int) (utils.CANVAS_HEIGHT * 0.97) - Assets.gamePlayReadyMenuButton.getHeight(),
        Assets.gamePlayReadyMenuButton, Assets.gamePlayReadyMenuButtonClicked);

    // Pause calculations.
    pausePopupBGX = (utils.CANVAS_WIDTH - Assets.pausePopupBG.getWidth()) / 2;
    pausePopupBGY = (utils.CANVAS_HEIGHT - Assets.pausePopupBG.getHeight()) / 2;

    pausePopupButtonsY = (int) (pausePopupBGY + 0.6 * Assets.pausePopupBG.getHeight());

    pausePopupResumeButton = new Button(pausePopupBGX + pausePopupButtonsHorizSpacing,
        pausePopupButtonsY, Assets.pausePopupResume, Assets.pausePopupResumeClicked);
    pausePopupMenuButton = new Button(pausePopupBGX + Assets.pausePopupResume.getWidth() + 2
        * (pausePopupButtonsHorizSpacing), pausePopupButtonsY, Assets.pausePopupMenu,
        Assets.pausePopupMenuClicked);

    // Game over calculations.
    gameOverBGY = utils.CANVAS_HEIGHT * 3 / 10;
    gameOverBGX = (utils.CANVAS_WIDTH - Assets.gameOverBG.getWidth()) / 2;

    // Second terms are due to alignment inside of GameOverBG.
    // The hard-coded 254 and 310 were determined manually in Photoshop.
    gameOverCurrScoreY = gameOverBGY + 254;
    gameOverBestScoreY = gameOverBGY + 310;
    gameOverScoresRight = gameOverBGX + (int) (Assets.gameOverBG.getWidth() * 0.95);

    // Half way between the bottom of 'Best:' and the bottom of gameOverBG.
    gameOverButtonsY = (gameOverBGY + Assets.gameOverBG.getHeight() + gameOverBestScoreY
        + Assets.scoreDigits.getHeight() - Assets.gameOverReplay.getHeight()) / 2;

    gameOverReplayButton = new Button(gameOverBGX + (int) (0.12 * Assets.gameOverBG.getWidth()),
        gameOverButtonsY, Assets.gameOverReplay, Assets.gameOverReplayClicked);
    gameOverMenuButton = new Button(gameOverBGX + (int) (0.59 * Assets.gameOverBG.getWidth()),
        gameOverButtonsY, Assets.gameOverMenu, Assets.gameOverMenuClicked);
  }

  public GameScreen(Game game, Context context, Utils utils) {
    super(game, context, utils);
    initialize();
  }

  /****************************************************************************************************************
   ********************************************** UPDATE METHODS **************************************************
   ********************* Methods to update objects and sounds etc. based on input and/or time *********************
   ****************************************************************************************************************/
  @Override
  public void update(float deltaTime) {
    List<TouchEvent> touchEvents = game.getInput().getTouchEvents();

    if (state == GameState.Ready) {
      updateReady(touchEvents);
    } else if (state == GameState.CountDown || state == GameState.Running) {
      updateCountDownAndReady(deltaTime, touchEvents);
    } else if (state == GameState.Dying) {
      updateDying(deltaTime);
    } else if (state == GameState.GameOver) {
      updateGameOver(touchEvents, deltaTime);
    }
  }

  private void updateCountDownAndReady(float deltaTime, List<TouchEvent> touchEvents) {
    if (utils.curr == GamePlayStateFlagEnum.GamePlayPaused && !paused) {
      countDownTimePassed = 0;
      paused = true;
      if (gameMusic.isPlaying()) {
        gameMusic.stop();
      }

      // When the back button is clicked while paused, onBackPressed()
      // updates utils to GamePlayStateFlagEnum.GamePlay.
    } else if (utils.curr == GamePlayStateFlagEnum.GamePlay && paused) {
      if (state == GameState.CountDown) {
        countDownTimePassed = 0;
      } else {
        state = GameState.CountDown;
      }
      paused = false;
    }

    // Also allowing the game to be paused during the countdown.
    // This just shows the paused popup and restarts the countdown.
    if (!paused) {
      if (state == GameState.CountDown) {
        updateCountDown(deltaTime);
      } else {
        updateRunning(touchEvents, deltaTime);
      }
    } else {
      updatePaused(touchEvents);
    }
  }

  private void updateCountDown(float deltaTime) {
    if (utils.curr == GamePlayStateFlagEnum.GamePlayPaused) {
      paused = true;
      countDownTimePassed = 0;
      countDownText = Assets.countDownThree;
    } else {
      if (countDownTimePassed < countDownTotalTime) {
        countDownTimePassed += deltaTime;

        // Update the text to display based on the amount of time remaining.
        if (countDownTimePassed <= 1) {
          countDownText = Assets.countDownThree;
        } else if (countDownTimePassed > 1 && countDownTimePassed <= 2) {
          countDownText = Assets.countDownTwo;
        } else {
          countDownText = Assets.countDownOne;
        }
        countDownTextX = (utils.CANVAS_WIDTH - countDownText.getWidth()) / 2;
      } else {
        if (Settings.soundEnabled && !gameMusic.isPlaying()) {
          gameMusic.play();
        }
        gameRunning = true;
        countDownTimePassed = 0;
        state = GameState.Running;
        game.setWakeLock(PowerManager.FULL_WAKE_LOCK);
      }
    }
  }

  private void updateReady(List<TouchEvent> touchEvents) {
    int len = 0;
    if (touchEvents != null) {
      len = touchEvents.size();
    }
    for (int i = 0; i < len; i++) {
      TouchEvent event = touchEvents.get(i);
      if (event != null && event.type == TouchEvent.TOUCH_UP) {
        if (readyButton.eventInBounds(event)) {
          // Ready Button.
          if (Settings.soundEnabled) {
            Assets.click.play(1);
          }
          utils.curr = GamePlayStateFlagEnum.GamePlay;
          state = GameState.CountDown;
          readyButton.removeHighlighted();
          return;

        } else if (readyMenuButton.eventInBounds(event)) {
          // Ready Menu Button.
          if (Settings.soundEnabled) {
            Assets.click.play(1);
          }
          utils.curr = GamePlayStateFlagEnum.NonGamePlay;
          game.setScreen(new MainMenuScreen(game, context, utils));
          return;
        }

        // Else handle highlighting.
      } else if (event != null
          && (event.type == TouchEvent.TOUCH_DOWN || event.type == TouchEvent.TOUCH_DRAGGED)) {
        if (readyButton.eventInBounds(event)) {
          readyButton.setHighlighted();
        } else {
          readyButton.removeHighlighted();
        }

        if (readyMenuButton.eventInBounds(event)) {
          readyMenuButton.setHighlighted();
        } else {
          readyMenuButton.removeHighlighted();
        }
      }
    }
  }

  private void updateRunning(List<TouchEvent> touchEvents, float deltaTime) {
    if (touchEvents != null) {
      int len = 0;
      if (touchEvents != null) {
        len = touchEvents.size();
      }

      for (int i = 0; i < len; i++) {
        TouchEvent event = touchEvents.get(i);
        if (event != null && event.type == TouchEvent.TOUCH_UP) {
          // Go visible.
          world.invincible = false;

          // Pause Button.
          if (pauseButton.eventInBounds(event)) {
            if (Settings.soundEnabled) {
              Assets.click.play(1);
              if (gameMusic.isPlaying()) {
                gameMusic.stop();
              }
            }
            paused = true;
            utils.curr = GamePlayStateFlagEnum.GamePlayPaused;
            game.setWakeLock(PowerManager.PARTIAL_WAKE_LOCK);
          }

          // Go invincible if TOUCH_DOWN but not on the pause button.
        } else if (event != null && event.type == TouchEvent.TOUCH_DOWN && !world.invDrained
            && !pauseButton.eventInBounds(event)) {
          world.invincible = true;
        }
      }
    }

    float accelX = game.getInput().getAccelX();
    float accelY = game.getInput().getAccelY();
    float accelZ = game.getInput().getAccelZ();
    world.antiVirus.setAccelX(accelX);
    world.antiVirus.setAccelY(accelY, accelZ);
    world.update(deltaTime);

    if (world.gameOver) {
      if (Settings.soundEnabled) {
        Assets.death.play(1);
        if (gameMusic.isPlaying()) {
          gameMusic.stop();
        }
      }
      state = GameState.Dying;
      if (world.newScore) {
        Settings.save(game.getFileIO());
      }
    }
  }

  private void updatePaused(List<TouchEvent> touchEvents) {
    if (touchEvents != null) {
      int len = 0;
      if (touchEvents != null) {
        len = touchEvents.size();
      }

      for (int i = 0; i < len; i++) {
        TouchEvent event = touchEvents.get(i);
        if (event != null && event.type == TouchEvent.TOUCH_UP) {

          // Resume Button.
          if (pausePopupResumeButton.eventInBounds(event)) {
            if (Settings.soundEnabled) {
              Assets.click.play(1);
            }
            utils.curr = GamePlayStateFlagEnum.GamePlay;
            state = GameState.CountDown;
            paused = false;
            pausePopupResumeButton.removeHighlighted();
            return;

            // Menu Button.
          } else if (pausePopupMenuButton.eventInBounds(event)) {
            if (Settings.soundEnabled) {
              Assets.click.play(1);
              if (gameMusic.isPlaying()) {
                gameMusic.stop();
              }
            }
            utils.curr = GamePlayStateFlagEnum.NonGamePlay;
            game.setScreen(new MainMenuScreen(game, context, utils));
            pausePopupMenuButton.removeHighlighted();
            return;
          }

          // Else handle highlighting.
        } else if (event != null
            && (event.type == TouchEvent.TOUCH_DOWN || event.type == TouchEvent.TOUCH_DRAGGED)) {

          // Resume Button.
          if (pausePopupResumeButton.eventInBounds(event)) {
            pausePopupResumeButton.setHighlighted();
          } else {
            pausePopupResumeButton.removeHighlighted();
          }

          // Menu Button.
          if (pausePopupMenuButton.eventInBounds(event)) {
            pausePopupMenuButton.setHighlighted();
          } else {
            pausePopupMenuButton.removeHighlighted();
          }
        }
      }
    }
  }

  // Updates fadeOutTimeRemaining and alpha to slowly fade out everything in fadeOutTime seconds.
  private void updateDying(float deltaTime) {

    // The next FadeOutTimeRemaining has to be calculated
    // first so we can proceed only if it's > 0.
    float nextFadeOutTimeRemaining = fadeOutTimeRemaining - deltaTime;

    if (nextFadeOutTimeRemaining > 0) {
      alpha -= (int) ((deltaTime / fadeOutTime) * 255f);
      fadeOutTimeRemaining = nextFadeOutTimeRemaining;
    } else {
      state = GameState.GameOver;
      utils.curr = GamePlayStateFlagEnum.GamePlayGameOver;
    }
  }

  private void updateGameOver(List<TouchEvent> touchEvents, float deltaTime) {
    if (!gameOverWakeLockChanged) {
      if (gameOverWakeLockDelay > 0) {
        gameOverWakeLockDelay -= deltaTime;
      } else {
        game.setWakeLock(PowerManager.PARTIAL_WAKE_LOCK);
        gameOverWakeLockChanged = true;
      }
    }
    int len = 0;
    if (touchEvents != null) {
      len = touchEvents.size();
    }

    for (int i = 0; i < len; i++) {
      TouchEvent event = touchEvents.get(i);
      if (event != null && event.type == TouchEvent.TOUCH_UP) {

        // Replay Button.
        if (gameOverReplayButton.eventInBounds(event)) {
          if (Settings.soundEnabled) {
            Assets.click.play(1);
          }
          game.setScreen(new GameScreen(game, context, utils));
          return;

          // Menu Button.
        } else if (gameOverMenuButton.eventInBounds(event)) {
          if (Settings.soundEnabled) {
            Assets.click.play(1);
          }
          utils.curr = GamePlayStateFlagEnum.NonGamePlay;
          game.setScreen(new MainMenuScreen(game, context, utils));
          return;
        }

        // Else handle highlighting.
      } else if (event != null
          && (event.type == TouchEvent.TOUCH_DOWN || event.type == TouchEvent.TOUCH_DRAGGED)) {

        // Replay Button.
        if (gameOverReplayButton.eventInBounds(event)) {
          gameOverReplayButton.setHighlighted();
        } else {
          gameOverReplayButton.removeHighlighted();
        }

        // Menu Button.
        if (gameOverMenuButton.eventInBounds(event)) {
          gameOverMenuButton.setHighlighted();
        } else {
          gameOverMenuButton.removeHighlighted();
        }
      }
    }
  }

  /****************************************************************************************************************
   ******************************************** END UPDATE METHODS ************************************************
   ****************************************************************************************************************/

  /****************************************************************************************************************
   ********************************************** PRESENT METHODS *************************************************
   **************************************** Methods to draw everything ********************************************
   ****************************************************************************************************************/
  @Override
  public void present(float deltaTime) {
    Graphics g = game.getGraphics();
    g.clear(android.graphics.Color.rgb(Constants.BG_RED, Constants.BG_GREEN, Constants.BG_BLUE));
    g.drawPixmap(Assets.background, gamePlayBGX, 0);

    if (state == GameState.Ready) {
      g.drawPixmap(Assets.dim, -10, -10);
      readyButton.draw(g);
      readyMenuButton.draw(g);
    } else if (state == GameState.CountDown) {
      if (gameRunning) {
        drawWorld(world, g);
      }

      // Only draw the dimming mask if it's not paused so we don't get double dimming.
      if (!paused) {
        g.drawPixmap(Assets.dim, -10, -10);
      }
      g.drawPixmap(countDownText, countDownTextX, countDownTextY);
    } else if (state == GameState.Running) {
      drawWorld(world, g);
    } else if (state == GameState.Dying) {
      drawWorldDying(world, g);
    } else if (state == GameState.GameOver) {
      drawWorldGameOver(world, g);
      drawGameOverUI(g);
    }

    if (paused) {
      drawPausedUI(g);
    }
  }

  private void drawWorld(World world, Graphics g) {
    AntiVirus antiVirus = world.antiVirus;
    pauseButton.draw(g);
    drawData(g);
    drawViruses(g);

    Pixmap antiVirusToDraw = Assets.antiVirus;
    if (world.invincible) {
      antiVirusToDraw = Assets.antiVirusInvincible;
    }
    g.drawPixmap(antiVirusToDraw, antiVirus.getX(), antiVirus.getY());
    drawScoreAndMeter(g);
  }

  private void drawWorldDying(World world, Graphics g) {
    AntiVirus antiVirus = world.antiVirus;
    g.drawPixmap(Assets.antiVirus, antiVirus.getX(), antiVirus.getY());
    g.drawPixmap(Assets.virus, collidedVirus.getX(), collidedVirus.getY());

    drawData(g, alpha);
    drawViruses(g, alpha);
    drawScoreAndMeter(g, alpha);
    pauseButton.draw(g, alpha);
  }

  private void drawWorldGameOver(World world, Graphics g) {
    AntiVirus antiVirus = world.antiVirus;
    g.drawPixmap(Assets.antiVirus, antiVirus.getX(), antiVirus.getY());
    g.drawPixmap(Assets.virus, collidedVirus.getX(), collidedVirus.getY());
  }

  private void drawPausedUI(Graphics g) {
    g.drawPixmap(Assets.dim, -10, -10);
    g.drawPixmap(Assets.pausePopupBG, pausePopupBGX, pausePopupBGY);
    pausePopupResumeButton.draw(g);
    pausePopupMenuButton.draw(g);
  }

  /**************************************************************************
   ***************** METHODS TO DRAW DATA AND VIRUSES ***********************
   **************************************************************************/
  private void drawData(Graphics g) {
    drawData(g, 255);
  }

  private void drawData(Graphics g, int alpha) {
    Stack<Data> data = world.data;
    Stack<Data> tempData = new Stack<Data>();
    Data currData;

    while (!data.empty()) {
      currData = data.pop();
      g.drawPixmap(Assets.data, currData.getX(), currData.getY(), alpha);
      tempData.push(currData);
    }
    world.data = tempData;
  }

  private void drawViruses(Graphics g) {
    drawViruses(g, 255);
  }

  // Goes through the Stack of AntiViruses, and draws them.
  private void drawViruses(Graphics g, int alpha) {
    Stack<Virus> viruses = world.viruses;
    Stack<Virus> tempViruses = new Stack<Virus>();
    Virus currVirus;

    while (!viruses.empty()) {
      currVirus = viruses.pop();
      g.drawPixmap(Assets.virus, currVirus.getX(), currVirus.getY(), alpha);
      tempViruses.push(currVirus);
    }
    world.viruses = tempViruses;
  }

  /**************************************************************************
   **************** END METHODS TO DRAW DATA AND VIRUSES ********************
   **************************************************************************/

  /**************************************************************************
   ******* METHODS TO DRAW UPPER RIGHT SCORE AND INVINCIBILITY METER ********
   **************************************************************************/
  private void drawScoreAndMeter(Graphics g) {
    drawScoreAndMeter(g, 255);
  }

  private void drawScoreAndMeter(Graphics g, int alpha) {
    Constants.drawScore(g, Assets.scoreDigits, "" + world.score, utils.CANVAS_WIDTH, 0, alpha);
    drawInvincibilityMeter(g, world.invRemaining, alpha);
    g.drawPixmap(Assets.invBorder, invBorderLeft, invBorderTop, alpha);
  }

  private void drawInvincibilityMeter(Graphics g, float remaining, int alpha) {
    int width = (int) (Assets.invMeter.getWidth() * remaining / Constants.INVINCIBILITY_MAX);
    Pixmap meterToDraw = Assets.invMeter;
    if (world.invDrained) {
      meterToDraw = Assets.invMeterDrained;
    }
    g.drawPixmap(meterToDraw, invMeterLeft, invBorderTop, 0, 0, width, Assets.invMeter.getHeight(),
        alpha);
  }

  /**************************************************************************
   ***** END METHODS TO DRAW UPPER RIGHT SCORE AND INVINCIBILITY METER ******
   **************************************************************************/

  private void drawGameOverUI(Graphics g) {
    g.drawPixmap(Assets.gameOverBG, gameOverBGX, gameOverBGY);
    gameOverReplayButton.draw(g);
    gameOverMenuButton.draw(g);

    // Draw the current score and best score in the game over popup.
    Pixmap digits;
    if (world.newScore) {
      digits = Assets.newScoreDigits;
    } else {
      digits = Assets.scoreDigits;
    }

    Constants.drawScore(g, digits, "" + world.score, gameOverScoresRight, gameOverCurrScoreY);
    Constants
        .drawScore(g, digits, "" + Settings.highscore, gameOverScoresRight, gameOverBestScoreY);
  }

  /****************************************************************************************************************
   ******************************************** END PRESENT METHODS ***********************************************
   ****************************************************************************************************************/

  @Override
  public void pause() {
    if (Assets.gameMusic.isPlaying()) {
      Assets.gameMusic.stop();
    }
    if (state == GameState.Running || state == GameState.CountDown) {
      utils.curr = GamePlayStateFlagEnum.GamePlayPaused;
      paused = true;
    }
  }

  // resume() is called even when the Replay button is hit.
  @Override
  public void resume() {}

  @Override
  public void dispose() {}
}
