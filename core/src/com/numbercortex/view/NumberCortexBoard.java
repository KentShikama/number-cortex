package com.numbercortex.view;

import java.util.ArrayList;
import java.util.Map;
import libgdx.NumberTextButton;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.DelayAction;
import com.badlogic.gdx.scenes.scene2d.actions.MoveByAction;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

class NumberCortexBoard {

    private static NumberTextButton.NumberTextButtonStyle greenRectangleStyle = buildButtonStyle("green_rectangle");
    private static NumberTextButton.NumberTextButtonStyle blueRectangleStyle = buildButtonStyle("blue_rectangle");
    private static NumberTextButton.NumberTextButtonStyle redRectangleStyle = buildButtonStyle("red_rectangle");
    private static NumberTextButton.NumberTextButtonStyle buildButtonStyle(String textureName) {
        BitmapFont font = Assets.boardNumberFont;
        Drawable numberRectangle = Assets.gameSkin.getDrawable(textureName);
        Drawable numberRectangleChecked = Assets.gameSkin.getDrawable(textureName + "_checked");
        NumberTextButton.NumberTextButtonStyle buttonStyle = new NumberTextButton.NumberTextButtonStyle();
        buttonStyle.font = font;
        buttonStyle.fontColor = Launch.BRIGHT_YELLOW;
        buttonStyle.up = numberRectangle;
        buttonStyle.highlighted = numberRectangleChecked;
        return buttonStyle;
    }

    private ArrayList<NumberTextButton> cells;
    private boolean isBlue;
    private int numberOfRows;

    private static class Singleton {
        private static NumberCortexBoard INSTANCE = new NumberCortexBoard();
    }
    private NumberCortexBoard() {
        this.isBlue = true;
        this.numberOfRows = 4;
        this.cells = buildCells(this.isBlue, numberOfRows);
    }

    static NumberCortexBoard createNumberCortexBoard(Stage stage, boolean isBlue, int numberOfRows) {
        NumberCortexBoard instance = Singleton.INSTANCE;
        if (instance.isBlue != isBlue || instance.numberOfRows != numberOfRows || instance.cells == null) {
            instance.isBlue = isBlue;
            instance.numberOfRows = numberOfRows;
            if (greenRectangleStyle == null || blueRectangleStyle == null || redRectangleStyle == null) {
                greenRectangleStyle = buildButtonStyle("green_rectangle");
                blueRectangleStyle = buildButtonStyle("blue_rectangle");
                redRectangleStyle = buildButtonStyle("red_rectangle");
            }
            instance.cells = buildCells(instance.isBlue, instance.numberOfRows);
        }
        float worldWidth = stage.getViewport().getWorldWidth();
        resetCellPositions(instance.numberOfRows, instance.cells, worldWidth);
        addCellsToStageIfAbsent(stage, instance);
        return instance;
    }
    private static ArrayList<NumberTextButton> buildCells(boolean isBlue, int numberOfRows) {
        ArrayList<NumberTextButton> cells = new ArrayList<NumberTextButton>();
        for (int i = 0; i < numberOfRows * numberOfRows; i++) {
            NumberTextButton rectangle;
            if (isGreen(i, numberOfRows)) {
                rectangle = new NumberTextButton("", greenRectangleStyle);
            } else {
                rectangle = createComplementaryColorRectangle(isBlue);
            }
            rectangle.setName(String.valueOf(i));
            cells.add(rectangle);
        }
        return cells;
    }
    private static NumberTextButton createComplementaryColorRectangle(boolean isBlue) {
        NumberTextButton rectangle;
        if (isBlue) {
            rectangle = new NumberTextButton("", blueRectangleStyle);
        } else {
            rectangle = new NumberTextButton("", redRectangleStyle);
        }
        return rectangle;
    }
    private static boolean isGreen(int i, int numberOfRows) {
        int[] greenListThree = { 0, 2, 4, 6, 8 };
        int[] greenListFour = { 0, 2, 5, 7, 8, 10, 13, 15 };
        if (numberOfRows == 3) {
            for (int green : greenListThree) {
                if (green == i) {
                    return true;
                }
            }
            return false;
        } else {
            for (int green : greenListFour) {
                if (green == i) {
                    return true;
                }
            }
            return false;
        }
    }
    private static void resetCellPositions(int numberOfRows, ArrayList<NumberTextButton> cells, float worldWidth) {
        int squareLength = Launch.SCREEN_WIDTH / numberOfRows + 1;
        for (int i = 0; i < numberOfRows * numberOfRows; i++) {
            int left = (i % numberOfRows) * squareLength;
            int bottom = ((numberOfRows - 1) - i / numberOfRows) * squareLength;
            NumberTextButton button = cells.get(i);
            float offsetFromOriginalWidth = (worldWidth - Launch.SCREEN_WIDTH) / 2;
            button.setBounds(left + offsetFromOriginalWidth, bottom + (Launch.SCREEN_HEIGHT - 850), squareLength, squareLength);
        }
    }
    private static void addCellsToStageIfAbsent(Stage stage, NumberCortexBoard instance) {
        for (NumberTextButton cell : instance.cells) {
            if (stage.getActors().size == 0) {
                cell.setText("");
                stage.addActor(cell);
            } else {
                for (Actor actor : stage.getActors()) {
                    if (actor == cell) {
                        continue;
                    }
                    cell.setText("");
                    stage.addActor(cell);
                }
            }
        }
    }

