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
import android.view.View;
import android.widget.TextView;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * User: serso
 * Date: 4/19/12
 * Time: 11:50 PM
 */
public class TextViewBuilder implements UpdatableViewBuilder<TextView> {

    private int textViewLayoutId;

    @Nullable
    private String tag;

    private TextViewBuilder() {
    }

    @Nonnull
    public static UpdatableViewBuilder<TextView> newInstance(int textViewLayoutId, @Nullable String tag) {
        final TextViewBuilder result = new TextViewBuilder();

        result.textViewLayoutId = textViewLayoutId;
        result.tag = tag;

        return result;
    }

    @Nonnull
    @Override
    public TextView build(@Nonnull Context context) {
        final TextView result = ViewFromLayoutBuilder.<TextView>newInstance(textViewLayoutId).build(context);

        result.setTag(createViewTag());

        return updateView(context, result);
    }

    @Nonnull
    private String createViewTag() {
        return tag == null ? this.getClass().getName() : tag;
    }

    @Nonnull
    @Override
    public TextView updateView(@Nonnull Context context, @Nonnull View view) {
        if (createViewTag().equals(view.getTag())) {
            return (TextView) view;
        } else {
            return build(context);
        }
    }
}
