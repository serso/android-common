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
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Parcelable;
import android.util.Log;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

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

    public static boolean isPhoneModel(@NotNull APhoneModel phoneModel) {
        final String model = Build.MODEL;
        return model != null && phoneModel.getModels().contains(model);
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

    @NotNull
    public static Parcelable.Creator<String> getStringParcelableCreator() {
        return StringParcelableCreator.getInstance();
    }

    public static void addIntentFlags(@NotNull Intent intent, boolean detached, @NotNull Context context) {
        int flags = 0;

        if (!(context instanceof Activity)) {
            flags = flags | Intent.FLAG_ACTIVITY_NEW_TASK;
        }

        if (detached) {
            flags = flags | Intent.FLAG_ACTIVITY_NO_HISTORY;
        }

        intent.setFlags(flags);

    }

    public static void toggleComponent(@NotNull Context context,
                                       @NotNull Class<? extends Context> componentClass,
                                       boolean enable) {
        final PackageManager pm = context.getPackageManager();

        final int componentState;
        if (enable) {
            componentState = PackageManager.COMPONENT_ENABLED_STATE_ENABLED;
        } else {
            componentState = PackageManager.COMPONENT_ENABLED_STATE_DISABLED;
        }

        pm.setComponentEnabledSetting(new ComponentName(context, componentClass), componentState, PackageManager.DONT_KILL_APP);
    }

    public static boolean isComponentEnabled(@NotNull Context context,
                                             @NotNull Class<? extends Context> componentClass) {
        final PackageManager pm = context.getPackageManager();

        int componentEnabledSetting = pm.getComponentEnabledSetting(new ComponentName(context, componentClass));
        return componentEnabledSetting == PackageManager.COMPONENT_ENABLED_STATE_ENABLED || componentEnabledSetting == PackageManager.COMPONENT_ENABLED_STATE_DEFAULT;
    }

    public static String saveBitmap(@NotNull Bitmap bitmap,
                                    @NotNull String path,
                                    @NotNull String fileName) {
        final File filePath = new File(path);
        filePath.mkdirs();

        final File file = new File(path, fileName);
        if (!file.exists()) {
            final String name = file.getAbsolutePath();

            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(name);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                fos.flush();
            } catch (FileNotFoundException e) {
                Log.e("AndroidUtils", e.getMessage(), e);
            } catch (IOException e) {
                Log.e("AndroidUtils", e.getMessage(), e);
            } finally {
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        Log.e("AndroidUtils", e.getMessage(), e);
                    }
                }
            }

            return name;
        }

        return null;
    }

}

