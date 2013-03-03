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

package org.solovyev.android.view.drag;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.EnumMap;
import java.util.Map;

/**
 * User: serso
 * Date: 11/3/12
 * Time: 1:57 PM
 */
public class DirectionDragButtonDefImpl implements DirectionDragButtonDef {

    @Nullable
    private CharSequence text;

    private Map<DragDirection, CharSequence> directionsTexts = new EnumMap<DragDirection, CharSequence>(DragDirection.class);

    @Nullable
    private Integer backgroundResId;

    @Nullable
    private Integer drawableResId;

    @Nullable
    private String tag;

    @Nullable
    private Float weight;

    @Nullable
    private Integer layoutMarginLeft;

    @Nullable
    private Integer layoutMarginRight;

    private DirectionDragButtonDefImpl() {
    }

    @Nonnull
    public static DirectionDragButtonDefImpl newInstance(@Nullable CharSequence text) {
        return newInstance(text, null, null, null, null);
    }

    @Nonnull
    public static DirectionDragButtonDefImpl newInstance(@Nullable CharSequence text,
                                                     @Nullable CharSequence up,
                                                     @Nullable CharSequence right,
                                                     @Nullable CharSequence down,
                                                     @Nullable CharSequence left) {
        return newInstance(text, up, right, down, left, null);
    }

    @Nonnull
    public static DirectionDragButtonDefImpl newInstance(@Nullable CharSequence text,
                                                     @Nullable CharSequence up,
                                                     @Nullable CharSequence right,
                                                     @Nullable CharSequence down,
                                                     @Nullable CharSequence left,
                                                     @Nullable Integer backgroundColor) {
        final DirectionDragButtonDefImpl result = new DirectionDragButtonDefImpl();

        result.text = text;
        result.directionsTexts.put(DragDirection.up, up);
        result.directionsTexts.put(DragDirection.right, right);
        result.directionsTexts.put(DragDirection.down, down);
        result.directionsTexts.put(DragDirection.left, left);

        result.backgroundResId = backgroundColor;

        return result;
    }

    @Nonnull
    public static DirectionDragButtonDefImpl newDrawableInstance(@Nonnull Integer drawableResId) {
        return newDrawableInstance(drawableResId, null);
    }

    @Nonnull
    public static DirectionDragButtonDefImpl newDrawableInstance(@Nonnull Integer drawableResId, @Nullable Integer backgroundColor) {
        final DirectionDragButtonDefImpl result = new DirectionDragButtonDefImpl();

        result.drawableResId = drawableResId;
        result.backgroundResId = backgroundColor;

        return result;

    }

    @Nullable
    @Override
    public CharSequence getText(@Nonnull DragDirection dragDirection) {
        return directionsTexts.get(dragDirection);
    }

    @Nullable
    @Override
    public Float getLayoutWeight() {
        return this.weight;
    }

    @Nullable
    @Override
    public Integer getLayoutMarginLeft() {
        return this.layoutMarginLeft;
    }

    @Nullable
    @Override
    public Integer getLayoutMarginRight() {
        return this.layoutMarginRight;
    }

    @Nullable
    @Override
    public Integer getDrawableResId() {
        return this.drawableResId;
    }

    @Nullable
    @Override
    public String getTag() {
        return tag;
    }

    @Nullable
    @Override
    public Integer getBackgroundResId() {
        return this.backgroundResId;
    }

    @Nullable
    @Override
    public CharSequence getText() {
        return text;
    }

    public void setWeight(@Nullable Float weight) {
        this.weight = weight;
    }

    public void setLayoutMarginRight(@Nullable Integer layoutMarginRight) {
        this.layoutMarginRight = layoutMarginRight;
    }

    public void setLayoutMarginLeft(@Nullable Integer layoutMarginLeft) {
        this.layoutMarginLeft = layoutMarginLeft;
    }

	public void setBackgroundResId(int backgroundResId) {
		this.backgroundResId = backgroundResId;
	}

	public void setTag(@Nullable String tag) {
		this.tag = tag;
	}

	public void setText(@Nullable CharSequence text) {
		this.text = text;
	}

	public void setBackgroundResId(@Nullable Integer backgroundResId) {
		this.backgroundResId = backgroundResId;
	}

	public void setDrawableResId(@Nullable Integer drawableResId) {
		this.drawableResId = drawableResId;
	}

	public void setDirectionText(@Nonnull DragDirection key, @Nullable CharSequence text) {
		directionsTexts.put(key, text);
	}
}
