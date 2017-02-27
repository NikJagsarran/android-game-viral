package com.prosus.androidgames.viral;

import android.content.Context;
import com.prosus.androidgames.framework.Game;
import com.prosus.androidgames.framework.Graphics;

public class HelpScreen1 extends HelpScreen {
	// The decimals below are percentages that were calculated with a 720p layout.
	int antiVirusTextY;
	int spaceBetweenTextAndImage; 
	int spaceBetweenImageAndNextText;
	int antiVirusTextX;
	int antiVirusY;
	int antiVirusX;
	int virusTextY;
	int virusTextX;
	int virusY;
	int virusX;
	int dataTextY;
	int dataTextX;
	int dataY;
	int dataX;
	
	private void initialize() {
		stages.add(HelpScreenStages.AntiVirusFadeIn);
		stages.add(HelpScreenStages.VirusFadeIn);
		stages.add(HelpScreenStages.DataFadeIn);
		stages.add(HelpScreenStages.AntiVirusTextFadeIn);
		stages.add(HelpScreenStages.VirusTextFadeIn);
		stages.add(HelpScreenStages.DataTextFadeIn);
		stages.add(HelpScreenStages.FadeInComplete);
		stage = stages.remove();
		
		// The decimals below are percentages that were calculated with a 720p layout.
		antiVirusTextY = (int) (utils.CANVAS_HEIGHT * 0.039); // 3.9% down.
		spaceBetweenTextAndImage = (int) (utils.CANVAS_HEIGHT * 0.0195); 
		spaceBetweenImageAndNextText = (int) (utils.CANVAS_HEIGHT * 0.0914);
		
		antiVirusTextX = (utils.CANVAS_WIDTH - Assets.helpScreen1AntiVirusText.getWidth()) / 2;
		antiVirusY = antiVirusTextY + Assets.helpScreen1AntiVirusText.getHeight() + spaceBetweenTextAndImage;
		antiVirusX = (utils.CANVAS_WIDTH - Assets.antiVirus.getWidth()) / 2;

		virusTextY = antiVirusY + Assets.antiVirus.getHeight() + spaceBetweenImageAndNextText;
		virusTextX = (utils.CANVAS_WIDTH - Assets.helpScreen1VirusText.getWidth()) / 2;
		virusY = virusTextY + Assets.helpScreen1VirusText.getHeight() + spaceBetweenTextAndImage;
		virusX = (utils.CANVAS_WIDTH - Assets.virus.getWidth()) / 2;
		
		dataTextY = virusY + Assets.virus.getHeight() + spaceBetweenImageAndNextText;
		dataTextX = (utils.CANVAS_WIDTH - Assets.helpScreen1DataText.getWidth()) / 2;
		dataY = dataTextY + Assets.helpScreen1DataText.getHeight() + spaceBetweenTextAndImage;
		dataX = (utils.CANVAS_WIDTH - Assets.data.getWidth()) / 2;
	}

	public HelpScreen1(Game game, Context context, Utils utils) {
		super(game, context, utils);
		initialize();
	}

	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		
		// Handle clicking of Next button.
		super.checkTapping(CurrHelpScreenEnum.HelpScreen1);
	}

	@Override
	public void present(float deltaTime) {
		super.present(deltaTime);
		Graphics g = game.getGraphics();
		
		if (stage == HelpScreenStages.AntiVirusFadeIn) {
			g.drawPixmap(Assets.antiVirus, antiVirusX, antiVirusY, alpha);
			
		} else if (stage == HelpScreenStages.VirusFadeIn) {
			g.drawPixmap(Assets.antiVirus, antiVirusX, antiVirusY);
			g.drawPixmap(Assets.virus, virusX, virusY, alpha);
			
		} else if (stage == HelpScreenStages.DataFadeIn) {
			g.drawPixmap(Assets.antiVirus, antiVirusX, antiVirusY);
			g.drawPixmap(Assets.virus, virusX, virusY);
			g.drawPixmap(Assets.data, dataX, dataY, alpha);
			
		} else if (stage == HelpScreenStages.AntiVirusTextFadeIn) {
			g.drawPixmap(Assets.antiVirus, antiVirusX, antiVirusY);
			g.drawPixmap(Assets.virus, virusX, virusY);
			g.drawPixmap(Assets.data, dataX, dataY);
			g.drawPixmap(Assets.helpScreen1AntiVirusText, antiVirusTextX, antiVirusTextY, alpha);
			
		} else if (stage == HelpScreenStages.VirusTextFadeIn) {
			g.drawPixmap(Assets.antiVirus, antiVirusX, antiVirusY);
			g.drawPixmap(Assets.virus, virusX, virusY);
			g.drawPixmap(Assets.data, dataX, dataY);
			g.drawPixmap(Assets.helpScreen1AntiVirusText, antiVirusTextX, antiVirusTextY);
			g.drawPixmap(Assets.helpScreen1VirusText, virusTextX, virusTextY, alpha);
			
		} else if (stage == HelpScreenStages.DataTextFadeIn) {
			g.drawPixmap(Assets.antiVirus, antiVirusX, antiVirusY);
			g.drawPixmap(Assets.virus, virusX, virusY);
			g.drawPixmap(Assets.data, dataX, dataY);
			g.drawPixmap(Assets.helpScreen1AntiVirusText, antiVirusTextX, antiVirusTextY);
			g.drawPixmap(Assets.helpScreen1VirusText, virusTextX, virusTextY);
			g.drawPixmap(Assets.helpScreen1DataText, dataTextX, dataTextY, alpha);
			
		} else {
			g.drawPixmap(Assets.antiVirus, antiVirusX, antiVirusY);
			g.drawPixmap(Assets.virus, virusX, virusY);
			g.drawPixmap(Assets.data, dataX, dataY);
			g.drawPixmap(Assets.helpScreen1AntiVirusText, antiVirusTextX, antiVirusTextY);
			g.drawPixmap(Assets.helpScreen1VirusText, virusTextX, virusTextY);
			g.drawPixmap(Assets.helpScreen1DataText, dataTextX, dataTextY);
		}
	}
	
	// pause() and resume() handled by HelpScreen.java.

	@Override
	public void dispose() {}
}