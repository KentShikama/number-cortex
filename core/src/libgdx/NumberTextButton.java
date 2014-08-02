package libgdx;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

/**
 * An extension of the com.badlogic.gdx.scenes.scene2d.ui.TextButton class allowing for the label to directly be modified
 */
public class NumberTextButton extends HighlightableButton {
    public static class NumberTextButtonStyle extends ButtonStyle {
        public BitmapFont font;
        public Color fontColor, downFontColor, overFontColor, checkedFontColor, checkedOverFontColor, disabledFontColor;

        public NumberTextButtonStyle() {}

        public NumberTextButtonStyle(Drawable up, Drawable down, Drawable checked, BitmapFont font) {
            super(up, down, checked);
            this.font = font;
        }

        public NumberTextButtonStyle(NumberTextButtonStyle style) {
            super(style);
            this.font = style.font;
            if (style.fontColor != null) {
                this.fontColor = new Color(style.fontColor);
            }
            if (style.downFontColor != null) {
                this.downFontColor = new Color(style.downFontColor);
            }
            if (style.overFontColor != null) {
                this.overFontColor = new Color(style.overFontColor);
            }
            if (style.checkedFontColor != null) {
                this.checkedFontColor = new Color(style.checkedFontColor);
            }
            if (style.checkedOverFontColor != null) {
                this.checkedFontColor = new Color(style.checkedOverFontColor);
            }
            if (style.disabledFontColor != null) {
                this.disabledFontColor = new Color(style.disabledFontColor);
            }
        }
    }

    private Label label;
    private NumberTextButtonStyle style;

    private String name;

    public NumberTextButton(String text, NumberTextButtonStyle style) {
        super();
        setStyle(style);
        this.style = style;
        setLabel(new Label(text, new LabelStyle(style.font, style.fontColor)));
    }

    NumberTextButton(String text, Skin skin) {
        this(text, skin.get(NumberTextButtonStyle.class));
        setSkin(skin);
    }

    NumberTextButton(String text, Skin skin, String styleName) {
        this(text, skin.get(styleName, NumberTextButtonStyle.class));
        setSkin(skin);
    }

    public void clearLabel() {
        setLabel(new Label("", new LabelStyle(style.font, style.fontColor)));
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        Color fontColor;
        if (label != null) {
            if (isDisabled() && style.disabledFontColor != null) {
                fontColor = style.disabledFontColor;
            } else if (isPressed() && style.downFontColor != null) {
                fontColor = style.downFontColor;
            } else if (isOver() && style.overFontColor != null) {
                fontColor = style.overFontColor;
            } else {
                fontColor = style.fontColor;
            }
            if (fontColor != null) {
                label.getStyle().fontColor = fontColor;
            }
        }
        super.draw(batch, parentAlpha);
    }

    public Label getLabel() {
        return label;
    }

    public Cell<Label> getLabelCell() {
        return getCell(label);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public NumberTextButtonStyle getStyle() {
        return style;
    }

    public CharSequence getText() {
        return label.getText();
    }

    public void setLabel(Label label) {
        this.label = label;
        this.clearChildren();
        label.setAlignment(Align.center);
        add(label).expand().fill();
        setSize(getWidth(), getHeight());
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setStyle(ButtonStyle style) {
        if (style == null) {
            throw new NullPointerException("style cannot be null");
        }
        if (!(style instanceof NumberTextButtonStyle)) {
            throw new IllegalArgumentException("style must be a NumberTextButtonStyle.");
        }
        super.setStyle(style);
        this.style = (NumberTextButtonStyle) style;
        if (label != null) {
            NumberTextButtonStyle textButtonStyle = (NumberTextButtonStyle) style;
            LabelStyle labelStyle = label.getStyle();
            labelStyle.font = textButtonStyle.font;
            labelStyle.fontColor = textButtonStyle.fontColor;
            label.setStyle(labelStyle);
        }
    }

    public void setText(String text) {
        label.setText(text);
    }
}
