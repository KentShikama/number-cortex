package com.numbercortex;

import java.util.ArrayList;
import java.util.Map;

import com.badlogic.gdx.Gdx;

public class GameManagerImpl implements GameManager {

	private static final String TAG = GameManagerImpl.class.getCanonicalName();

	private PlayScreen screen;

	private ArrayList<Player> players = new ArrayList<Player>();
	private CortexModel model;
	private GameSettings settings;

	private CortexState state;
	private String currentPlayer;

	private BrainUtilities utilities;

	private Persistence preferences;
	
	private GameManagerImpl() {}
	private static class Singleton {
		private static final GameManagerImpl INSTANCE = new GameManagerImpl();
	}
	public static GameManager getInstance() {
		return Singleton.INSTANCE;
	}
	public static GameManager createNewGameManager() {
		return createNewGameManager(null);
	}
	public static GameManager createNewGameManager(CortexState state) {
		GameManagerImpl messenger = Singleton.INSTANCE;
		messenger.preferences = Persistence.getInstance();
		messenger.screen = ScreenTracker.playScreen;
		messenger.state = state;
		messenger.players.clear();
		messenger.settings = buildSettings(messenger);
		messenger.utilities = new BrainUtilities(messenger.settings);
		addPlayers(messenger, messenger.screen);
		messenger.screen.setGameSettingsAndPreferences(messenger.settings, messenger.preferences);
		if (state == null) {
			messenger.model = new DefaultCortexModel(messenger, messenger.settings);			
		} else {
			messenger.model = new DefaultCortexModel(messenger, messenger.settings, messenger.state);
		}
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
		Persistence persistence = Persistence.getInstance();
		String playerOneName = persistence.getPlayerOneName();
		String playerTwoName = persistence.getPlayerTwoName();
		Player playerOne = new HumanPlayer(playerOneName, screen, messenger);
		Player playerTwo = new HumanPlayer(playerTwoName, screen, messenger);
		messenger.players.add(playerOne);
		messenger.players.add(playerTwo);
	}
	private static GameSettings buildSettings(GameManagerImpl messenger) {
		if (ScreenTracker.mode == ScreenTracker.Mode.SINGLE_PLAYER) {
			int level = ScreenTracker.currentLevel;
			return GameSettingsLoader.loadLevel(level);
		} else {
			return Persistence.getInstance().getTwoPlayerGameSettings();
		}
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
		if (ScreenTracker.isInPlay) {
			updateState(state);
		} else {
			Gdx.app.log(TAG, "There is no saved game to resume.");
		}
	}

	@Override
	public void startNewGame() {
		if (ScreenTracker.isInPlay) {
			Gdx.app.log(TAG, "Deleting previous game data.");
		}
		ScreenTracker.isInPlay = true;
		manuallySetFirstPlayer();
		registerPlayersAndStartGame();
	}
	private void manuallySetFirstPlayer() {
		if (ScreenTracker.mode == ScreenTracker.Mode.SINGLE_PLAYER) {
			switch (ScreenTracker.currentLevel) {
				case 0:
					model.setFirstPlayerPosition(1);
					break;
				case 18:
					model.setFirstPlayerPosition(0);
					break;
			}
		}
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
		handleTutorialMessages(state);
		handleLevelUnlockingIfApplicable(state);
		for (Player player : players) {
			String playerName = player.getName();
			if (playerName.equals(currentPlayer)) {
				player.updateState(state);
			}
		}
	}
	private void handleTutorialMessages(CortexState state) {
		if (ScreenTracker.currentLevel == 0 && ScreenTracker.mode == ScreenTracker.Mode.SINGLE_PLAYER) {
			int numberOfRows = settings.getNumberOfRows();
			int turnCount = utilities.getTurnNumber(state, numberOfRows);
			String[] messages = getTutorialMessage(turnCount);
			if (messages != null) {
				screen.showConfirmationDialog(messages);
			}
		}
	}
	private String[] getTutorialMessage(int turnCount) {
		if (turnCount == 1) {
			return new String[] {
					"Welcome! Number Cortex is a game where you take turns with your opponent placing numbers (1 ~ 17, excluding 9) on the board.",
					"Go ahead and drag and drop the chosen number onto the board." };
		} else if (turnCount == 2) {
			return new String[] { "Number Cortex is unique because you get to choose which number your opponent will play next. Double tap your opponents next number from the scroller below." };
		} else if (turnCount == 5) {
			return new String[] { "Your objective is to be the first one to make a 3-in-a-row (horizontally, vertically, or diagonally) of all evens, all odds, all single digits, or all double digits. Good luck!" };
		}
		return null;
	}
	private void handleLevelUnlockingIfApplicable(CortexState state) {
		String winner = state.getWinner();
		Map<Integer, Integer> coordinateNumberMap = state.getCoordinateNumberMap();
		ArrayList<Integer> openCoordinates = BrainUtilities.getOpenCoordinates(coordinateNumberMap);
		if (playerWinsSinglePlayerGame(winner) || tutorialEnds(winner, openCoordinates)) {
			unlockNextLevelIfOnMaxLevel();
		}
	}
	private boolean playerWinsSinglePlayerGame(String winner) {
		return winner != null && ScreenTracker.mode == ScreenTracker.Mode.SINGLE_PLAYER && winner.equals("Player");
	}
	private boolean tutorialEnds(String winner, ArrayList<Integer> openCoordinates) {
		return ScreenTracker.currentLevel == 0 && ScreenTracker.mode == ScreenTracker.Mode.SINGLE_PLAYER
				&& (winner != null || openCoordinates.isEmpty());
	}
	private void unlockNextLevelIfOnMaxLevel() {
		Persistence preferences = Persistence.getInstance();
		int currentLevel = ScreenTracker.currentLevel;
		int maxLevel = preferences.getMaxLevel();
		if (currentLevel == maxLevel) {
			String message = getUnlockMessage(currentLevel);
			if (message != null) {
				screen.showConfirmationDialog(5.1f, message); // Delay depends on winning animation
			}
			int raisedMaxLevel = ++maxLevel;
			preferences.setMaxLevel(raisedMaxLevel);
			preferences.save();
			Gdx.app.log(TAG, "Level up " + raisedMaxLevel);
		} else if (currentLevel == 0) {
			ScreenTracker.currentLevel = 1;
		}
	}
	private String getUnlockMessage(int currentLevel) {
		switch (currentLevel) {
			case 0:
				return "Adjustments unlocked! You can now adjust the placement of a number up to two times. Simply drag the placed number to a different square.";
			case 1:
				return "You can single tap a number on the scroller to fade it out. This can be a good visual reminder of the numbers you do not want to choose.";
			case 3:
				return "New rule: Primes and Composites! Adding to the current ruleset, you can make a 3-in-a-row of all primes (1*, 3, 5, 7, 9, 11, 13, 15, 17) or all composites (2, 4, 6, 8, 10, 12, 14, 16).";
			case 6:
				return "New rule: Middles and Extremes! Adding to the current ruleset, you can make a 3-in-a-row of all middles (5 ~ 12) or all extremes (1 ~ 4 and 13 ~ 17).";
			case 9:
				return "Board size increase! You must now make a 4-in-a-row of winning numbers in order to win.";
			case 13:
				return "Four squares unlocked! You may now make a 4-in-a-row by placing a set of winning numbers in a 2 x 2 square.";
		}
		return null;
	}
}
