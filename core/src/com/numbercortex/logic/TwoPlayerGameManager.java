package com.numbercortex.logic;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.numbercortex.CortexState;
import com.numbercortex.GameSettings;
import com.numbercortex.Persistence;
import com.numbercortex.view.DragAndDropHandler;
import com.numbercortex.view.PlayScreen;
import com.numbercortex.view.ScreenTracker;

public class TwoPlayerGameManager implements GameManager {

	private static final String TAG = TwoPlayerGameManager.class.getCanonicalName();

	private PlayScreen screen;

	private ArrayList<Player> players;
	private CortexModel model;
	private GameSettings settings;

	private CortexState state;
	private String currentPlayer;

	private Persistence preferences;

	private TwoPlayerGameManager() {}
	private static class Singleton {
		private static final TwoPlayerGameManager INSTANCE = new TwoPlayerGameManager();
	}
	public static GameManager getInstance() {
		return Singleton.INSTANCE;
	}
	public static GameManager createNewGameManager() {
		return createNewGameManager(null);
	}
	public static GameManager createNewGameManager(CortexState state) {
		TwoPlayerGameManager messenger = Singleton.INSTANCE;
		messenger.state = state;
		messenger.preferences = Persistence.getInstance();
		messenger.screen = ScreenTracker.playScreen;
		messenger.settings = buildSettings(messenger.preferences);
		messenger.players = buildPlayers(messenger, messenger.screen, messenger.preferences, messenger.settings);
		messenger.screen.setGameSettingsAndPreferences(messenger.settings, messenger.preferences);
		if (state == null) {
			messenger.model = new DefaultCortexModel(messenger, messenger.settings);
		} else {
			messenger.model = new DefaultCortexModel(messenger, messenger.settings, messenger.state);
		}
		return messenger;
	}
	private static ArrayList<Player> buildPlayers(GameManager messenger, PlayScreen screen, Persistence preferences,
			GameSettings settings) {
		ArrayList<Player> players = new ArrayList<Player>();
		String playerOneName = preferences.getPlayerOneName();
		String playerTwoName = preferences.getPlayerTwoName();
		Player playerOne = new HumanPlayer(playerOneName, screen, messenger, settings);
		Player playerTwo = new HumanPlayer(playerTwoName, screen, messenger, settings);
		players.add(playerOne);
		players.add(playerTwo);
		return players;
	}
	private static GameSettings buildSettings(Persistence preferences) {
		return preferences.getTwoPlayerGameSettings();
	}

	@Override
	public CortexState getState() {
		return state;
	}

	@Override
	public void chooseNumber(String playerName, int nextNumber) {
		model.chooseNumber(currentPlayer, nextNumber);
	}

	@Override
	public void placeNumber(String playerName, int coordinate) {
		model.placeNumber(currentPlayer, coordinate);
	}

	@Override
	public void resumeGame() {
		updateState(state);
	}

	@Override
	public void startNewGame() {
		Persistence persistence = Persistence.getInstance();
		if (persistence.isInPlay()) {
			Gdx.app.log(TAG, "Deleting previous game data.");
		}
		persistence.setInPlay(true);
		DragAndDropHandler.getInstance().resetPlacementCount();
		registerPlayersAndStartGame();
	}
	private void registerPlayersAndStartGame() {
		for (Player player : players) {
			model.register(player.getName());
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
