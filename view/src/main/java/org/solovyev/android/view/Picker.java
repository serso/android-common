/*
 * Copyright (c) 2009-2011. Created by serso aka se.solovyev.
 * For more information, please, contact se.solovyev@gmail.com
 * or visit http://se.solovyev.org
 */

package org.solovyev.android.view;

/**
 * User: serso
 * Date: 9/18/11
 * Time: 10:03 PM
 */
/*
 * Copyright (C) 2008 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A view for selecting a number
 * <p/>
 * For a dialog using this view, see {@link android.app.TimePickerDialog}.
 */
public class Picker extends LinearLayout {

    public static interface OnChangedListener {
        void onChanged(@NotNull Picker picker, @NotNull Object value);
    }

    public static interface Range {

        int getStartPosition();

        int getCount();

        @NotNull
        String getStringValueAt(int position);

        @NotNull
        Object getValueAt(int position);
    }


    @NotNull
    private final Handler handler;

    @NotNull
    private final Runnable runnable = new Runnable() {
        public void run() {
            if (increment) {
                changeCurrent(current + 1);
                handler.postDelayed(this, speed);
            } else if (decrement) {
                changeCurrent(current - 1);
                handler.postDelayed(this, speed);
            }
        }
    };

    @NotNull
    private final TextView text;

    @NotNull
    private Range range;

    /**
     * Current value of this NumberPicker
     */
    private int current;

    /**
     * Previous value of this NumberPicker.
     */
    private int previous;

    @Nullable
    private OnChangedListener onChangedListener;

    private long speed = 300;

    private boolean increment;

    private boolean decrement;

    @NotNull
    private final PickerButton incrementButton;

    @NotNull
    private final PickerButton decrementButton;

    /**
     * Create a new number picker
     *
     * @param context the application environment
     */
    public Picker(Context context) {
        this(context, null);
    }

    /**
     * Create a new number picker
     *
     * @param context the application environment
     * @param attrs   a collection of attributes
     */
    public Picker(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        setOrientation(VERTICAL);

        // INFLATING LAYOUT
        final LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.number_picker, this, true);

        handler = new Handler();

        final OnClickListener clickListener = new OnClickListener() {
            public void onClick(View v) {
                // now perform the increment/decrement
                if (R.id.increment == v.getId()) {
                    changeCurrent(current + 1);
                } else if (R.id.decrement == v.getId()) {
                    changeCurrent(current - 1);
                }
            }
        };

        final OnLongClickListener longClickListener = new OnLongClickListener() {
            /**
             * We start the long click here but rely on the {@link PickerButton}
             * to inform us when the long click has ended.
             */
            public boolean onLongClick(View v) {
                if (R.id.increment == v.getId()) {
                    increment = true;
                    handler.post(runnable);
                } else if (R.id.decrement == v.getId()) {
                    decrement = true;
                    handler.post(runnable);
                }
                return true;
            }
        };


        incrementButton = (PickerButton) this.findViewById(R.id.increment);
        incrementButton.setNumberPicker(this);
        incrementButton.setOnClickListener(clickListener);
        incrementButton.setOnLongClickListener(longClickListener);

        decrementButton = (PickerButton) this.findViewById(R.id.decrement);
        decrementButton.setNumberPicker(this);
        decrementButton.setOnClickListener(clickListener);
        decrementButton.setOnLongClickListener(longClickListener);

        text = (TextView) this.findViewById(R.id.timepicker_input);

        if (!isEnabled()) {
            setEnabled(false);
        }
    }

    /**
     * Set the enabled state of this view. The interpretation of the enabled
     * state varies by subclass.
     *
     * @param enabled True if this view is enabled, false otherwise.
     */
    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        incrementButton.setEnabled(enabled);
        decrementButton.setEnabled(enabled);
        text.setEnabled(enabled);
    }

    /**
     * Set the callback that indicates the number has been adjusted by the user.
     *
     * @param listener the callback, should not be null.
     */
    public void setOnChangeListener(OnChangedListener listener) {
        this.onChangedListener = listener;
    }

    public void setRange(@NotNull Range range) {
        this.range = range;
        this.current = range.getStartPosition();

        updateView();
    }

    public void setCurrent(int current) {
        if (current < 0 || current >= range.getCount()) {
            throw new IllegalArgumentException(
                    "Current: " + current + " should be >= 0 and < " + range.getCount());
        }
        this.current = current;
        updateView();
    }

    /**
     * Sets the speed at which the numbers will scroll when the +/-
     * buttons are longpressed
     *
     * @param speed The speed (in milliseconds) at which the numbers will scroll
     *              default 300ms
     */
    public void setSpeed(long speed) {
        this.speed = speed;
    }

    protected void changeCurrent(int current) {

        // Wrap around the values if we go past the start or end
        if (current < 0) {
            current = this.range.getCount() - 1;
        } else if (current >= this.range.getCount()) {
            current = 0;
        }

        this.previous = this.current;
        this.current = current;

        notifyChange();
        updateView();
    }

    private void notifyChange() {
        if (onChangedListener != null) {
            onChangedListener.onChanged(this, range.getValueAt(current));
        }
    }

    private void updateView() {
        text.setText(range.getStringValueAt(current));
    }

    /**
     * @hide
     */
    public void cancelIncrement() {
        increment = false;
    }

    /**
     * @hide
     */
    public void cancelDecrement() {
        decrement = false;
    }

    /**
     * @return current position in Picker
     */
    public int getCurrent() {
        return current;
    }
}