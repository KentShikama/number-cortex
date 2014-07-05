package com.numbercortex;

import java.util.ArrayList;

public class GameManagerImpl implements GameManager {

	private ArrayList<Player> players = new ArrayList<Player>();
	private CortexModel model;
	private GameSettings settings;

	private CortexState state;
	private String currentPlayer;
	
	private GameManagerImpl() {}
	
	private static class Singleton {
		private static final GameManagerImpl INSTANCE = new GameManagerImpl();
	}
	
	public static GameManager getInstance() {
		return Singleton.INSTANCE;
	}

	public static GameManagerImpl createMessenger() {
		GameManagerImpl messenger = Singleton.INSTANCE;
		messenger.players.clear();
		PlayScreen screen = ScreenTracker.playScreen;
		
		if (ScreenTracker.mode == ScreenTracker.Mode.SINGLE_PLAYER) {
			int level = ScreenTracker.level;
			messenger.settings = GameSettingsLoader.loadLevel(level);
			
			Player human = new HumanPlayer("Player", screen, messenger);
			Player computer = new ComputerPlayer(screen, messenger);
			messenger.players.add(human);
			messenger.players.add(computer);	
		} else {
			messenger.settings = CortexPreferences.getInstance().getTwoPlayerGameSettings();
			
			Player playerOne = new HumanPlayer("Player 1", screen, messenger);
			Player playerTwo = new HumanPlayer("Player 2", screen, messenger);
			messenger.players.add(playerOne);
			messenger.players.add(playerTwo);
		}
		screen.setGameSettings(messenger.settings);
		messenger.model = new DefaultCortexModel(messenger, messenger.settings);
		
		return messenger;
	}
	
	public GameSettings getSettings() {
		return settings;
	}

	@Override
	public void chooseNumber(String playerName, int nextNumber) {
		model.chooseNumber(currentPlayer, nextNumber);
	}

	@Override
	public void placeNumber(String playerName, int coordinate) {
		model.placeNumber(currentPlayer, coordinate);
	}
	
	public void startNewGame() {
		ScreenTracker.isInPlay = true;
		register();
	}
	
	public void resumeGame() {
		updateState(state);
	}
	
	private void register() {
		for (Player player : players) {
			model.register(player.getName());
		}
	}

	@Override
	public void updateState(CortexState state) {
		this.state = state;
		currentPlayer = state.getCurrentPlayer();
		for (Player player : players) {
			String playerName = player.getName();
			if (playerName.equals(currentPlayer)) {
				player.updateState(state);
			}
		}
	}


}
