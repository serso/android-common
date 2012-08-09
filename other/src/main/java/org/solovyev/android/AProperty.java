package org.solovyev.android;

import android.os.Parcelable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * User: serso
 * Date: 5/30/12
 * Time: 7:16 PM
 */
public interface AProperty extends Parcelable {

    @NotNull
    String getName();

    @Nullable
    String getValue();
}
