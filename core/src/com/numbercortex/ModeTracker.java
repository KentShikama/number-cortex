package com.numbercortex;

import java.util.HashMap;
import java.util.Map;

public class ModeTracker {
	
	private ModeTracker() {}
	
	public static Mode mode;

	public static enum Mode {
		SINGLE_PLAYER, TWO_PLAYER;
	}
	private static Map<String, Mode> modeMap = new HashMap<String, Mode>();
	static {
		modeMap.put(Mode.SINGLE_PLAYER.name(), Mode.SINGLE_PLAYER);
		modeMap.put(Mode.TWO_PLAYER.name(), Mode.TWO_PLAYER);
	}
	public static Mode getMode(String mode) {
		return modeMap.get(mode);			
	}
}
