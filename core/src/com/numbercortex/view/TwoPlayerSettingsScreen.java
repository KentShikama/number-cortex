package com.numbercortex.view;

import libgdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.SnapshotArray;
import com.numbercortex.CortexState;
import com.numbercortex.ModeTracker;
import com.numbercortex.Persistence;
import com.numbercortex.logic.GameManager;
import com.numbercortex.logic.TwoPlayerGameManager;
import com.numbercortex.view.TransitionScreen.Direction;

class TwoPlayerSettingsScreen extends SettingsScreen {

    static final String TAG = "Two Player Settings Screen";

    private TextField playerOneNameField;
    private TextField playerTwoNameField;

    private class TwoChoiceRadioSettingGroup extends SettingGroup {
        private TwoChoiceRadioSettingGroup(Label choiceOneLabel, Label choiceTwoLabel, ImageButton choiceOneCheckbox, ImageButton choiceTwoCheckbox, final GroupState groupState) {
            super(groupState);
            this.addActor(choiceOneLabel);
            this.addActor(choiceTwoLabel);
            this.addActor(choiceOneCheckbox);
            this.addActor(choiceTwoCheckbox);
            ButtonGroup<Button> group = new ButtonGroup<Button>(choiceOneCheckbox, choiceTwoCheckbox);
            for (final Button button : group.getButtons()) {
                button.addListener(new ClickListenerWithSound() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        if (groupState == GroupState.CLICKABLE) {
                            button.setChecked(!button.isChecked());
                        }
                    }
                });
            }
        }
        @Override
        public void draw(Batch batch, float parentAlpha) {
            SnapshotArray<Actor> children = this.getChildren();
            if (groupState == GroupState.TRANSPARENT) {
                parentAlpha = 0.5f;
            }
            for (Actor child : children) {
                child.draw(batch, parentAlpha);
            }
        }
    }

    private class TextFieldSettingGroup extends SettingGroup {
        private TextFieldSettingGroup(Label label, TextField textField, GroupState groupState) {
            super(groupState);
            this.addActor(label);
            this.addActor(textField);
            if (groupState != GroupState.CLICKABLE) {
                textField.setDisabled(true);
            }
        }
        @Override
        public void draw(Batch batch, float parentAlpha) {
            SnapshotArray<Actor> children = this.getChildren();
            if (groupState == GroupState.TRANSPARENT) {
                parentAlpha = 0.5f;
            }
            for (Actor child : children) {
                child.draw(batch, parentAlpha);
            }
        }
    }

    TwoPlayerSettingsScreen(Game game) {
        super(game);
    }

    @Override
    public void show() {
        super.show();

        Persistence persistence = Persistence.getInstance();
        gameSettings = persistence.getTwoPlayerGameSettings();
        GroupState groupState;
        if (persistence.isInPlay()) {
            groupState = GroupState.VISIBLE;
        } else {
            groupState = GroupState.CLICKABLE;
        }

        addGridLines();

        addPlayerOneName(persistence, groupState);
        addPlayerTwoName(persistence, groupState);

        addBoardSize(groupState);

        addEvenOdd(groupState);
        addSingleDouble(groupState);
        addPrimeComposite(groupState);
        addMiddleEdge(groupState);

        addDiagonalsGroup(groupState);
        addFourSquaresGroup(groupState);

        if (persistence.isInPlay()) {
            addResumeButton();
        } else {
            addPlayButton();
            addBackButton();
        }
    }

    private void addGridLines() {
        int[] position = { 302, 524, 806 };
        GridLines gridLines = new GridLines(position);
        stage.addActor(gridLines);
    }

    private void addPlayerOneName(Persistence persistence, GroupState groupState) {
        Label playerOneNameLabel = buildPlayerOneNameLabel();
        playerOneNameField = buildPlayerOneNameField(persistence);
        TextFieldSettingGroup playerOneNameGroup = new TextFieldSettingGroup(playerOneNameLabel, playerOneNameField, groupState);
        stage.addActor(playerOneNameGroup);
    }
    private Label buildPlayerOneNameLabel() {
        Label label = new Label("Name (P1)", labelStyle57);
        label.setAlignment(Align.center);
        label.setPosition(48 - 6, Launch.SCREEN_HEIGHT - 122);
        return label;
    }
    private TextField buildPlayerOneNameField(Persistence persistence) {
        String playerOneName = persistence.getPlayerOneName();
        if (textFieldStyle == null) {
            textFieldStyle = buildTextFieldStyle();
        }
        TextField playerOneNameField = new TextField(playerOneName, textFieldStyle);
        playerOneNameField.setBounds(324, Launch.SCREEN_HEIGHT - 140, 268, 93);
        playerOneNameField.setMaxLength(20);
        playerOneNameField.setMessageText("Edit...");
        return playerOneNameField;
    }

    private void addPlayerTwoName(Persistence persistence, GroupState groupState) {
        Label playerTwoNameLabel = buildPlayerTwoNameLabel();
        playerTwoNameField = buildPlayerTwoNameField(persistence);
        TextFieldSettingGroup playerTwoNameGroup = new TextFieldSettingGroup(playerTwoNameLabel, playerTwoNameField, groupState);
        stage.addActor(playerTwoNameGroup);
    }
    private Label buildPlayerTwoNameLabel() {
        Label label = new Label("Name (P2)", labelStyle57);
        label.setAlignment(Align.center);
        label.setPosition(48 - 6, Launch.SCREEN_HEIGHT - 242);
        return label;
    }
    private TextField buildPlayerTwoNameField(Persistence persistence) {
        String playerTwoName = persistence.getPlayerTwoName();
        if (textFieldStyle == null) {
            textFieldStyle = buildTextFieldStyle();
        }
        TextField playerTwoNameField = new TextField(playerTwoName, textFieldStyle);
        playerTwoNameField.setBounds(324, Launch.SCREEN_HEIGHT - 262, 268, 93);
        playerTwoNameField.setMaxLength(20);
        playerTwoNameField.setMessageText("Edit...");
        return playerTwoNameField;
    }

    private void addBoardSize(GroupState groupState) {
        Image boardSizeIcon = buildBoardSizeIcon();
        Label boardSizeLabel = buildBoardSizeLabel();
        stage.addActor(boardSizeIcon);
        stage.addActor(boardSizeLabel);

        Label label3x3 = buildLabel3x3();
        Label label4x4 = buildLabel4x4();
        ImageButton checkbox3x3 = buildCheckbox3x3();
        ImageButton checkbox4x4 = buildCheckbox4x4();
        TwoChoiceRadioSettingGroup boardSizeGroup = new TwoChoiceRadioSettingGroup(label3x3, label4x4, checkbox3x3, checkbox4x4, groupState);
        stage.addActor(boardSizeGroup);
    }
    private Image buildBoardSizeIcon() {
        int positionX = 144;
        int positionY = Launch.SCREEN_HEIGHT - 416;
        TextureRegion iconTexture = Assets.settingsSkin.getRegion("board_size_icon");
        Image boardSizeIcon = buildIcon(iconTexture, positionX, positionY);
        return boardSizeIcon;
    }
    private Label buildBoardSizeLabel() {
        Label label = new Label("Board Size", labelStyle50);
        label.setAlignment(Align.center);
        label.setPosition(80, Launch.SCREEN_HEIGHT - 490);
        return label;
    }
    private Label buildLabel3x3() {
        Label label = new Label("3x3", labelStyle57);
        label.setAlignment(Align.center);
        label.setPosition(466 - 6, Launch.SCREEN_HEIGHT - 396);
        return label;
    }
    private Label buildLabel4x4() {
        Label label = new Label("4x4", labelStyle57);
        label.setAlignment(Align.center);
        label.setPosition(466 - 6, Launch.SCREEN_HEIGHT - 498);
        return label;
    }
    private ImageButton buildCheckbox3x3() {
        int positionX = 364;
        int positionY = Launch.SCREEN_HEIGHT - 394;
        boolean isChecked = (gameSettings.getNumberOfRows() == 3);
        final ImageButton checkbox3x3 = buildCheckbox(positionX, positionY, isChecked);
        checkbox3x3.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                gameSettings.setNumberOfRows(checkbox3x3.isChecked() ? 3 : 4);
            }
        });
        return checkbox3x3;
    }
    private ImageButton buildCheckbox4x4() {
        int positionX = 364;
        int positionY = Launch.SCREEN_HEIGHT - 498;
        boolean isChecked = (gameSettings.getNumberOfRows() == 4);
        final ImageButton checkbox4x4 = buildCheckbox(positionX, positionY, isChecked);
        checkbox4x4.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                gameSettings.setNumberOfRows(checkbox4x4.isChecked() ? 4 : 3);
            }
        });
        return checkbox4x4;
    }

    private void addPlayButton() {
        ClickListener listener = new ClickListenerWithSound() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                saveUnsyncedPreferences();
                GameManager manager = TwoPlayerGameManager.createNewGameManager(ScreenTracker.playScreen);
                ScreenTracker.transitionScreen.transition(Direction.RIGHT, ScreenTracker.playScreen);
                manager.startNewGame();
            }
        };
        ForwardBottomNavigation forwardBottomNavigation = new ForwardBottomNavigation("Game", listener);
        stage.addActor(forwardBottomNavigation);
    }
    private void addBackButton() {
        BackBottomNavigation backBottomNavigation = new BackBottomNavigation("Home", ScreenTracker.titleScreen);
        stage.addActor(backBottomNavigation);
    }
    private void addResumeButton() {
        ForwardBottomNavigation forwardBottomNavigation = new ForwardBottomNavigation("Game", ScreenTracker.playScreen);
        stage.addActor(forwardBottomNavigation);
    }

    @Override
    public void render(float delta) {
        if (Gdx.input.isKeyPressed(Input.Keys.BACK)) {
            backKey = true;
        } else if (backKey) {
            backKey = false;
            Persistence persistence = Persistence.getInstance();
            if (persistence.isInPlay()) {
                ScreenTracker.transitionScreen.transition(Direction.RIGHT, ScreenTracker.playScreen);
            } else {
                ScreenTracker.transitionScreen.transition(Direction.LEFT, ScreenTracker.titleScreen);
            }
        }
        super.render(delta);
    }
    @Override
    public void hide() {
        saveUnsyncedPreferences();
    }
    @Override
    public void pause() {
        saveUnsyncedPreferences();
        saveOtherPreferences();
    }
    private void saveUnsyncedPreferences() {
        Persistence persistence = Persistence.getInstance();
        String playerOneName = playerOneNameField.getText();
        String playerTwoName = playerTwoNameField.getText();
        if (playerOneName.equals(playerTwoName)) {
            playerTwoName = playerTwoName + " 2";
        }
        persistence.setPlayerOneName(playerOneName);
        persistence.setPlayerTwoName(playerTwoName);
    }
    private void saveOtherPreferences() {
        Persistence persistence = Persistence.getInstance();
        persistence.setCurrentScreen(TAG);
        persistence.setMode(ModeTracker.mode.name());
        if (persistence.isInPlay()) {
            GameManager gameManager = TwoPlayerGameManager.getInstance();
            CortexState currentState = gameManager.getState();
            persistence.setCurrentCortexState(currentState);
        }
    }
}
