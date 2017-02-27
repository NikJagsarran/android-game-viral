package com.prosus.androidgames.viral;

import android.content.Context;

import com.prosus.androidgames.framework.Game;
import com.prosus.androidgames.framework.Graphics;
import com.prosus.androidgames.framework.Pixmap;

public class HelpScreen3 extends HelpScreen {
  int upperTextY;
  int upperTextX;
  int lowerTextY;
  int lowerTextX;

  enum InvStages {
    VirusIn1, RestoreInv1, VirusIn2, RestoreInv2
  }

  InvStages invStage = InvStages.VirusIn1;
  Pixmap antiVirusPixmap = Assets.antiVirus;

  int centerVerticalSpace;
  int animationReqSpace;
  int invBorderX;
  int invBorderY;
  int invMeterX;
  int antiVirusX;
  int antiVirusY;

  boolean invincible;
  float invRemaining = Constants.INVINCIBILITY_MAX;
  boolean invDrained;

  int inv1StartVirusX;
  int inv1EndVirusX;
  int inv2StartVirusX;

  float virusMovementTickTime;
  int virusSpeed = -10;
  int virusStartX;
  int virusStartY = antiVirusY;
  boolean virusCreated;
  Virus virus;

  private void initialize() {
    stages.add(HelpScreenStages.AntiVirusFadeIn);
    stages.add(HelpScreenStages.InvMeterFadeIn);
    stages.add(HelpScreenStages.HelpScreen3UpperTextFadeIn);
    stages.add(HelpScreenStages.HelpScreen3LowerTextFadeIn);
    stages.add(HelpScreenStages.FadeInComplete);
    stage = stages.remove();

    upperTextY = (int) (utils.CANVAS_HEIGHT * 0.04); // 4% down.
    upperTextX = (utils.CANVAS_WIDTH - Assets.helpScreen3UpperText.getWidth()) / 2;

    lowerTextY = nextButton.getY() - (int) (utils.CANVAS_HEIGHT * 0.04)
        - Assets.helpScreen3LowerText.getHeight(); // 4% up from the next button.
    lowerTextX = (utils.CANVAS_WIDTH - Assets.helpScreen3LowerText.getWidth()) / 2;

    // Calculating this to center the animation in this space.
    centerVerticalSpace = lowerTextY - (upperTextY + Assets.helpScreen3UpperText.getHeight());
    animationReqSpace = Assets.invBorder.getHeight() + (int) (utils.CANVAS_HEIGHT * 0.02)
        + Assets.antiVirus.getHeight();

    invBorderX = (int) (utils.CANVAS_WIDTH * 0.05);
    invBorderY = upperTextY + Assets.helpScreen3UpperText.getHeight()
        + (centerVerticalSpace - animationReqSpace) / 2;

    // I guess the meter is 8 pixels thinner than the border, but the same height.
    invMeterX = invBorderX + 4;

    antiVirusX = invBorderX
        + (int) ((Assets.invBorder.getWidth() - Assets.antiVirus.getWidth()) / 2);
    antiVirusY = invBorderY + Assets.invBorder.getHeight() + (int) (utils.CANVAS_HEIGHT * 0.02);

    inv1StartVirusX = antiVirusX + Assets.antiVirus.getWidth() + (int) (utils.CANVAS_WIDTH * 0.02);
    inv1EndVirusX = antiVirusX - (int) (utils.CANVAS_WIDTH * 0.02) - Assets.virus.getWidth();
    inv2StartVirusX = antiVirusX + Assets.antiVirus.getWidth() + (int) (utils.CANVAS_WIDTH * 0.1);

    virusStartX = utils.CANVAS_WIDTH;
    virusStartY = antiVirusY;
    virus = new Virus(virusStartX, virusStartY, virusSpeed, 0, utils);
  }

  public HelpScreen3(Game game, Context context, Utils utils) {
    super(game, context, utils);
    initialize();
  }

  @Override
  public void update(float deltaTime) {
    super.update(deltaTime);
    if (stage == HelpScreenStages.FadeInComplete) {
      updateScene(deltaTime);
      updateInvincibility(deltaTime);
    }

    // Handle clicking of Next button.
    super.checkTapping(CurrHelpScreenEnum.HelpScreen3);
  }

  private void updateScene(float deltaTime) {
    if (invStage == InvStages.VirusIn1) {
      moveVirus(deltaTime);

      if (!invincible && virus.getX() <= inv1StartVirusX) {
        invincible = true;
        antiVirusPixmap = Assets.antiVirusInvincible;
      } else if (invincible && virus.getX() <= inv1EndVirusX) {
        invincible = false;
        antiVirusPixmap = Assets.antiVirus;
        invStage = InvStages.RestoreInv1;
      }

    } else if (invStage == InvStages.RestoreInv1) {
      if (!virus.isOutOfBounds()) {
        moveVirus(deltaTime);
      }

      if (invRemaining == Constants.INVINCIBILITY_MAX) {
        invStage = InvStages.VirusIn2;
        virus = new Virus(virusStartX, virusStartY, virusSpeed, 0, utils);
      }

    } else if (invStage == InvStages.VirusIn2) {
      moveVirus(deltaTime);

      if (!invincible && virus.getX() <= inv1StartVirusX) {
        invincible = true;
        antiVirusPixmap = Assets.antiVirusInvincible;
      } else if (invincible && invDrained) {
        invincible = false;
        antiVirusPixmap = Assets.antiVirus;
        invStage = InvStages.RestoreInv2;
      }

    } else if (invStage == InvStages.RestoreInv2) {
      if (!virus.isOutOfBounds()) {
        moveVirus(deltaTime);
      }

      if (invRemaining == Constants.INVINCIBILITY_MAX) {
        invStage = InvStages.VirusIn1;
        virus = new Virus(virusStartX, virusStartY, virusSpeed, 0, utils);
      }
    }
  }

