package org.solovyev.android.keyboard;

import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * User: serso
 * Date: 11/5/12
 * Time: 2:08 PM
 */
public class AKeyboardButtonPreview {

    private static final int MSG_SHOW_PREVIEW = 1;
    private static final int MSG_REMOVE_PREVIEW = 2;
    private static final int MSG_REPEAT = 3;
    private static final int MSG_LONGPRESS = 4;

    @NotNull
    private PopupWindow popup;

    @NotNull
    private final View popupParent;

    @NotNull
    private TextView previewView;

    @NotNull
    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_SHOW_PREVIEW:
                    showText0(msg.arg1, msg.arg2, (String) msg.obj);
                    break;
                case MSG_REMOVE_PREVIEW:
                    previewView.setVisibility(View.INVISIBLE);
                    break;
                /*case MSG_REPEAT:
                    if (repeatKey()) {
                        Message repeat = Message.obtain(this, MSG_REPEAT);
                        sendMessageDelayed(repeat, REPEAT_INTERVAL);
                    }
                    break;
                case MSG_LONGPRESS:
                    openPopupIfRequired((MotionEvent) msg.obj);
                    break;*/
            }
        }
    };
    private static final long DELAY_AFTER_PREVIEW = 250;
    private static final long DELAY_BEFORE_PREVIEW = 0;

    public AKeyboardButtonPreview(@NotNull View popupParent) {
        this.popupParent = popupParent;
    }

    public void createPreviewView(@NotNull LayoutInflater layoutInflater) {
        previewView = (TextView)layoutInflater.inflate(R.layout.drag_keyboard_preview, null);
        previewView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        popup = new PopupWindow(previewView);
    }

    public void showText(@NotNull View view, @Nullable CharSequence text) {
        final int[] location = new int[2];
        view.getLocationInWindow(location);
        handler.sendMessageDelayed(handler.obtainMessage(MSG_SHOW_PREVIEW, location[0] + view.getWidth() / 2, location[1], text), DELAY_BEFORE_PREVIEW);
    }

    private void showText0(int x, int y, @Nullable CharSequence text) {
        final PopupWindow popup = this.popup;

        previewView.setCompoundDrawables(null, null, null, null);
        previewView.setText(text);

        previewView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));

        final int popupWidth = previewView.getMeasuredWidth();
        final int popupHeight = previewView.getMeasuredHeight();
        final int popupMargin = 10;

        int popupX = x - popupWidth / 2;
        int popupY = y - popupHeight - popupMargin;

        handler.removeMessages(MSG_REMOVE_PREVIEW);

        if (popup.isShowing()) {
            popup.update(popupX, popupY, popupWidth, popupHeight);
        } else {
            popup.setWidth(popupWidth);
            popup.setHeight(popupHeight);
            popup.showAtLocation(popupParent, Gravity.NO_GRAVITY, popupX, popupY);
        }

        previewView.setVisibility(View.VISIBLE);

        handler.sendMessageDelayed(handler.obtainMessage(MSG_REMOVE_PREVIEW), DELAY_AFTER_PREVIEW);
    }
}
