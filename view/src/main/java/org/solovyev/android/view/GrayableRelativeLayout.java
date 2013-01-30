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

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import org.jetbrains.annotations.NotNull;

/**
 * User: serso
 * Date: 4/19/12
 * Time: 12:34 AM
 */
public class GrayableRelativeLayout extends RelativeLayout implements Grayable {

    @NotNull
    private ViewGrayable grayable = new GrayableImpl();

    public GrayableRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GrayableRelativeLayout(Context context) {
        super(context);
    }

    @Override
    public void grayOut() {
        grayable.grayOut();
    }

    @Override
    public void grayIn() {
        grayable.grayIn();
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);

        this.grayable.dispatchDraw(this, canvas);
    }
}