  private void moveVirus(float deltaTime) {
    virusMovementTickTime += deltaTime;
    while (virusMovementTickTime > Constants.FRAME_TIME) {
      virusMovementTickTime -= Constants.FRAME_TIME;
      virus.move();
      if (virus.isOutOfBounds()) {
        virus = new Virus(virusStartX, virusStartY, 0, 0, utils);
      }
    }
  }

  // TODO Maybe put this in a common place since it's also called in World.java.
  private void updateInvincibility(float deltaTime) {
    if (invincible && !invDrained) {

      // Since we want the invincibility meter to last Constants.INVINCIBILITY_MAX seconds, and deltaTime
      // is in seconds, the amount it's reduced is given by deltaTime * Constants.INVINCIBILITY_MAX. 
      invRemaining -= deltaTime * Constants.INVINCIBILITY_MAX;

      if (invRemaining <= 0) {
        invincible = false;
        invDrained = true;
      }

      // Else, we're not invincible, so we only want to update if invincibility is not full.
    } else if (invRemaining != Constants.INVINCIBILITY_MAX) {
      if (invDrained) {
        // Refill half as fast.
        invRemaining += deltaTime * Constants.INVINCIBILITY_MAX / 2;
      } else {
        invRemaining += deltaTime * Constants.INVINCIBILITY_MAX;
      }

      if (invRemaining >= Constants.INVINCIBILITY_MAX) {
        invRemaining = Constants.INVINCIBILITY_MAX;
        invDrained = false;
      }
    } else {
      invDrained = false;
    }
  }

  @Override
  public void present(float deltaTime) {
    super.present(deltaTime);
    Graphics g = game.getGraphics();

    if (stage == HelpScreenStages.AntiVirusFadeIn) {
      g.drawPixmap(antiVirusPixmap, (int) antiVirusX, (int) antiVirusY, alpha);

    } else if (stage == HelpScreenStages.InvMeterFadeIn) {
      g.drawPixmap(antiVirusPixmap, (int) antiVirusX, (int) antiVirusY);
      g.drawPixmap(Assets.invMeter, invMeterX, invBorderY, alpha);
      g.drawPixmap(Assets.invBorder, invBorderX, invBorderY, alpha);

    } else if (stage == HelpScreenStages.HelpScreen3UpperTextFadeIn) {
      g.drawPixmap(antiVirusPixmap, (int) antiVirusX, (int) antiVirusY);
      g.drawPixmap(Assets.invMeter, invMeterX, invBorderY);
      g.drawPixmap(Assets.invBorder, invBorderX, invBorderY);
      g.drawPixmap(Assets.helpScreen3UpperText, upperTextX, upperTextY, alpha);

    } else if (stage == HelpScreenStages.HelpScreen3LowerTextFadeIn) {
      g.drawPixmap(antiVirusPixmap, (int) antiVirusX, (int) antiVirusY);
      g.drawPixmap(Assets.invMeter, invMeterX, invBorderY);
      g.drawPixmap(Assets.invBorder, invBorderX, invBorderY);
      g.drawPixmap(Assets.helpScreen3UpperText, upperTextX, upperTextY);
      g.drawPixmap(Assets.helpScreen3LowerText, lowerTextX, lowerTextY, alpha);

    } else {
      g.drawPixmap(antiVirusPixmap, (int) antiVirusX, (int) antiVirusY);
      drawInvincibilityMeter(g, invRemaining);
      g.drawPixmap(Assets.invBorder, invBorderX, invBorderY);
      g.drawPixmap(Assets.helpScreen3UpperText, upperTextX, upperTextY);
      g.drawPixmap(Assets.helpScreen3LowerText, lowerTextX, lowerTextY);
      g.drawPixmap(Assets.virus, virus.getX(), virus.getY());
    }
  }

  private void drawInvincibilityMeter(Graphics g, float remaining) {
    int width = (int) (Assets.invMeter.getWidth() * remaining / Constants.INVINCIBILITY_MAX);
    Pixmap meterToDraw = Assets.invMeter;
    if (invDrained) {
      meterToDraw = Assets.invMeterDrained;
    }
    g.drawPixmap(meterToDraw, invMeterX, invBorderY, 0, 0, width, Assets.invMeter.getHeight());
  }
}