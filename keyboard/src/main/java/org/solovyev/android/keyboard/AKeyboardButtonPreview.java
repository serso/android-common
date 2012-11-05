package org.solovyev.android.keyboard;

import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
    private View previewView;

    @NotNull
    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_SHOW_PREVIEW:
                    showText0((Params) msg.obj);
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
    private static final long DELAY_AFTER_PREVIEW = 500;
    private static final long DELAY_BEFORE_PREVIEW = 0;

    public AKeyboardButtonPreview(@NotNull View popupParent) {
        this.popupParent = popupParent;
    }

    public void createPreviewView(@NotNull LayoutInflater layoutInflater) {
        previewView = layoutInflater.inflate(R.layout.drag_keyboard_preview, null);
        previewView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        popup = new PopupWindow(previewView);
    }

    public void showPreview(@NotNull View view, @Nullable CharSequence text, @Nullable Integer drawableResId) {
        final int[] location = new int[2];
        view.getLocationInWindow(location);
        int x = location[0] + view.getWidth() / 2;
        int y = location[1];

        final Params params;
        if (text == null) {
            if (drawableResId != null) {
                params = Params.newDrawableInstance(x, y, drawableResId);
            } else {
                params = Params.newTextInstance(x, y, "");
                Log.e(AKeyboardButtonPreview.class.getSimpleName(), "For view: " + view + " neither text nor drawable resource is specified!");
            }
        } else {
            params = Params.newTextInstance(x, y, text.toString());
        }

        handler.sendMessageDelayed(handler.obtainMessage(MSG_SHOW_PREVIEW, params), DELAY_BEFORE_PREVIEW);
    }

    private void showText0(@NotNull Params params) {
        final PopupWindow popup = this.popup;

        boolean image = false;

        final TextView previewTextView = (TextView) previewView.findViewById(R.id.preview_text_view);
        previewTextView.setText(params.text);

        final Integer drawableResId = params.drawableResId;
        final ImageView previewImageView = (ImageView) previewView.findViewById(R.id.preview_image_view);
        if (drawableResId != null) {
            final Drawable drawable = previewView.getContext().getResources().getDrawable(drawableResId);
            previewImageView.setImageDrawable(drawable);
            if (drawable != null) {
                image = true;
            }
        } else {
            previewImageView.setImageDrawable(null);
        }

        if (image) {
            previewTextView.setVisibility(View.GONE);
            previewImageView.setVisibility(View.VISIBLE);
        } else {
            previewImageView.setVisibility(View.GONE);
            previewTextView.setVisibility(View.VISIBLE);
        }

        previewView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));

        final int popupWidth = previewView.getMeasuredWidth();
        final int popupHeight = previewView.getMeasuredHeight();
        final int popupMargin = 10;

        int popupX = params.x - popupWidth / 2;
        int popupY = params.y - popupHeight - popupMargin;

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

    public static final class Params implements Parcelable {

        @NotNull
        public static final Parcelable.Creator<Params> CREATOR = new Parcelable.Creator<Params>() {

            @Override
            public Params createFromParcel(@NotNull Parcel in) {
                return new Params(in);
            }

            @Override
            public Params[] newArray(int size) {
                return new Params[size];
            }
        };


        private int x;

        private int y;

        @Nullable
        private String text;

        @Nullable
        private Integer drawableResId;

        public Params(@NotNull Parcel in) {
            this.x = in.readInt();
            this.y = in.readInt();
            this.text = in.readString();
            this.drawableResId = in.readInt();
        }

        private Params(int x, int y, @Nullable String text, @Nullable Integer drawableResId) {
            this.x = x;
            this.y = y;
            this.text = text;
            this.drawableResId = drawableResId;
        }

        @NotNull
        public static Params newTextInstance(int x, int y, @NotNull String text) {
            return new Params(x, y, text, null);
        }

        @NotNull
        public static Params newDrawableInstance(int x, int y, @NotNull Integer drawableResId) {
            return new Params(x, y, null, drawableResId);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(@NotNull Parcel out, int flags) {
            out.writeInt(x);
            out.writeInt(y);
            out.writeString(text);
            out.writeInt(drawableResId == null ? 0 : drawableResId);
        }
    }
}
