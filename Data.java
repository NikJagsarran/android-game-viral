package com.prosus.androidgames.viral;

public class Data {
  public int x;
  public int y;
  public float speedX;
  public float speedY;

  private enum Direction {
    DOWN, LEFT, UP, RIGHT
  }

  private Direction direction;
  private Utils utils;

  public Data(int x, int y, Utils utils) {
    this.x = x;
    this.y = y;
    this.utils = utils;
  }

  public Data(Utils utils) {
    this.utils = utils;

    // Randomly choose a side to spawn.
    float sideChooser = (float) (Math.random() * utils.SCREEN_PERIMETER);

    if (sideChooser < utils.SCREEN_TOP_UPPER_BOUND) {
      x = (int) (Math.random() * (utils.CANVAS_WIDTH - Assets.data.getWidth()));
      y = 0 - Assets.data.getHeight();
      speedY = Constants.DATA_SPEED;
      direction = Direction.DOWN;

    } else if (sideChooser >= utils.SCREEN_TOP_UPPER_BOUND
        && sideChooser < utils.SCREEN_RIGHT_UPPER_BOUND) {
      x = utils.CANVAS_WIDTH;
      y = (int) (Math.random() * (utils.CANVAS_HEIGHT - Assets.data.getHeight()));
      speedX = -1 * Constants.DATA_SPEED;
      direction = Direction.LEFT;

    } else if (sideChooser >= utils.SCREEN_RIGHT_UPPER_BOUND
        && sideChooser < utils.SCREEN_BOTTOM_UPPER_BOUND) {
      x = (int) (Math.random() * (utils.CANVAS_WIDTH - Assets.data.getWidth()));
      y = utils.CANVAS_HEIGHT;
      speedY = -1 * Constants.DATA_SPEED;
      direction = Direction.UP;

    } else {
      x = 0 - Assets.data.getWidth();
      y = (int) (Math.random() * (utils.CANVAS_HEIGHT - Assets.data.getHeight()));
      speedX = Constants.DATA_SPEED;
      direction = Direction.RIGHT;
    }
  }

  public Data(boolean start, Utils utils) {
    this.utils = utils;

    if (start) {
      x = (int) (Math.random() * utils.CANVAS_WIDTH);
      y = (int) (Math.random() * utils.CANVAS_HEIGHT);

      float directionChooser = (float) (Math.random() * 4);
      if (directionChooser < 1) {
        direction = Direction.UP;
        speedY = -1 * Constants.DATA_SPEED;

      } else if (directionChooser >= 1 && directionChooser < 2) {
        direction = Direction.RIGHT;
        speedX = Constants.DATA_SPEED;

      } else if (directionChooser >= 2 && directionChooser < 3) {
        direction = Direction.DOWN;
        speedY = Constants.DATA_SPEED;

      } else {
        direction = Direction.LEFT;
        speedX = -1 * Constants.DATA_SPEED;
      }
    }
  }

  public void move() {
    x += speedX;
    y += speedY;
  }

  public boolean isOutOfBounds() {
    if ((direction == Direction.DOWN && y >= utils.CANVAS_HEIGHT)
        || (direction == Direction.LEFT && x <= (0 - Assets.data.getWidth()))
        || (direction == Direction.UP && y <= (0 - Assets.data.getHeight()))
        || (direction == Direction.RIGHT && x >= utils.CANVAS_WIDTH)) {
      return true;
    }
    return false;
  }

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }
}