    void updateCell(int coordinate, int number) {
        NumberTextButton cell = cells.get(coordinate);
        cell.setText(String.valueOf(number));
    }
    void clearCell(int coordinate) {
        NumberTextButton cell = cells.get(coordinate);
        cell.setText("");
    }

    float showWinningCoordinates(Map<Integer, Integer> winningMap) {
        for (Map.Entry<Integer, Integer> winningEntry : winningMap.entrySet()) {
            buildWinningAction(winningEntry);
        }
        return 4f;
    }
    private void buildWinningAction(Map.Entry<Integer, Integer> winningEntry) {
        int winningCoordinate = winningEntry.getKey();
        final int winningValue = winningEntry.getValue();
        final NumberTextButton cell = cells.get(winningCoordinate);
        final Label cellLabel = cell.getLabel();
        RepeatAction repeatAction = buildRepeatableFlashingAction(winningValue, cell, cellLabel);
        cell.addAction(repeatAction);
    }
    private RepeatAction buildRepeatableFlashingAction(final int winningValue, final NumberTextButton cell, final Label cellLabel) {
        DelayAction delayAction = Actions.delay(0.5f);
        Action toggleAction = buildToggleAction(winningValue, cell, cellLabel);
        SequenceAction sequence = Actions.sequence(toggleAction, delayAction);
        RepeatAction repeatAction = new RepeatAction();
        repeatAction.setAction(sequence);
        repeatAction.setCount(8);
        return repeatAction;
    }
    private Action buildToggleAction(final int winningValue, final NumberTextButton cell, final Label cellLabel) {
        Action toggleAction = new Action() {
            @Override
            public boolean act(float delta) {
                toggleCell(winningValue, cell, cellLabel);
                return true;
            }
            private void toggleCell(final int winningValue, final NumberTextButton cell, final Label cellLabel) {
                if (cell.isHighlighted()) {
                    cellLabel.setText(String.valueOf(winningValue));
                    cell.setHighlighted(false);
                } else {
                    cellLabel.setText("");
                    cell.setHighlighted(true);
                }
            }
        };
        return toggleAction;
    }

    void clearBoard() {
        for (NumberTextButton cell : cells) {
            cell.clearActions();
            cell.setHighlighted(false);
        }
    }

    ArrayList<NumberTextButton> getBoardCells() {
        return cells;
    }
    int getNumberOfRows() {
        return numberOfRows;
    }

    void bringCellsDown() {
        for (NumberTextButton button : cells) {
            button.moveBy(0, -220);
        }
    }
    void bringCellsDownWithAnimation(float delay) {
        for (NumberTextButton button : cells) {
            DelayAction delayAction = Actions.delay(delay);
            MoveByAction moveToAction = Actions.moveBy(0, -220, 1f);
            button.addAction(Actions.sequence(delayAction, moveToAction));
        }
    }

    static void dispose() {
        Singleton.INSTANCE.cells = null;
        greenRectangleStyle = null;
        blueRectangleStyle = null;
        redRectangleStyle = null;
    }
}