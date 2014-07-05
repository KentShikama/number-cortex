package com.numbercortex;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;

public class GameManagerImpl implements GameManager {

	private static final String TAG = GameManagerImpl.class.getCanonicalName();
	
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
	public static GameManagerImpl createNewGameManager() {
		GameManagerImpl messenger = Singleton.INSTANCE;
		PlayScreen screen = ScreenTracker.playScreen;
		messenger.players.clear();
		messenger.settings = buildSettings(messenger);
		addPlayers(messenger, screen);
		screen.setGameSettings(messenger.settings);
		messenger.model = new DefaultCortexModel(messenger, messenger.settings);
		return messenger;
	}
	private static void addPlayers(GameManagerImpl messenger, PlayScreen screen) {
		if (ScreenTracker.mode == ScreenTracker.Mode.SINGLE_PLAYER) {
			addPlayersForSinglePlayerMode(messenger, screen);	
		} else {
			addPlayersForTwoPlayerMode(messenger, screen);
		}
	}
	private static void addPlayersForSinglePlayerMode(GameManagerImpl messenger, PlayScreen screen) {
		Player human = new HumanPlayer("Player", screen, messenger);
		Player computer = new ComputerPlayer(screen, messenger);
		messenger.players.add(human);
		messenger.players.add(computer);
	}
	private static void addPlayersForTwoPlayerMode(GameManagerImpl messenger, PlayScreen screen) {
		Player playerOne = new HumanPlayer("Player 1", screen, messenger);
		Player playerTwo = new HumanPlayer("Player 2", screen, messenger);
		messenger.players.add(playerOne);
		messenger.players.add(playerTwo);
	}
	private static GameSettings buildSettings(GameManagerImpl messenger) {
		if (ScreenTracker.mode == ScreenTracker.Mode.SINGLE_PLAYER) {
			int level = ScreenTracker.level;
			return GameSettingsLoader.loadLevel(level);
		} else {
			return CortexPreferences.getInstance().getTwoPlayerGameSettings();
		}
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
		if (ScreenTracker.isInPlay) {
			Gdx.app.log(TAG, "Deleting previous game data.");
		}
		ScreenTracker.isInPlay = true;
		registerPlayersAndStartGame();
	}
	private void registerPlayersAndStartGame() {
		for (Player player : players) {
			model.register(player.getName());
		}
	}
	
	public void resumeGame() {
		if (ScreenTracker.isInPlay) {
			updateState(state);
		} else {
			Gdx.app.log(TAG, "There is no saved game to resume.");
		}
	}

	@Override
	public void updateState(CortexState state) {
		this.state = state;
		this.currentPlayer = state.getCurrentPlayer();
		updateCurrentPlayerState(state);
	}
	private void updateCurrentPlayerState(CortexState state) {
		for (Player player : players) {
			String playerName = player.getName();
			if (playerName.equals(currentPlayer)) {
				player.updateState(state);
			}
		}
	}
}
