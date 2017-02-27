package com.prosus.androidgames.viral;

import com.prosus.androidgames.framework.Screen;
import com.prosus.androidgames.framework.impl.AndroidGame;

public class ViralGame extends AndroidGame {
	public Screen getStartScreen(Utils utils) {
		return new LoadingScreen(this, this.getApplicationContext(), utils);
	}
}
