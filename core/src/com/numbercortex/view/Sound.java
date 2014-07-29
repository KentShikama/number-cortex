package com.numbercortex.view;

import com.numbercortex.Persistence;

public class Sound {
	public static void click() {
		if (Persistence.getInstance().isSound()) {
			Assets.clickSound.play();
		}
	}

	public static void missClick() {
		if (Persistence.getInstance().isSound()) {
			Assets.missClickSound.play();
		}
	}

	public static void loopGameBGM() {
		if (Persistence.getInstance().isMusic()) {
			Assets.backgroundMusic.play();
			Assets.backgroundMusic.setVolume(0.9f);
			Assets.backgroundMusic.setLooping(true);
		}
	}

	public static void pauseGameBGM() {
		Assets.backgroundMusic.pause();
	}

	public static void stopGameBGM() {
		Assets.backgroundMusic.stop();
	}

	public static void stopBackgroundAndShowWin() {
		Assets.backgroundMusic.stop();
		if (Persistence.getInstance().isSound()) {
			Assets.winSound.play();
		}
	}

	public static void stopBackgroundAndShowLose() {
		Assets.backgroundMusic.stop();
		if (Persistence.getInstance().isSound()) {
			Assets.loseSound.play(0.5f);
		}
	}

}
