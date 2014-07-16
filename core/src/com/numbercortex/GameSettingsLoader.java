package com.numbercortex;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;

public class GameSettingsLoader {

	private static ArrayList<GameSettings> array;

	public static GameSettings loadLevel(int level) {
		if (array == null) {
			GameSettingsLoader.load();
		}
		return array.get(level);
	}

	@SuppressWarnings("unchecked")
	private static void load() {
		Json json = new Json();
		FileHandle handle = Gdx.files.internal("data/levels.json");
		array = json.fromJson(ArrayList.class, handle);
	}
}
