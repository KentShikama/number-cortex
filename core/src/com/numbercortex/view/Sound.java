package com.numbercortex.view;

public class Sound {
	public static void click() {
		Assets.clickSound.play();
	}

	public static void missClick() {
		Assets.missClickSound.play();
	}

	public static void loopGameBGM() {
		Assets.backgroundMusic.play();
		Assets.backgroundMusic.setLooping(true);
	}

	public static void pauseGameBGM() {
		Assets.backgroundMusic.pause();
	}

	public static void stopGameBGM() {
		Assets.backgroundMusic.stop();
	}

	public static void stopBackgroundAndShowWin() {
		Assets.backgroundMusic.stop();
		Assets.winSound.play();
	}

	public static void stopBackgroundAndShowLose() {
		Assets.backgroundMusic.stop();
		Assets.loseSound.play(0.5f);
	}

}
