package com.numbercortex.logic;

import java.util.ArrayList;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.numbercortex.CortexState;
import com.numbercortex.GameSettings;
import com.numbercortex.Persistence;
import com.numbercortex.view.DragAndDropHandler;
import com.numbercortex.view.ScreenTracker;
import com.numbercortex.view.Sound;

public class TwoPlayerGameManager implements GameManager {

	private static final String TAG = TwoPlayerGameManager.class.getCanonicalName();

	private Playable screen;

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
	private static ArrayList<Player> buildPlayers(GameManager messenger, Playable screen, Persistence preferences,
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
	public void setState(CortexState temporaryState) {
		this.state = temporaryState;
		updateState(state);
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
		if (state == null) {
			return;
		}
		updateState(state);
		if (Persistence.getInstance().isInPlay()) {
			Sound.loopGameBGM();
		}
	}

	@Override
	public void startNewGame() {
		Persistence persistence = Persistence.getInstance();
		persistence.setInPlay(true);
		DragAndDropHandler.getInstance().resetPlacementCount();
		registerPlayersAndStartGame();
		Sound.loopGameBGM();
	}
	private void registerPlayersAndStartGame() {
		for (Player player : players) {
			model.register(player.getName());
		}
	}

	@Override
	public void updateState(CortexState state) {
		this.state = state;
		handleEndingSounds(state);
		updateCurrentPlayerState(state);
	}
	private void handleEndingSounds(CortexState state) {
		String winner = state.getWinner();
		Map<Integer, Integer> coordinateNumberMap = state.getCoordinateNumberMap();
		ArrayList<Integer> openCoordinates = BoardUtilities.getOpenCoordinates(coordinateNumberMap);
		Persistence persistence = Persistence.getInstance();
		if (winner != null && persistence.isInPlay()) {
			Sound.stopBackgroundAndShowWin();
		} else if (isTieGame(winner, openCoordinates)) {
			Sound.stopGameBGM();
		}
	}
	private boolean isTieGame(String winner, ArrayList<Integer> openCoordinates) {
		return winner == null && openCoordinates.isEmpty();
	}
	private void updateCurrentPlayerState(CortexState state) {
		this.currentPlayer = state.getCurrentPlayer();
		Player player = getPlayerWithName(currentPlayer);
		player.updateState(state);
	}
	private Player getPlayerWithName(String playerName) {
		for (Player player : players) {
			String currentIterationPlayerName = player.getName();
			if (currentIterationPlayerName.equals(playerName)) {
				return player;
			}
		}
		return null;
	}
}
