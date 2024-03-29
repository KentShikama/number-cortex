package com.numbercortex.logic;

import java.util.ArrayList;
import java.util.Map;
import com.badlogic.gdx.Gdx;
import com.numbercortex.CortexState;
import com.numbercortex.GameSettings;
import com.numbercortex.GameSettingsLoader;
import com.numbercortex.Persistence;
import com.numbercortex.view.DragAndDropHandler;
import com.numbercortex.view.Sound;

public class SinglePlayerGameManager implements GameManager {

    private static final String TAG = SinglePlayerGameManager.class.getCanonicalName();

    private Playable screen;

    private ArrayList<Player> players;
    private CortexModel model;
    private GameSettings settings;

    private CortexState state;
    private String currentPlayer;

    private Persistence preferences;

    private int currentLevel;

    private SinglePlayerGameManager() {}
    private static class Singleton {
        private static final SinglePlayerGameManager INSTANCE = new SinglePlayerGameManager();
    }
    public static GameManager getInstance() {
        return Singleton.INSTANCE;
    }
    public static GameManager createNewGameManager(Playable screen) {
        return createNewGameManager(screen, null);
    }
    public static GameManager createNewGameManager(Playable screen, CortexState state) {
        SinglePlayerGameManager messenger = Singleton.INSTANCE;
        messenger.state = state;
        messenger.preferences = Persistence.getInstance();
        messenger.screen = screen;
        messenger.settings = buildSettings(messenger, messenger.preferences);
        messenger.players = buildPlayers(messenger, messenger.screen, messenger.settings, messenger.preferences);
        messenger.screen.setGameSettingsAndPreferences(messenger.settings, messenger.preferences);
        if (state == null) {
            messenger.model = new DefaultCortexModel(messenger, messenger.settings);
        } else {
            messenger.model = new DefaultCortexModel(messenger, messenger.settings, messenger.state);
        }
        return messenger;
    }
    private static ArrayList<Player> buildPlayers(GameManager messenger, Playable screen, GameSettings settings, Persistence preferences) {
        ArrayList<Player> players = new ArrayList<Player>();
        Player computer = new ComputerPlayer(screen, messenger, settings);
        String playerName = adjustPlayerNameIfNeeded(preferences, computer);
        Player human = new HumanPlayer(playerName, screen, messenger, settings);
        players.add(human);
        players.add(computer);
        return players;
    }
    private static String adjustPlayerNameIfNeeded(Persistence preferences, Player computer) {
        String playerName = preferences.getYourName();
        String computerName = computer.getName();
        if (computerName.equals(playerName)) {
            playerName = playerName + " ";
        }
        return playerName;
    }
    private static GameSettings buildSettings(SinglePlayerGameManager messenger, Persistence preferences) {
        int level = preferences.getCurrentLevel();
        messenger.currentLevel = level;
        return GameSettingsLoader.loadLevel(messenger.currentLevel);
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
        if (state != null) {
            updateState(state);
            if (Persistence.getInstance().isInPlay()) {
                Sound.stopOpeningBGM();
                Sound.loopGameBGMGradually();
            }
        }
    }

