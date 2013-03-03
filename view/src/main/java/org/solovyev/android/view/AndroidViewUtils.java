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

package org.solovyev.android.view;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import javax.annotation.Nonnull;

/**
 * User: serso
 * Date: 11/3/12
 * Time: 10:49 PM
 */
public final class AndroidViewUtils {

    private AndroidViewUtils() {
        throw new AssertionError();
    }

    public static boolean drawDrawables(Canvas canvas, @Nonnull TextView textView) {
        final int compoundPaddingLeft = textView.getCompoundPaddingLeft();
        final int compoundPaddingTop = textView.getCompoundPaddingTop();
        final int compoundPaddingRight = textView.getCompoundPaddingRight();
        final int compoundPaddingBottom = textView.getCompoundPaddingBottom();

        final int scrollX = textView.getScrollX();
        final int scrollY = textView.getScrollY();

        final int right = textView.getRight();
        final int left = textView.getLeft();
        final int bottom = textView.getBottom();
        final int top = textView.getTop();

        final Drawable[] drawables = textView.getCompoundDrawables();
        if (drawables != null) {

            int vspace = bottom - top - compoundPaddingBottom - compoundPaddingTop;
            int hspace = right - left - compoundPaddingRight - compoundPaddingLeft;

            Drawable topDr = drawables[1];
            // IMPORTANT: The coordinates computed are also used in invalidateDrawable()
            // Make sure to update invalidateDrawable() when changing this code.
            if (topDr != null) {
                canvas.save();
                canvas.translate(scrollX + compoundPaddingLeft + (hspace - topDr.getBounds().width()) / 2,
                        scrollY + textView.getPaddingTop() + vspace / 2);
                topDr.draw(canvas);
                canvas.restore();
                return true;
            }
        }

        return false;
    }

    public static void applyButtonDef(@Nonnull Button button, @Nonnull ButtonDef buttonDef) {
        button.setText(buttonDef.getText());

        final Integer drawableResId = buttonDef.getDrawableResId();
        if ( drawableResId != null ) {
            button.setPadding(0, 0, 0, 0);

            final Drawable drawable = button.getContext().getResources().getDrawable(drawableResId);
            button.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null);
            button.setCompoundDrawablePadding(0);
        }

        applyViewDef(button, buttonDef);
    }

    public static void applyButtonDef(@Nonnull ImageButton imageButton, @Nonnull ButtonDef buttonDef) {
        final Integer drawableResId = buttonDef.getDrawableResId();
        if ( drawableResId != null ) {
            imageButton.setImageDrawable(imageButton.getContext().getResources().getDrawable(drawableResId));
        }

        applyViewDef(imageButton, buttonDef);
    }

    public static void applyViewDef(@Nonnull View view, @Nonnull ViewDef viewDef) {
        final Integer backgroundResId = viewDef.getBackgroundResId();
        if (backgroundResId != null) {
            view.setBackgroundResource(backgroundResId);
        }

        final String tag = viewDef.getTag();
        if ( tag != null ) {
            view.setTag(tag);
        }
    }
}
