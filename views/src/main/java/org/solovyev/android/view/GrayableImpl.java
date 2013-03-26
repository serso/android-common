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
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.View;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * User: serso
 * Date: 4/19/12
 * Time: 12:51 AM
 */
public class GrayableImpl implements ViewGrayable {

    @Nullable
    private Paint paint;

    @Override
    public void grayOut() {
        paint = new Paint();
        paint.setARGB(180, 75, 75, 75);
    }

    @Override
    public void grayIn() {
        paint = null;
    }

    @Override
    public void dispatchDraw(@Nonnull View view, @Nonnull Canvas canvas) {
        final Paint localPaint = paint;
        if (localPaint != null) {
            final RectF drawRect = new RectF();
            drawRect.set(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());

            canvas.drawRoundRect(drawRect, 5, 5, localPaint);
        }
    }
}
