/*
 * Copyright 2013 serso aka se.solovyev
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Contact details
 *
 * Email: se.solovyev@gmail.com
 * Site:  http://se.solovyev.org
 */

package org.solovyev.android.keyboard;

import android.content.res.Resources;
import org.solovyev.android.view.drag.DirectionDragButtonDefImpl;
import org.solovyev.android.view.drag.DragDirection;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.EnumMap;
import java.util.Map;

public class DragAKeyboardButtonDefImpl implements DragAKeyboardButtonDef {

	@Nonnull
	private DirectionDragButtonDefImpl directionDragButtonDef;


	private boolean allowRepeat = true;

	@Nullable
	private Integer keycode;

	@Nonnull
	private final Map<DragDirection, Integer> directionKeycodes = new EnumMap<DragDirection, Integer>(DragDirection.class);

	@Nullable
	private Integer previewDrawableResId;

	@Nullable
	private CharSequence previewText;

	@Nonnull
	private final Map<DragDirection, CharSequence> previewDirectionTexts = new EnumMap<DragDirection, CharSequence>(DragDirection.class);


	@Override
	@Nullable
	public CharSequence getText(@Nonnull DragDirection dragDirection) {
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

	@Nullable
	@Override
	public Integer getPreviewDrawableResId() {
		return this.previewDrawableResId;
	}

	public void setKeycode(@Nullable Integer actionCode) {
		this.keycode = actionCode;
	}

	@Override
	public Integer getDirectionKeycode(@Nonnull DragDirection dragDirection) {
		return this.directionKeycodes.get(dragDirection);
	}

	public void setDirectionKeycode(@Nonnull DragDirection direction, @Nullable Integer actionCode) {
		directionKeycodes.put(direction, actionCode);
	}

	public void setAllowRepeat(boolean allowRepeat) {
		this.allowRepeat = allowRepeat;
	}

	@Nonnull
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

	public static DragAKeyboardButtonDefImpl newInstance(@Nullable CharSequence text, @Nullable Integer keycode) {
		final DragAKeyboardButtonDefImpl result = new DragAKeyboardButtonDefImpl();

		result.directionDragButtonDef = DirectionDragButtonDefImpl.newInstance(text);
		result.keycode = keycode;

		return result;
	}

	public static DragAKeyboardButtonDefImpl newDrawableInstance(int drawableResId, int keycode) {
		final DragAKeyboardButtonDefImpl result = new DragAKeyboardButtonDefImpl();

		result.directionDragButtonDef = DirectionDragButtonDefImpl.newDrawableInstance(drawableResId);
		result.keycode = keycode;

		return result;
	}

	public static DragAKeyboardButtonDefImpl newDrawableInstance(int drawableResId,
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

	public void setText(@Nullable CharSequence text) {
		directionDragButtonDef.setText(text);
	}

	public void setBackgroundResId(@Nullable Integer backgroundResId) {
		directionDragButtonDef.setBackgroundResId(backgroundResId);
	}

	public void setDrawableResId(@Nullable Integer drawableResId) {
		directionDragButtonDef.setDrawableResId(drawableResId);
	}

	public void setDirectionText(@Nonnull DragDirection key, @Nullable CharSequence text) {
		directionDragButtonDef.setDirectionText(key, text);
	}

	public void setPreviewText(@Nullable CharSequence previewText) {
		this.previewText = previewText;
	}

	public void setPreviewDirectionText(@Nonnull DragDirection direction,
										@Nullable CharSequence charSequence) {
		previewDirectionTexts.put(direction, charSequence);
	}

	@Override
	public void setImeOptions(@Nonnull Resources resources, int imeOptions) {
	}

	@Override
	public void setShifted(boolean shifted) {
	}

	@Nullable
	@Override
	public CharSequence getPreviewText() {
		return this.previewText;
	}

	@Nullable
	@Override
	public CharSequence getPreviewDirectionText(@Nonnull DragDirection direction) {
		return this.previewDirectionTexts.get(direction);
	}

	public void setPreviewDrawableResId(@Nullable Integer previewDrawableResId) {
		this.previewDrawableResId = previewDrawableResId;
	}
}
