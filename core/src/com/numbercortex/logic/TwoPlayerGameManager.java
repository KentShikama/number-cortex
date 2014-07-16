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

	private ArrayList<Player> players = new ArrayList<Player>();
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
		messenger.preferences = Persistence.getInstance();
		messenger.screen = ScreenTracker.playScreen;
		messenger.state = state;
		messenger.players.clear();
		messenger.settings = buildSettings(messenger.preferences);
		addPlayers(messenger, messenger.screen, messenger.preferences);
		messenger.screen.setGameSettingsAndPreferences(messenger.settings, messenger.preferences);
		if (state == null) {
			messenger.model = new DefaultCortexModel(messenger, messenger.settings);
		} else {
			messenger.model = new DefaultCortexModel(messenger, messenger.settings, messenger.state);
		}
		return messenger;
	}
	private static void addPlayers(TwoPlayerGameManager messenger, PlayScreen screen, Persistence preferences) {
		String playerOneName = preferences.getPlayerOneName();
		String playerTwoName = preferences.getPlayerTwoName();
		Player playerOne = new HumanPlayer(playerOneName, screen, messenger);
		Player playerTwo = new HumanPlayer(playerTwoName, screen, messenger);
		messenger.players.add(playerOne);
		messenger.players.add(playerTwo);
	}
	private static GameSettings buildSettings(Persistence preferences) {
		return preferences.getTwoPlayerGameSettings();
	}

	@Override
	public GameSettings getSettings() {
		return settings;
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
		if (Persistence.getInstance().isInPlay()) {
			Gdx.app.log(TAG, "Deleting previous game data.");
		}
		Persistence.getInstance().setInPlay(true);
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
