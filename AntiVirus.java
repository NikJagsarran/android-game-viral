package com.prosus.androidgames.viral;

public class AntiVirus {
  private int x;
  private int y;
  private float accelX;
  private float accelY;

  private boolean neutralPositiveY = Settings.neutralPositiveY;
  private float neutralAngle = (float) Math.atan(Settings.neutralZAccel / Settings.neutralYAccel);
  private Utils utils;

  public AntiVirus(int x, int y, Utils utils) {
    this.x = x;
    this.y = y;
    this.utils = utils;
  }

  public void move() {
    int tempX = x;

    // This is negative since the accelerometer's X acceleration is reversed
    // with respect to the orientation of the phone in portrait mode.
    tempX -= (4 * accelX);
    if (tempX < 0) {
      x = 0;
    } else if (tempX > utils.ANTIVIRUS_X_UPPER_BOUND) {
      x = utils.ANTIVIRUS_X_UPPER_BOUND;
    } else {
      x = tempX;
    }
    int tempY = y;
    tempY += (40 * accelY);

    if (tempY < 0) {
      y = 0;
    } else if (tempY > utils.ANTIVIRUS_Y_UPPER_BOUND) {
      y = utils.ANTIVIRUS_Y_UPPER_BOUND;
    } else {
      y = tempY;
    }
  }

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }

  public void setAccelX(float x) {
    accelX = x;
  }

  public void setAccelY(float y, float z) {
    float arctan = (float) Math.atan(z / y);

    if (Float.isNaN(arctan)) {
      arctan = 0f;
    } else {
      if (neutralPositiveY && y < 0) {
        if (z < 0) {
          arctan -= Math.PI;
        } else {
          arctan += Math.PI;
        }
      } else if (!neutralPositiveY && y > 0) {
        if (z < 0) {
          arctan += Math.PI;
        } else {
          arctan -= Math.PI;
        }
      }
    }
    accelY = (float) (neutralAngle - arctan);
  }
}