package org.solovyev.android.samples.http;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import org.jetbrains.annotations.NotNull;
import org.solovyev.android.http.RemoteFileService;
import org.solovyev.android.list.ListItem;
import org.solovyev.android.samples.R;
import org.solovyev.android.view.ViewFromLayoutBuilder;

/**
 * User: serso
 * Date: 8/10/12
 * Time: 2:32 PM
 */
public class HttpListItem implements ListItem {

    @NotNull
    private String uri;

    @NotNull
    private RemoteFileService remoteFileService;

    public HttpListItem(@NotNull String uri, @NotNull RemoteFileService remoteFileService) {
        this.uri = uri;
        this.remoteFileService = remoteFileService;
    }

    @Override
    public OnClickAction getOnClickAction() {
        return null;
    }

    @Override
    public OnClickAction getOnLongClickAction() {
        return null;
    }

    @NotNull
    @Override
    public View updateView(@NotNull Context context, @NotNull View view) {
        if (getTag().equals(view.getTag())) {
            fillView(context, view);
            return view;
        } else {
            return build(context);
        }
    }

    private void fillView(@NotNull Context context, @NotNull View root) {
        final ImageView icon = (ImageView) root.findViewById(R.id.http_item_icon);

        remoteFileService.loadImage(uri, icon, R.drawable.icon);

        final TextView text = (TextView) root.findViewById(R.id.http_item_text);
        text.setText(uri);
    }

    @NotNull
    @Override
    public View build(@NotNull Context context) {
        final View root = ViewFromLayoutBuilder.newInstance(R.layout.http_list_item).build(context);
        root.setTag(getTag());
        fillView(context, root);
        return root;
    }

    @NotNull
    private String getTag() {
        return "http_list_item";
    }
}
