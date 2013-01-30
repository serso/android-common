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

package org.solovyev.android.keyboard;

import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
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
import org.solovyev.android.AndroidUtils;

/**
 * User: serso
 * Date: 11/5/12
 * Time: 2:08 PM
 */
public class AKeyboardButtonPreview {

    private static final String TAG = AKeyboardButtonPreview.class.getSimpleName();

    private static final int MSG_SHOW_PREVIEW = 1;
    private static final int MSG_REMOVE_PREVIEW = 2;

    private static final long DELAY_AFTER_PREVIEW = 200;
    private static final long DELAY_BEFORE_PREVIEW = 0;

    @NotNull
    private PopupWindow popup;

    @NotNull
    private final View popupParent;

    private View previewView;

    @NotNull
    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_SHOW_PREVIEW:
                    Log.d(TAG, "Show preview for " + msg.obj);
                    showText0((PreviewParams) msg.obj);
                    break;
                case MSG_REMOVE_PREVIEW:
                    Log.d(TAG, "Hide preview for " + msg.obj);
                    hide();
                    break;
            }
        }
    };

    public AKeyboardButtonPreview(@NotNull View popupParent) {
        this.popupParent = popupParent;
    }

    public void createPreviewView(@NotNull LayoutInflater layoutInflater) {
        Log.d(TAG, "Creating preview view and popup window...");

        previewView = layoutInflater.inflate(R.layout.drag_keyboard_preview, null);
        previewView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        popup = new PopupWindow(previewView);
    }

    public void showPreview(@NotNull View view, @Nullable CharSequence text, @Nullable Integer drawableResId) {
        final int[] location = new int[2];
        view.getLocationInWindow(location);
        int x = location[0] + view.getWidth() / 2;
        int y = location[1];

        final PreviewParams previewParams;
        if (text == null) {
            if (drawableResId != null) {
                previewParams = PreviewParams.newDrawableInstance(x, y, drawableResId);
            } else {
                previewParams = PreviewParams.newTextInstance(x, y, "");
                Log.e(AKeyboardButtonPreview.class.getSimpleName(), "For view: " + view + " neither text nor drawable resource is specified!");
            }
        } else {
            previewParams = PreviewParams.newTextInstance(x, y, text.toString());
        }

		synchronized (handler) {
			// remove all 'remove' runnable for current params
			handler.removeMessages(MSG_REMOVE_PREVIEW, previewParams);
			handler.removeMessages(MSG_SHOW_PREVIEW, previewParams);

			handler.sendMessageDelayed(handler.obtainMessage(MSG_SHOW_PREVIEW, previewParams), DELAY_BEFORE_PREVIEW);
		}
	}

    private void showText0(@NotNull PreviewParams previewParams) {
		synchronized (handler) {
			handler.removeMessages(MSG_REMOVE_PREVIEW, previewParams);
		}

        final PopupWindow popup = this.popup;

        boolean image = false;

        final TextView previewTextView = (TextView) previewView.findViewById(R.id.preview_text_view);
        previewTextView.setText(previewParams.getText());

        final Integer drawableResId = previewParams.getDrawableResId();
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
        final int popupMargin = AndroidUtils.toPixels(popupParent.getContext().getResources().getDisplayMetrics(), 10);

        int popupX = previewParams.getX() - popupWidth / 2;
        int popupY = previewParams.getY() - popupHeight - popupMargin;

        if (popup.isShowing()) {
            popup.update(popupX, popupY, popupWidth, popupHeight);
        } else {
            popup.setWidth(popupWidth);
            popup.setHeight(popupHeight);
            popup.showAtLocation(popupParent, Gravity.NO_GRAVITY, popupX, popupY);
        }

        previewView.setVisibility(View.VISIBLE);

		synchronized (handler) {
			handler.sendMessageDelayed(handler.obtainMessage(MSG_REMOVE_PREVIEW, previewParams), DELAY_AFTER_PREVIEW);
		}
	}

	public void hide() {
        if (previewView != null) {
            previewView.setVisibility(View.INVISIBLE);
        }
    }
}
