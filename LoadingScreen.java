package com.prosus.androidgames.viral;

import android.content.Context;

import com.prosus.androidgames.framework.Audio;
import com.prosus.androidgames.framework.Game;
import com.prosus.androidgames.framework.Graphics;
import com.prosus.androidgames.framework.Screen;
import com.prosus.androidgames.framework.Graphics.PixmapFormat;

public class LoadingScreen extends Screen {
  public LoadingScreen(Game game, Context context, Utils utils) {
    super(game, context, utils);
  }

  public void update(float deltaTime) {
    Graphics g = game.getGraphics();
    Assets.background = g.newPixmap("gamePlay/gamePlayBG.png", PixmapFormat.ARGB4444);
    Assets.title = g.newPixmap("mainMenuScreen/logoV2.png", PixmapFormat.ARGB4444);

    Assets.mainMenuPlayButton = g.newPixmap("mainMenuScreen/mainMenuPlayButtonV2.png",
        PixmapFormat.ARGB4444);
    Assets.mainMenuPlayButtonClicked = g.newPixmap(
        "mainMenuScreen/mainMenuPlayButtonClickedV2.png", PixmapFormat.ARGB4444);
    Assets.mainMenuHelpButton = g.newPixmap("mainMenuScreen/mainMenuHelpButtonV2.png",
        PixmapFormat.ARGB4444);
    Assets.mainMenuHelpButtonClicked = g.newPixmap(
        "mainMenuScreen/mainMenuHelpButtonClickedV2.png", PixmapFormat.ARGB4444);
    Assets.mainMenuOptionsButton = g.newPixmap("mainMenuScreen/mainMenuOptionsButtonV2.png",
        PixmapFormat.ARGB4444);
    Assets.mainMenuOptionsButtonClicked = g.newPixmap(
        "mainMenuScreen/mainMenuOptionsButtonClickedV2.png", PixmapFormat.ARGB4444);
    Assets.mainMenuHighScoreText = g.newPixmap("mainMenuScreen/mainMenuHighScoreText.png",
        PixmapFormat.ARGB4444);

    Assets.helpScreen1AntiVirusText = g.newPixmap("helpScreens/helpScreen1AntiVirusText.png",
        PixmapFormat.ARGB4444);
    Assets.helpScreen1VirusText = g.newPixmap("helpScreens/helpScreen1VirusText.png",
        PixmapFormat.ARGB4444);
    Assets.helpScreen1DataText = g.newPixmap("helpScreens/helpScreen1DataText.png",
        PixmapFormat.ARGB4444);
    Assets.helpScreen2Text = g.newPixmap("helpScreens/helpScreen2Text.png", PixmapFormat.ARGB4444);
    Assets.helpScreen3UpperText = g.newPixmap("helpScreens/helpScreen3UpperText.png",
        PixmapFormat.ARGB4444);
    Assets.helpScreen3LowerText = g.newPixmap("helpScreens/helpScreen3LowerText.png",
        PixmapFormat.ARGB4444);

    Assets.helpNextButton = g.newPixmap("helpScreens/helpNextButtonV2.png", PixmapFormat.ARGB4444);
    Assets.helpNextButtonClicked = g.newPixmap("helpScreens/helpNextButtonClickedV2.png",
        PixmapFormat.ARGB4444);

    Assets.optionsScreenTitle = g.newPixmap("optionsScreens/optionsScreenTitle.png",
        PixmapFormat.ARGB4444);
    Assets.optionsScreenNeutralButton = g.newPixmap(
        "optionsScreens/optionsScreenSetNeutralPosition.png", PixmapFormat.ARGB4444);
    Assets.optionsScreenNeutralButtonClicked = g.newPixmap(
        "optionsScreens/optionsScreenSetNeutralPositionClicked.png", PixmapFormat.ARGB4444);
    Assets.optionsScreenSoundButton = g.newPixmap("optionsScreens/optionsScreenSoundButtonV3.png",
        PixmapFormat.ARGB4444);
    Assets.optionsScreenSoundButtonClicked = g.newPixmap(
        "optionsScreens/optionsScreenSoundButtonClickedV3.png", PixmapFormat.ARGB4444);
    Assets.optionsScreenSoundButtonDisabled = g.newPixmap(
        "optionsScreens/optionsScreenSoundButtonDisabledV3.png", PixmapFormat.ARGB4444);
    Assets.optionsScreenSensitivityText = g.newPixmap(
        "optionsScreens/optionsScreenSensitivityText.png", PixmapFormat.ARGB4444);
    Assets.optionsScreenBackButton = g.newPixmap("optionsScreens/optionsScreenBackButtonV2.png",
        PixmapFormat.ARGB4444);
    Assets.optionsScreenBackButtonClicked = g.newPixmap(
        "optionsScreens/optionsScreenBackButtonClickedV2.png", PixmapFormat.ARGB4444);
    Assets.optionsScreenSaveButton = g.newPixmap("optionsScreens/optionsScreenSaveButtonV2.png",
        PixmapFormat.ARGB4444);
    Assets.optionsScreenSaveButtonClicked = g.newPixmap(
        "optionsScreens/optionsScreenSaveButtonClickedV2.png", PixmapFormat.ARGB4444);

    Assets.calibrateScreen1Text1 = g.newPixmap(
        "optionsScreens/calibrateScreen1/calibrateScreen1Text1V2.png", PixmapFormat.ARGB4444);
    Assets.calibrateScreen1Text2 = g.newPixmap(
        "optionsScreens/calibrateScreen1/calibrateScreen1Text2V2.png", PixmapFormat.ARGB4444);
    Assets.calibrateScreen1Device = g.newPixmap(
        "optionsScreens/calibrateScreen1/calibrateScreen1Device.png", PixmapFormat.ARGB4444);
    Assets.calibrateScreen1DeviceText = g.newPixmap(
        "optionsScreens/calibrateScreen1/calibrateScreen1DeviceText.png", PixmapFormat.ARGB4444);
    Assets.calibrateScreen1Head = g.newPixmap(
        "optionsScreens/calibrateScreen1/calibrateScreen1Head.png", PixmapFormat.ARGB4444);
    Assets.calibrateScreen1No = g.newPixmap(
        "optionsScreens/calibrateScreen1/calibrateScreen1NoV2.png", PixmapFormat.ARGB4444);
    Assets.calibrateScreen1NoClicked = g.newPixmap(
        "optionsScreens/calibrateScreen1/calibrateScreen1NoClickedV2.png", PixmapFormat.ARGB4444);
    Assets.calibrateScreen1Yes = g.newPixmap(
        "optionsScreens/calibrateScreen1/calibrateScreen1YesV2.png", PixmapFormat.ARGB4444);
    Assets.calibrateScreen1YesClicked = g.newPixmap(
        "optionsScreens/calibrateScreen1/calibrateScreen1YesClickedV2.png", PixmapFormat.ARGB4444);
    Assets.calibrateScreen1ResetToDefault = g.newPixmap(
        "optionsScreens/calibrateScreen1/calibrateScreen1ResetToDefaultV2.png",
        PixmapFormat.ARGB4444);
    Assets.calibrateScreen1ResetToDefaultClicked = g.newPixmap(
        "optionsScreens/calibrateScreen1/calibrateScreen1ResetToDefaultClickedV2.png",
        PixmapFormat.ARGB4444);
    Assets.calibrateScreen1ResetToDefaultDisabled = g.newPixmap(
        "optionsScreens/calibrateScreen1/calibrateScreen1ResetToDefaultDisabledV2.png",
        PixmapFormat.ARGB4444);
    Assets.calibrateScreen1ResetPopupBG = g.newPixmap(
        "optionsScreens/calibrateScreen1/resetPopup/calibrateScreen1ResetPopupBG.png",
        PixmapFormat.ARGB4444);

    Assets.calibrateScreen2Ready = g.newPixmap(
        "optionsScreens/calibrateScreen2/calibrateScreen2ReadyV2.png", PixmapFormat.ARGB4444);
    Assets.calibrateScreen2ReadyClicked = g
        .newPixmap("optionsScreens/calibrateScreen2/calibrateScreen2ReadyClickedV2.png",
            PixmapFormat.ARGB4444);
    Assets.calibrateScreen2ThreeSeconds = g
        .newPixmap("optionsScreens/calibrateScreen2/calibrateScreen2ThreeSecondsV2.png",
            PixmapFormat.ARGB4444);
    Assets.calibrateScreen2TwoSeconds = g.newPixmap(
        "optionsScreens/calibrateScreen2/calibrateScreen2TwoSecondsV2.png", PixmapFormat.ARGB4444);
    Assets.calibrateScreen2OneSecond = g.newPixmap(
        "optionsScreens/calibrateScreen2/calibrateScreen2OneSecondV2.png", PixmapFormat.ARGB4444);
    Assets.calibrateScreen2FaceUp = g.newPixmap(
        "optionsScreens/calibrateScreen2/calibrateScreen2FaceUpText.png", PixmapFormat.ARGB4444);
    Assets.calibrateScreen2FaceDown = g.newPixmap(
        "optionsScreens/calibrateScreen2/calibrateScreen2FaceDownText.png", PixmapFormat.ARGB4444);
    Assets.calibrateScreen2Done = g.newPixmap(
        "optionsScreens/calibrateScreen2/calibrateScreen2DoneV2.png", PixmapFormat.ARGB4444);
    Assets.calibrateScreen2Continue = g.newPixmap(
        "optionsScreens/calibrateScreen2/calibrateScreen2ContinueV2.png", PixmapFormat.ARGB4444);
    Assets.calibrateScreen2ContinueClicked = g.newPixmap(
        "optionsScreens/calibrateScreen2/calibrateScreen2ContinueClickedV2.png",
        PixmapFormat.ARGB4444);
    Assets.calibrateScreen2DegreeSymbol = g.newPixmap(
        "optionsScreens/calibrateScreen2/calibrateScreen2DegreeSymbol.png", PixmapFormat.ARGB4444);

    Assets.antiVirus = g.newPixmap("sprites/antiVirus.png", PixmapFormat.ARGB4444);
    Assets.antiVirusInvincible = g.newPixmap("sprites/antiVirusInvincible.png",
        PixmapFormat.ARGB4444);
    Assets.virus = g.newPixmap("sprites/virus.png", PixmapFormat.ARGB4444);

    Assets.dim = g.newPixmap("gamePlay/dim.png", PixmapFormat.ARGB4444);
    Assets.gamePlayReadyMenuButton = g.newPixmap("gamePlay/gamePlayReadyMenuButton.png",
        PixmapFormat.ARGB4444);
    Assets.gamePlayReadyMenuButtonClicked = g.newPixmap(
        "gamePlay/gamePlayReadyMenuButtonClicked.png", PixmapFormat.ARGB4444);
    Assets.countDownThree = g.newPixmap("gamePlay/countDown/three.png", PixmapFormat.ARGB4444);
    Assets.countDownTwo = g.newPixmap("gamePlay/countDown/two.png", PixmapFormat.ARGB4444);
    Assets.countDownOne = g.newPixmap("gamePlay/countDown/one.png", PixmapFormat.ARGB4444);

    Assets.pauseButton = g.newPixmap("gamePlay/pauseButton.png", PixmapFormat.ARGB4444);
    Assets.pausePopupBG = g
        .newPixmap("gamePlay/pausePopup/pausePopupBG.png", PixmapFormat.ARGB4444);
    Assets.pausePopupResume = g.newPixmap("gamePlay/pausePopup/pausePopupResumeButton.png",
        PixmapFormat.ARGB4444);
    Assets.pausePopupResumeClicked = g.newPixmap(
        "gamePlay/pausePopup/pausePopupResumeButtonClicked.png", PixmapFormat.ARGB4444);
    Assets.pausePopupMenu = g.newPixmap("gamePlay/pausePopup/pausePopupMenuButton.png",
        PixmapFormat.ARGB4444);
    Assets.pausePopupMenuClicked = g.newPixmap(
        "gamePlay/pausePopup/pausePopupMenuButtonClicked.png", PixmapFormat.ARGB4444);

    Assets.gameOverBG = g.newPixmap("gamePlay/gameOverPopup/gameOverBG.png", PixmapFormat.ARGB4444);
    Assets.gameOverReplay = g.newPixmap("gamePlay/gameOverPopup/gameOverReplayButtonV2.png",
        PixmapFormat.ARGB4444);
    Assets.gameOverReplayClicked = g.newPixmap(
        "gamePlay/gameOverPopup/gameOverReplayButtonClickedV2.png", PixmapFormat.ARGB4444);
    Assets.gameOverMenu = g.newPixmap("gamePlay/gameOverPopup/gameOverMenuButtonV2.png",
        PixmapFormat.ARGB4444);
    Assets.gameOverMenuClicked = g.newPixmap(
        "gamePlay/gameOverPopup/gameOverMenuButtonClickedV2.png", PixmapFormat.ARGB4444);

    Assets.data = g.newPixmap("sprites/data.png", PixmapFormat.ARGB4444);
    Assets.scoreDigits = g.newPixmap("sprites/scoreDigits.png", PixmapFormat.ARGB4444);
    Assets.newScoreDigits = g.newPixmap("sprites/newScoreDigits.png", PixmapFormat.ARGB4444);

    Assets.invMeter = g.newPixmap("sprites/invincibilityMeter.png", PixmapFormat.ARGB4444);
    Assets.invMeterDrained = g.newPixmap("sprites/invincibilityMeterDrained.png",
        PixmapFormat.ARGB4444);
    Assets.invBorder = g.newPixmap("sprites/invincibilityMeterBorder.png", PixmapFormat.ARGB4444);

    Audio audio = game.getAudio();
    Assets.click = audio.newSound("sounds/click.ogg");
    Assets.death = audio.newSound("sounds/death.ogg");

    Assets.menuMusic = audio.newMusic("sounds/menuMusic.ogg");
    Assets.menuMusic.setLooping(true);

    Assets.gameMusic = audio.newMusic("sounds/gameMusicV2.ogg");
    Assets.gameMusic.setLooping(true);

    Settings.load(game.getFileIO());
    utils.updateAssetDependentVars();
    game.setScreen(new MainMenuScreen(game, context, utils));
  }

  public void present(float deltaTime) {}

  public void pause() {}

  public void resume() {}

  public void dispose() {}
}
