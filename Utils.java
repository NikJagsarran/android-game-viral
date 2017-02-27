package com.prosus.androidgames.viral;

public class Utils {
  public static enum GamePlayStateFlagEnum {
    NonGamePlay, GamePlayReady, GamePlay, GamePlayPaused, GamePlayGameOver
  }

  public GamePlayStateFlagEnum curr;
  public int CANVAS_HEIGHT = 1280;
  public int CANVAS_WIDTH = 720;

  public int ANTIVIRUS_Y_UPPER_BOUND;
  public int ANTIVIRUS_X_UPPER_BOUND;

  public int SCREEN_PERIMETER = (2 * CANVAS_HEIGHT) + (2 * CANVAS_WIDTH);
  public int SCREEN_TOP_UPPER_BOUND = CANVAS_WIDTH;
  public int SCREEN_RIGHT_UPPER_BOUND = CANVAS_WIDTH + CANVAS_HEIGHT;
  public int SCREEN_BOTTOM_UPPER_BOUND = (2 * CANVAS_WIDTH) + CANVAS_HEIGHT;

  public Utils() {
    curr = GamePlayStateFlagEnum.NonGamePlay;
  }

  public Utils(int canvasHeight, int canvasWidth) {
    curr = GamePlayStateFlagEnum.NonGamePlay;
    CANVAS_HEIGHT = canvasHeight;
    CANVAS_WIDTH = canvasWidth;

    SCREEN_PERIMETER = (2 * canvasHeight) + (2 * canvasWidth);
    SCREEN_TOP_UPPER_BOUND = canvasWidth;
    SCREEN_RIGHT_UPPER_BOUND = canvasWidth + canvasHeight;
    SCREEN_BOTTOM_UPPER_BOUND = (2 * canvasWidth) + canvasHeight;
  }

  public void updateAssetDependentVars() {
    ANTIVIRUS_Y_UPPER_BOUND = CANVAS_HEIGHT - Assets.antiVirus.getHeight();
    ANTIVIRUS_X_UPPER_BOUND = CANVAS_WIDTH - Assets.antiVirus.getWidth();
  }
}
