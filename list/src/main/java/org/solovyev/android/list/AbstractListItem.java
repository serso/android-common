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
 * ---------------------------------------------------------------------
 * Contact details
 *
 * Email: se.solovyev@gmail.com
 * Site:  http://se.solovyev.org
 */

package org.solovyev.android.list;

import android.content.Context;
import org.solovyev.android.view.TextViewBuilder;
import android.view.View;
import android.widget.TextView;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.solovyev.android.view.UpdatableViewBuilder;

/**
 * User: serso
 * Date: 4/18/12
 * Time: 1:23 AM
 */
public abstract class AbstractListItem implements UpdatableViewBuilder<TextView> {

    @NotNull
    private final UpdatableViewBuilder<TextView> textViewCreator;

    protected AbstractListItem(int textViewLayoutId, @NotNull String tag) {
        this.textViewCreator = TextViewBuilder.newInstance(textViewLayoutId, tag);
    }

    protected AbstractListItem(int textViewLayoutId) {
        this.textViewCreator = TextViewBuilder.newInstance(textViewLayoutId, null);
    }

    @Override
    @NotNull
    public TextView updateView(@NotNull Context context, @NotNull View view) {
        return fillView(context, textViewCreator.updateView(context, view));
    }

    @Override
    @NotNull
    public TextView build(@NotNull Context context) {
        return fillView(context, textViewCreator.build(context));
    }

    @NotNull
    private TextView fillView(@NotNull Context context, @NotNull TextView textView) {
        textView.setText(getText(context));
        return textView;
    }

    @Nullable
    protected abstract CharSequence getText(@NotNull Context context);
}