    @Override
    public void startNewGame() {
        Persistence persistence = Persistence.getInstance();
        persistence.setInPlay(true);
        DragAndDropHandler.getInstance().resetPlacementCount();
        manuallySetFirstPlayer();
        registerPlayersAndStartGame();
        Sound.stopOpeningBGM();
        Sound.loopGameBGMGradually();
    }
    private void manuallySetFirstPlayer() {
        switch (currentLevel) {
            case 0:
                model.setFirstPlayerPosition(0);
                break;
            case 18:
                model.setFirstPlayerPosition(0);
                break;
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
        handleTutorialMessages(state);
        handleGameEndState(state);
        updateCurrentPlayerState(state);
    }
    private void handleTutorialMessages(CortexState state) {
        if (currentLevel == 0) {
            int turnCount = BoardUtilities.getTurnNumber(state);
            String[] messages = GameMessages.getTutorialMessage(turnCount);
            if (messages != null) {
                screen.generateConfirmationDialogs(messages);
            }
        }
    }
    private void handleGameEndState(CortexState state) {
        if (preferences.isInPlay()) {
            String winnerName = state.getWinner();
            Player winner = getPlayerWithName(winnerName);
            Map<Integer, Integer> coordinateNumberMap = state.getCoordinateNumberMap();
            ArrayList<Integer> openCoordinates = BoardUtilities.getOpenCoordinates(coordinateNumberMap);
            if (playerWinsGame(winner) || tutorialEnds(winnerName, openCoordinates)) {
                Sound.playWinAndRestartOpeningBGM();
                String winningAttribute = state.getWinningAttribute();
                unlockNextLevelIfOnMaxLevel(openCoordinates, preferences.getLossesInARowAtMaxLevel(), winningAttribute);
            } else if (winnerName != null) {
                Sound.playLoseAndRestartOpeningBGM();
                preferences.setLosses(preferences.getLosses() + 1);
                String[] shareMessage = GameMessages.getLossShareMessage(preferences.getLosses());
                if (shareMessage != null) {
                    screen.generateShareDialog(shareMessage[0], shareMessage[1], shareMessage[2]);
                }
                int maxLevel = preferences.getMaxLevel();
                if (currentLevel == maxLevel) {
                    preferences.setLossesInARowAtMaxLevel(preferences.getLossesInARowAtMaxLevel() + 1); 
                }
            } else if (winnerName == null && openCoordinates.isEmpty()) {
                Sound.silenceAndRestartOpeningBGM();
                preferences.setTies(preferences.getTies() + 1);
                String[] shareMessage = GameMessages.getTieShareMessage(preferences.getTies());
                if (shareMessage != null) {
                    screen.generateShareDialog(shareMessage[0], shareMessage[1], shareMessage[2]);
                }
            }
        }
    }
    private boolean playerWinsGame(Player winner) {
        return winner != null && (winner instanceof InteractableSendable);
    }
    private boolean tutorialEnds(String winner, ArrayList<Integer> openCoordinates) {
        return currentLevel == 0 && gameIsOver(winner, openCoordinates);
    }
    private boolean gameIsOver(String winner, ArrayList<Integer> openCoordinates) {
        return winner != null || openCoordinates.isEmpty();
    }
    private void unlockNextLevelIfOnMaxLevel(ArrayList<Integer> openCoordinates, int lossesInARow, String winningAttribute) {
        int maxLevel = preferences.getMaxLevel();
        if (currentLevel == maxLevel) {
            String[] shareMessage = GameMessages.getClearShareMessage(currentLevel);
            if (shareMessage != null) {
                String facebookDescription = buildFacebookDescription(lossesInARow, winningAttribute);
                screen.generateShareDialog(shareMessage[0], shareMessage[1], facebookDescription);
            }
            if (currentLevel == 14) {
            	screen.generateRateDialog();
            }
            preferences.setLossesInARowAtMaxLevel(0);
            if (currentLevel != 18) {
                String unlockMessage = GameMessages.getUnlockMessage(currentLevel);
                if (unlockMessage != null) {
                    screen.generateConfirmationDialogs(unlockMessage);
                }
                int raisedMaxLevel = ++maxLevel;
                preferences.setMaxLevel(raisedMaxLevel);
                Gdx.app.log(TAG, "Level up " + raisedMaxLevel);
            }
        }
        if (currentLevel == 0 && openCoordinates.isEmpty()) {
            // In case the player loses and clicks play again, force player to move on to level 1
            preferences.setCurrentLevel(1);
        }
    }
    private String buildFacebookDescription(int lossesInARow, String winningAttribute) {
        String facebookDescription = "After " + lossesInARow + " losses, the level was cleared by a set of " + winningAttribute.toLowerCase() + ".";
        if (lossesInARow == 0) {
            facebookDescription = "On the first try, the level was cleared by a set of " + winningAttribute.toLowerCase() + ".";
        }
        if (currentLevel == 18) {
            facebookDescription = facebookDescription.replace("level", "last level");
        }
        return facebookDescription;
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
