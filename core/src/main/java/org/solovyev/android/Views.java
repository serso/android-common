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

package org.solovyev.android;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.*;
import android.widget.TabHost;
import android.widget.TextView;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * User: serso
 * Date: 2/2/13
 * Time: 5:57 PM
 */
public final class Views {

    private static final boolean AT_LEAST_API_5 = Build.VERSION.SDK_INT >= 5;

    private Views() {
        throw new AssertionError();
    }

    /**
     * Method runs through view and all it's children recursively and process them via viewProcessor
     *
     * @param view          parent view to be processed, if view is ViewGroup then all it's children will be processed
     * @param viewProcessor object which processes views
     */
    public static void processViews(@NotNull View view, @NotNull ViewProcessor<View> viewProcessor) {
        processViewsOfType0(view, null, viewProcessor);
    }

    static <T> void processViewsOfType0(@NotNull View view, @Nullable Class<T> viewClass, @NotNull ViewProcessor<T> viewProcessor) {
        if (view instanceof ViewGroup) {
            final ViewGroup viewGroup = (ViewGroup) view;

            if (viewClass == null || viewClass.isAssignableFrom(ViewGroup.class)) {
                //noinspection unchecked
                viewProcessor.process((T) viewGroup);
            }

            for (int index = 0; index < viewGroup.getChildCount(); index++) {
                processViewsOfType0(viewGroup.getChildAt(index), viewClass, viewProcessor);
            }
        } else if (viewClass == null || viewClass.isAssignableFrom(view.getClass())) {
            //noinspection unchecked
            viewProcessor.process((T) view);
        }
    }

    /**
     * Method runs through view and all it's children recursively and process all instances of viewClass via viewProcessor
     *
     * @param view          parent view to be processed, if view is ViewGroup then all it's children will be processed
     * @param viewClass     only instances of specified class will be processed
     * @param viewProcessor object which processes views
     */
    public static <T> void processViewsOfType(@NotNull View view, @NotNull Class<T> viewClass, @NotNull ViewProcessor<T> viewProcessor) {
        processViewsOfType0(view, viewClass, viewProcessor);
    }

    /**
     * Method center the tabs' contents on specified tabHost elements.
     * This method should be invoked only for tabs with only text on them (and no image)
     * This method checks some known devices/android versions/builds which don't support tab centering and do nothing for them
     * <p/>
     * NOTE: be aware that this method doesn't cover all unsupported cases, for sure don't use this method
     *
     * @param tabHost tabHost element
     */
    public static void centerAndWrapTabsFor(@NotNull TabHost tabHost) {
        if (allowCenterAndWrappingTabs()) {
            int tabCount = tabHost.getTabWidget().getTabCount();
            for (int i = 0; i < tabCount; i++) {
                final View view = tabHost.getTabWidget().getChildTabViewAt(i);
                if (view != null) {

                    if (view.getLayoutParams().height > 0) {
                        // reduce height of the tab
                        view.getLayoutParams().height *= 0.8;
                    }

                    //  get title text view
                    final View textView = view.findViewById(android.R.id.title);
                    if (textView instanceof TextView) {
                        // just in case check the type

                        // center text
                        ((TextView) textView).setGravity(Gravity.CENTER);
                        // wrap text
                        ((TextView) textView).setSingleLine(false);

                        // explicitly set layout parameters
                        textView.getLayoutParams().height = ViewGroup.LayoutParams.FILL_PARENT;
                        textView.getLayoutParams().width = ViewGroup.LayoutParams.WRAP_CONTENT;
                    }
                }
            }
        }
    }

    /**
     * Internal method where checking if device supports centering of text tabs
     *
     * @return true if centering of text tabs is supported for this device/build/OS version
     */
    private static boolean allowCenterAndWrappingTabs() {
        boolean result = true;

        String deviceModel = Build.MODEL;
        if (deviceModel != null) {
            deviceModel = deviceModel.toUpperCase();
            if (deviceModel.contains("M1") || deviceModel.contains("MIONE") || deviceModel.contains("MI-ONE")) {
                // Xiaomi Phone MiOne => do not allow to center and wrap tabs
                result = false;
                Log.i(AndroidUtils.class.getName(), "Device model doesn't support center and wrap of tabs: " + Build.MODEL);
            }
        }

        if (result) {
            String buildId = Build.DISPLAY;
            if (buildId != null) {
                buildId = buildId.toUpperCase();
                if (buildId.contains("MIUI")) {
                    // fix for MIUI ROM
                    result = false;
                    Log.i(AndroidUtils.class.getName(), "Device build doesn't support center and wrap of tabs: " + Build.DISPLAY);
                }
            }
        }

        return result;
    }

    /**
     * Method adds tab to the tabHost element
     *
     * @param context       activity which users tabHost
     * @param tabHost       tabHost element
     * @param tabId         id of tab to be added
     * @param tabCaptionId  string id of tab to be added
     * @param activityClass activity class to be invoked if the tab is pressed
     */
    public static void addTab(@NotNull Context context,
                              @NotNull TabHost tabHost,
                              @NotNull String tabId,
                              int tabCaptionId,
                              @NotNull Class<? extends Activity> activityClass) {

        // create intent to be invoked on tab press
        final Intent intent = new Intent().setClass(context, activityClass);

        // init TabSpec
        final TabHost.TabSpec tabSpec = tabHost.newTabSpec(tabId).setIndicator(context.getString(tabCaptionId)).setContent(intent);

        tabHost.addTab(tabSpec);
    }

    public static int toPixels(@NotNull DisplayMetrics dm, float dps) {
        final float scale = dm.density;
        return (int) (dps * scale + 0.5f);
    }

    public static int getScreenOrientation(@NotNull Activity activity) {
        final Display display = activity.getWindowManager().getDefaultDisplay();

        final int result;

        if (display.getWidth() <= display.getHeight()) {
            result = Configuration.ORIENTATION_PORTRAIT;
        } else {
            result = Configuration.ORIENTATION_LANDSCAPE;
        }

        return result;
    }

    // copied from API-15
    public static boolean isLayoutSizeAtLeast(int size, @NotNull Configuration configuration) {
        int cur = configuration.screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK;
        if (cur == Configuration.SCREENLAYOUT_SIZE_UNDEFINED) return false;
        return cur >= size;
    }

    public static int getPointerCountFromMotionEvent(@NotNull MotionEvent event) {
        return AT_LEAST_API_5 ? event.getPointerCount() : 1;
    }

    public static float getXFromMotionEvent(@NotNull MotionEvent event, int pointer) {
        return AT_LEAST_API_5 ? event.getX(pointer) : 0;
    }

    public static float getYFromMotionEvent(@NotNull MotionEvent event, int pointer) {
        return AT_LEAST_API_5 ? event.getY(pointer) : 0;
    }

    /**
     * Interface to process view. See AndroidUtils#processViews(android.view.View, AndroidUtils.ViewProcessor<android.view.View>) for more details
     *
     * @param <V> view type
     * @see Views#processViews(android.view.View, ViewProcessor
     */
    public static interface ViewProcessor<V> {
        void process(@NotNull V view);
    }
}
