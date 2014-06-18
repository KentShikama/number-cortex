package com.numbercortex;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DefaultCortexModel implements CortexModel {
	
	private ArrayList<Integer> list =  new ArrayList<Integer>();
	private Map<Integer, Integer> coordinateNumberMap = new HashMap<Integer, Integer>();
	
	private static final int BOARD_SIZE = 16;
	
	private CortexScreen screen;
	private CortexPreferences preferences;
	
	public DefaultCortexModel(CortexPreferences preferences) {
		this.preferences = preferences;
	}

	public void register(CortexScreen screen) {
		this.screen = screen;
	}

	@Override
	public void startGame() {
		setInitialBoardState();
		setInitialAvailableNumbers();
		coordinateNumberMap.put(2, 2);
		CortexState state = new CortexState.CortexStateBuilder(coordinateNumberMap, list).build();
		if (screen == null) {
			System.out.println("Please register a screen");
		} else {
			screen.updateState(state);
		}
	}

	private void setInitialBoardState() {
		coordinateNumberMap.clear();
		for (int i = 0; i < BOARD_SIZE; i++) {
			coordinateNumberMap.put(i, -1);
		}
	}

	private void setInitialAvailableNumbers() {
		list.clear();
		for (int i = 1; i < 18; i++) {
			if (i == 9) {
				continue;
			}
			list.add(i);
		}
	}

	@Override
	public void placeNumber(int number, int coordinate) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void chooseNumber(int nextNumber) {
		// TODO Auto-generated method stub
		
	}

}
