package com.numbercortex;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Disableable;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Array;

public class HighlightableButton extends Table implements Disableable {
	private ButtonStyle style;
	boolean isChecked, isDisabled, isHighlighted;
	ButtonGroup buttonGroup;
	private ClickListener clickListener;

	public HighlightableButton (Skin skin) {
		super(skin);
		initialize();
		setStyle(skin.get(ButtonStyle.class));
		setSize(getPrefWidth(), getPrefHeight());
	}

	public HighlightableButton (Skin skin, String styleName) {
		super(skin);
		initialize();
		setStyle(skin.get(styleName, ButtonStyle.class));
		setSize(getPrefWidth(), getPrefHeight());
	}

	public HighlightableButton (Actor child, Skin skin, String styleName) {
		this(child, skin.get(styleName, ButtonStyle.class));
	}

	public HighlightableButton (Actor child, ButtonStyle style) {
		initialize();
		add(child);
		setStyle(style);
		setSize(getPrefWidth(), getPrefHeight());
	}

	public HighlightableButton (ButtonStyle style) {
		initialize();
		setStyle(style);
		setSize(getPrefWidth(), getPrefHeight());
	}

	/** Creates a button without setting the style or size. At least a style must be set before using this button. */
	public HighlightableButton () {
		initialize();
	}

	private void initialize () {
		setTouchable(Touchable.enabled);
		addListener(clickListener = new ClickListener() {
			public void clicked (InputEvent event, float x, float y) {
				if (isDisabled) return;
			}
		});
	}

	public HighlightableButton (Drawable up) {
		this(new ButtonStyle(up, null, null));
	}

	public HighlightableButton (Drawable up, Drawable down) {
		this(new ButtonStyle(up, down, null));
	}

	public HighlightableButton (Drawable up, Drawable down, Drawable checked) {
		this(new ButtonStyle(up, down, checked));
	}

	public HighlightableButton (Actor child, Skin skin) {
		this(child, skin.get(ButtonStyle.class));
	}

	public boolean isPressed () {
		return clickListener.isVisualPressed();
	}

	public boolean isOver () {
		return clickListener.isOver();
	}

	public ClickListener getClickListener () {
		return clickListener;
	}

	public boolean isDisabled () {
		return isDisabled;
	}

	/** When true, the button will not toggle {@link #isChecked()} when clicked and will not fire a {@link ChangeEvent}. */
	public void setDisabled (boolean isDisabled) {
		this.isDisabled = isDisabled;
	}
	
	public boolean isHighlighted () {
		return isHighlighted;
	}

	public void setHighlighted (boolean isHighlighted) {
		this.isHighlighted = isHighlighted;
	}
	
	public void setStyle (ButtonStyle style) {
		if (style == null) throw new IllegalArgumentException("style cannot be null.");
		this.style = style;

		Drawable background = style.up;
		if (background == null) {
			background = style.down;
			if (background == null) background = style.checked;
		}
		if (background != null) {
			padBottom(background.getBottomHeight());
			padTop(background.getTopHeight());
			padLeft(background.getLeftWidth());
			padRight(background.getRightWidth());
		}
		invalidateHierarchy();
	}

	/** Returns the button's style. Modifying the returned style may not have an effect until {@link #setStyle(ButtonStyle)} is
	 * called. */
	public ButtonStyle getStyle () {
		return style;
	}

	public void draw (Batch batch, float parentAlpha) {
		validate();

		Drawable background = null;
		float offsetX = 0, offsetY = 0;
		if (isPressed() && !isDisabled) {
			background = style.down == null ? style.up : style.down;
			offsetX = style.pressedOffsetX;
			offsetY = style.pressedOffsetY;
		} else {
			if (isDisabled && style.disabled != null)
				background = style.disabled;
			else if (isChecked && style.checked != null)
				background = (isOver() && style.checkedOver != null) ? style.checkedOver : style.checked;
			else if (isOver() && style.over != null)
				background = style.over;
			else if (isHighlighted && style.highlighted != null)
				background = style.highlighted;
			else
				background = style.up;
			offsetX = style.unpressedOffsetX;
			offsetY = style.unpressedOffsetY;
		}
		setBackground(background, false);

		Array<Actor> children = getChildren();
		for (int i = 0; i < children.size; i++)
			children.get(i).moveBy(offsetX, offsetY);
		super.draw(batch, parentAlpha);
		for (int i = 0; i < children.size; i++)
			children.get(i).moveBy(-offsetX, -offsetY);
	}

	public float getPrefWidth () {
		float width = super.getPrefWidth();
		if (style.up != null) width = Math.max(width, style.up.getMinWidth());
		if (style.down != null) width = Math.max(width, style.down.getMinWidth());
		if (style.checked != null) width = Math.max(width, style.checked.getMinWidth());
		return width;
	}

	public float getPrefHeight () {
		float height = super.getPrefHeight();
		if (style.up != null) height = Math.max(height, style.up.getMinHeight());
		if (style.down != null) height = Math.max(height, style.down.getMinHeight());
		if (style.checked != null) height = Math.max(height, style.checked.getMinHeight());
		return height;
	}

	public float getMinWidth () {
		return getPrefWidth();
	}

	public float getMinHeight () {
		return getPrefHeight();
	}

	static public class ButtonStyle {
		/** Optional. */
		public Drawable up, down, over, checked, checkedOver, disabled, highlighted;
		/** Optional. */
		public float pressedOffsetX, pressedOffsetY;
		/** Optional. */
		public float unpressedOffsetX, unpressedOffsetY;

		public ButtonStyle () {
		}

		public ButtonStyle (Drawable up, Drawable down, Drawable checked) {
			this.up = up;
			this.down = down;
			this.checked = checked;
		}

		public ButtonStyle (ButtonStyle style) {
			this.up = style.up;
			this.down = style.down;
			this.over = style.over;
			this.checked = style.checked;
			this.checkedOver = style.checkedOver;
			this.disabled = style.disabled;
			this.pressedOffsetX = style.pressedOffsetX;
			this.pressedOffsetY = style.pressedOffsetY;
			this.unpressedOffsetX = style.unpressedOffsetX;
			this.unpressedOffsetY = style.unpressedOffsetY;
		}
	}
}
