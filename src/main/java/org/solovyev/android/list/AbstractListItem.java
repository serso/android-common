package org.solovyev.android.list;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.solovyev.android.view.TextViewBuilder;
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
