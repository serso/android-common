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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * User: serso
 * Date: 4/18/12
 * Time: 12:57 AM
 */
public class ViewFromLayoutBuilder<V extends View> implements ViewBuilder<V> {

    private final int layoutId;

    private final int viewId;

    private final boolean wholeLayout;

    @Nullable
    private LayoutInflater layoutInflater;

    private ViewFromLayoutBuilder(int layoutId, int viewId, boolean wholeLayout) {
        this.layoutId = layoutId;
        this.viewId = viewId;
        this.wholeLayout = wholeLayout;
    }

    @NotNull
    public static <V extends View> ViewFromLayoutBuilder<V> newInstance(int layoutId, int viewId) {
        return new ViewFromLayoutBuilder<V>(layoutId, viewId, false);
    }

    @NotNull
    public static <V extends View> ViewFromLayoutBuilder<V> newInstance(int layoutId) {
        return new ViewFromLayoutBuilder<V>(layoutId, 0, true);
    }

    public void setLayoutInflater(@Nullable LayoutInflater layoutInflater) {
        this.layoutInflater = layoutInflater;
    }

    @NotNull
    @Override
    public V build(@NotNull Context context) {

        LayoutInflater li = layoutInflater;
        if (li == null) {
            li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        if (wholeLayout) {
            // if whole layout - just return view
            return (V)li.inflate(layoutId, null);
        } else {
            // else try to find view by id
            final ViewGroup itemView = (ViewGroup) li.inflate(layoutId, null);
            return (V)itemView.findViewById(viewId);
        }
    }
}
