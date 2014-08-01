package com.numbercortex.view;

import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.numbercortex.Persistence;

public class Sound {
	private static float volume;
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

	public static void loopOpeningBGM() {
		if (Persistence.getInstance().isMusic()) {
			Assets.openingBGM.play();
			Assets.openingBGM.setVolume(0.9f);
			Assets.openingBGM.setLooping(true);
		}
	}
	public static void loopOpeningBGMGradually() {
		if (Persistence.getInstance().isMusic()) {
			Assets.openingBGM.play();
			volume = 0f;
			Timer.schedule(new Task() {
				@Override
				public void run() {
					if (volume < 0.9f) {
						volume += 0.01f;
					} else {
						volume = 0.9f;
					}
					Assets.openingBGM.setVolume(volume);
				}
			}, 0f, 0.08f, 90);
			Assets.openingBGM.setLooping(true);
		}
	}
	public static void stopOpeningBGM() {
		Timer.instance().clear();
		Assets.openingBGM.stop();
	}

	public static void loopGameBGMGradually() {
		if (Persistence.getInstance().isMusic()) {
			Assets.gameBGM.play();
			volume = 0f;
			Timer.schedule(new Task() {
				@Override
				public void run() {
					if (volume < 0.9f) {
						volume += 0.01f;
					} else {
						volume = 0.9f;
					}
					Assets.gameBGM.setVolume(volume);
				}
			}, 0f, 0.08f, 90);
			Assets.gameBGM.setLooping(true);
		}
	}
	public static void pauseGameBGM() {
		Timer.instance().clear();
		Assets.gameBGM.pause();
	}
	public static void stopGameBGM() {
		Timer.instance().clear();
		Assets.gameBGM.stop();
	}

	public static void playWinAndRestartOpeningBGM() {
		stopGameBGM();
		if (Persistence.getInstance().isSound()) {
			Assets.winSound.play();
		}
		Timer.schedule(new Task() {
			@Override
			public void run() {
				Assets.winSound.stop();
				loopOpeningBGM();
			}
		}, 5f);
	}
	public static void playLoseAndRestartOpeningBGM() {
		stopGameBGM();
		if (Persistence.getInstance().isSound()) {
			Assets.loseSound.play(0.5f);
		}
		Timer.schedule(new Task() {
			@Override
			public void run() {
				Assets.loseSound.stop();
				loopOpeningBGM();
			}
		}, 5f);
	}
	public static void silenceAndRestartOpeningBGM() {
		stopGameBGM();
		Timer.schedule(new Task() {
			@Override
			public void run() {
				loopOpeningBGM();
			}
		}, 5f);
	}	
}
