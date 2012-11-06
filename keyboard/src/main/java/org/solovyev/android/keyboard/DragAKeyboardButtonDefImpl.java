package org.solovyev.android.keyboard;

import android.content.res.Resources;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.solovyev.android.view.drag.DirectionDragButtonDefImpl;
import org.solovyev.android.view.drag.DragDirection;

import java.util.EnumMap;
import java.util.Map;

public class DragAKeyboardButtonDefImpl implements DragAKeyboardButtonDef {

	@NotNull
	private DirectionDragButtonDefImpl directionDragButtonDef;


	private boolean allowRepeat = true;

	@Nullable
	private Integer keycode;

	@NotNull
	private final Map<DragDirection, Integer> directionKeycodes = new EnumMap<DragDirection, Integer>(DragDirection.class);

	@Override
	@Nullable
	public CharSequence getText(@NotNull DragDirection dragDirection) {
		return directionDragButtonDef.getText(dragDirection);
	}

	@Override
	@Nullable
	public Integer getDrawableResId() {
		return directionDragButtonDef.getDrawableResId();
	}

	@Override
	@Nullable
	public CharSequence getText() {
		return directionDragButtonDef.getText();
	}

	@Override
	@Nullable
	public Float getLayoutWeight() {
		return directionDragButtonDef.getLayoutWeight();
	}

	@Override
	@Nullable
	public Integer getLayoutMarginLeft() {
		return directionDragButtonDef.getLayoutMarginLeft();
	}

	@Override
	@Nullable
	public Integer getLayoutMarginRight() {
		return directionDragButtonDef.getLayoutMarginRight();
	}

	@Override
	@Nullable
	public String getTag() {
		return directionDragButtonDef.getTag();
	}

	@Override
	@Nullable
	public Integer getBackgroundResId() {
		return directionDragButtonDef.getBackgroundResId();
	}

	@Override
	public boolean allowRepeat() {
		return this.allowRepeat;
	}

	@Nullable
	@Override
	public Integer getKeycode() {
		return this.keycode;
	}

	public void setKeycode(@Nullable Integer actionCode) {
		this.keycode = actionCode;
	}

	@Override
	public Integer getDirectionKeycode(@NotNull DragDirection dragDirection) {
		return this.directionKeycodes.get(dragDirection);
	}

	public void setDirectionKeycode(@NotNull DragDirection direction, @Nullable Integer actionCode) {
		directionKeycodes.put(direction, actionCode);
	}

	public void setAllowRepeat(boolean allowRepeat) {
		this.allowRepeat = allowRepeat;
	}

	@NotNull
	public static DragAKeyboardButtonDefImpl newInstance(@Nullable CharSequence text,
														 @Nullable CharSequence up,
														 @Nullable CharSequence right,
														 @Nullable CharSequence down,
														 @Nullable CharSequence left) {
		final DragAKeyboardButtonDefImpl result = new DragAKeyboardButtonDefImpl();

		result.directionDragButtonDef = DirectionDragButtonDefImpl.newInstance(text, up, right, down, left);

		return result;
	}

	public static DragAKeyboardButtonDefImpl newInstance(@Nullable CharSequence text,
													 @Nullable CharSequence up,
													 @Nullable CharSequence right,
													 @Nullable CharSequence down,
													 @Nullable CharSequence left,
													 int drawableResId) {
		final DragAKeyboardButtonDefImpl result = new DragAKeyboardButtonDefImpl();

		result.directionDragButtonDef = DirectionDragButtonDefImpl.newInstance(text, up, right, down, left, drawableResId);

		return result;
	}

	public static DragAKeyboardButtonDef newInstance(@Nullable CharSequence text, @Nullable Integer keycode) {
		final DragAKeyboardButtonDefImpl result = new DragAKeyboardButtonDefImpl();

		result.directionDragButtonDef = DirectionDragButtonDefImpl.newInstance(text);
		result.keycode = keycode;

		return result;
	}

	public static DragAKeyboardButtonDef newDrawableInstance(int drawableResId, int keycode) {
		final DragAKeyboardButtonDefImpl result = new DragAKeyboardButtonDefImpl();

		result.directionDragButtonDef = DirectionDragButtonDefImpl.newDrawableInstance(drawableResId);
		result.keycode = keycode;

		return result;
	}

	public static DragAKeyboardButtonDef newDrawableInstance(int drawableResId,
															 int keycode,
															 int backgroundResId) {
		final DragAKeyboardButtonDefImpl result = new DragAKeyboardButtonDefImpl();

		result.directionDragButtonDef = DirectionDragButtonDefImpl.newDrawableInstance(drawableResId, backgroundResId);
		result.keycode = keycode;

		return result;
	}

	public void setWeight(@Nullable Float weight) {
		directionDragButtonDef.setWeight(weight);
	}

	public void setLayoutMarginRight(@Nullable Integer layoutMarginRight) {
		directionDragButtonDef.setLayoutMarginRight(layoutMarginRight);
	}

	public void setLayoutMarginLeft(@Nullable Integer layoutMarginLeft) {
		directionDragButtonDef.setLayoutMarginLeft(layoutMarginLeft);
	}

	public void setBackgroundResId(int backgroundResId) {
		directionDragButtonDef.setBackgroundResId(backgroundResId);
	}

	public void setTag(@Nullable String tag) {
		directionDragButtonDef.setTag(tag);
	}

	@Override
	public void setImeOptions(@NotNull Resources resources, int imeOptions) {
	}

	@Override
	public void setShifted(boolean shifted) {
	}
}
