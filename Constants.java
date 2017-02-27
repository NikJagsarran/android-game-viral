package com.prosus.androidgames.viral;

import com.prosus.androidgames.framework.Graphics;
import com.prosus.androidgames.framework.Pixmap;

public final class Constants {
  public static final int BG_RED = 10;
  public static final int BG_GREEN = 10;
  public static final int BG_BLUE = 10;

  public static final int SCORE_DIGIT_WIDTH = 30;

  // Approximately 7.07 with g=10m/s^2 (Resting device shows this value of g).
  public static final float DEFAULT_Y_ACCEL = 7.07f;
  public static final float DEFAULT_Z_ACCEL = 7.07f;
  public static final float DEFAULT_ANGLE = (float) Math.atan(DEFAULT_Z_ACCEL / DEFAULT_Y_ACCEL);
  public static boolean DEFAULT_POSITIVE_Y = true;

  public static final int VIRUS_HEIGHT = Assets.virus.getHeight();
  public static final int VIRUS_WIDTH = Assets.virus.getWidth();

  public static final float VIRUS_GENERATION_TIME = 0.3f; // Seconds.
  public static final int VIRUS_INITIAL_SPEED = 6;
  public static final int VIRUS_SPEED_INCREASE_TIME = 5; // Seconds.
  public static final int VIRUS_SPEED_INCREMENT = 1; // Pixels per Second.
  public static final int VIRUS_SPEED_MAX = 100; // Pixels per Second.

  public static final float FRAME_TIME = 0.03f;

  public static final int DATA_NUM_AT_START = 20;
  public static final float DATA_GENERATION_TIME = 0.2f; // Seconds.
  public static final int DATA_SPEED = 3; // Pixels / Second.

  public static final int INVINCIBILITY_MAX = 100;

  public static void drawScore(Graphics g, Pixmap digits, String score, int right, int top) {
    drawScore(g, digits, score, right, top, 255);
  }

  public static void drawScore(Graphics g, Pixmap digits, String score, int right, int top,
      int alpha) {
    int numChars = score.length();
    int currX = right - (numChars * Constants.SCORE_DIGIT_WIDTH);
    int currDigit;

    for (int i = 0; i < numChars; i++) {
      currDigit = Integer.valueOf("" + score.charAt(i));
      g.drawPixmap(digits, currX, top, currDigit * Constants.SCORE_DIGIT_WIDTH, 0,
          Constants.SCORE_DIGIT_WIDTH, Assets.scoreDigits.getHeight(), alpha);
      currX += Constants.SCORE_DIGIT_WIDTH;
    }
  }
}
