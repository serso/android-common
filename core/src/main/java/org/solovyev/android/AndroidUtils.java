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

import android.app.*;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.*;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TextView;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

/**
 * User: serso
 * Date: 12/21/11
 * Time: 11:54 PM
 */

/**
 * This class contains static methods for working with some android classes
 */
@SuppressWarnings("UnusedDeclaration")
public final class AndroidUtils {

    // not intended for instantiation
	private AndroidUtils() {
		throw new AssertionError();
	}

    /*
    **********************************************************************
    *
    *                           CONSTANTS
    *
    **********************************************************************
    */

    public static final String LINE_SEPARATOR = System.getProperty("line.separator");


    /*
    **********************************************************************
    *
    *                           STATIC FIELDS
    *
    **********************************************************************
    */

    /*@Nullable*/
    private static Boolean debug = null;

    /*
    **********************************************************************
    *
    *                           STATIC METHODS
    *
    **********************************************************************
    */

	/**
	 * Method center the tabs' contents on specified tabHost elements.
	 * This method should be invoked only for tabs with only text on them (and no image)
	 * This method checks some known devices/android versions/builds which don't support tab centering and do nothing for them
	 *
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
	 * @param context activity which users tabHost
	 * @param tabHost tabHost element
	 * @param tabId id of tab to be added
	 * @param tabCaptionId string id of tab to be added
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


	/**
	 * Method returns version of current application.
	 *
	 * @param context	context
	 * @param appPackageName full name of the package of an app, 'com.example.app' for example.
	 * @return version number we are currently in, if for some reason package info was not found and thus versionCode could not be found -1 is returned
	 */
	public static int getAppVersionCode(@NotNull Context context, @NotNull String appPackageName) {
		try {
			return context.getPackageManager().getPackageInfo(appPackageName, 0).versionCode;
		} catch (PackageManager.NameNotFoundException e) {
			// App not installed!
		}
		return -1;
	}

		/**
	 * Method runs through view and all it's children recursively and process all instances of viewClass via viewProcessor
	 * @param view parent view to be processed, if view is ViewGroup then all it's children will be processed
	 * @param viewClass only instances of specified class will be processed
	 * @param viewProcessor object which processes views
	 */
	public static <T> void processViewsOfType(@NotNull View view, @NotNull Class<T> viewClass, @NotNull ViewProcessor<T> viewProcessor) {
		processViewsOfType0(view, viewClass, viewProcessor);
	}

	/**
	 * Method runs through view and all it's children recursively and process them via viewProcessor
	 * @param view parent view to be processed, if view is ViewGroup then all it's children will be processed
	 * @param viewProcessor object which processes views
	 */
	public static void processViews(@NotNull View view, @NotNull ViewProcessor<View> viewProcessor) {
		processViewsOfType0(view, null, viewProcessor);
	}

	private static <T> void processViewsOfType0(@NotNull View view, @Nullable Class<T> viewClass, @NotNull ViewProcessor<T> viewProcessor) {
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

    @NotNull
    public static Handler newUiHandler() {
        if ( !AndroidUtils.isUiThread() ) {
            throw new AssertionError("Must be called on UI thread!");
        }

        return new Handler();
    }

    /**
	 * Interface to process view. See AndroidUtils#processViews(android.view.View, AndroidUtils.ViewProcessor<android.view.View>) for more details
	 *
	 * @see AndroidUtils#processViews(android.view.View, AndroidUtils.ViewProcessor)
	 * @param <V> view type
	 */
	public static interface ViewProcessor<V> {
		void process(@NotNull V view);
	}

	/**
	 * Method restarts activity
	 * @param activity to be restarted activity
	 */
	public static void restartActivity(@NotNull Activity activity) {
		final Intent intent = activity.getIntent();
		/*
		for compatibility with android_1.6_compatibility
		overridePendingTransition(0, 0);
		intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);*/

		Log.d(activity.getClass().getName(), "Finishing current activity!");
		activity.finish();

		/*
		for compatibility with android_1.6_compatibility

		overridePendingTransition(0, 0);*/
		Log.d(activity.getClass().getName(), "Starting new activity!");
		activity.startActivity(intent);
	}

	public static int toPixels(@NotNull DisplayMetrics dm, float dps) {
		final float scale = dm.density;
		return (int) (dps * scale + 0.5f);
	}

	public static enum PhoneModel {
		samsung_galaxy_s_2(	"GT-I9100","GT-I9100M","GT-I9100P","GT-I9100T","SC-02C","SHW-M250K","SHW-M250L","SHW-M250S"),
		samsung_galaxy_s("GT-I9000","GT-I9000B","GT-I9000M","GT-I9000T","SGH-I897");

		@NotNull
		private final List<String> values;

		PhoneModel(@NotNull String... values) {
			this.values = Arrays.asList(values);
		}

		@NotNull
		public List<String> getValues() {
			return values;
		}
	}

	public static boolean isPhoneModel( @NotNull PhoneModel phoneModel ) {
		final String model = Build.MODEL;
		return model != null && phoneModel.getValues().contains(model);
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

    public static boolean isDebuggable(@NotNull Context context) {
        if (debug == null) {
            debug = 0 != (context.getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE);
        }
        return debug;
    }

    public static void showDialog(@NotNull DialogFragment dialogFragment,
                                  @NotNull String fragmentTag,
                                  @NotNull FragmentManager fm) {
        final FragmentTransaction ft = fm.beginTransaction();

        Fragment prev = fm.findFragmentByTag(fragmentTag);
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        // Create and show the dialog.
        dialogFragment.show(ft, fragmentTag);
    }

    public static boolean isUiThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }

    @NotNull
    public static Parcelable.Creator<String> getStringParcelableCreator() {
        return StringParcelableCreator.getInstance();
    }

    private static final class StringParcelableCreator implements Parcelable.Creator<String> {

        @NotNull
        private final static Parcelable.Creator<String> instance = new StringParcelableCreator();

        @NotNull
        public static Parcelable.Creator<String> getInstance() {
            return instance;
        }

        private StringParcelableCreator() {
        }

        @Override
        public String createFromParcel(Parcel in) {
            return in.readString();
        }

        @Override
        public String[] newArray(int size) {
            return new String[size];
        }
    }
}

