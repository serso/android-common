package org.solovyev.android;

import android.os.Parcelable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.solovyev.common.JCloneable;

import java.io.Serializable;

/**
 * User: serso
 * Date: 5/30/12
 * Time: 7:16 PM
 */
public interface AProperty extends Parcelable, JCloneable<AProperty>, Serializable {

    @NotNull
    String getName();

    @Nullable
    String getValue();
}
