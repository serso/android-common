package org.solovyev.android;

import android.os.Parcel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * User: serso
 * Date: 5/30/12
 * Time: 7:17 PM
 */
public class APropertyImpl implements AProperty {

    @NotNull
    private String name;

    @Nullable
    private String value;

    private APropertyImpl(@NotNull String name, String value) {
        this.name = name;
        this.value = value;
    }

    @NotNull
    public static AProperty fromParcel(@NotNull Parcel in) {
        final String name = in.readString();
        final String value = in.readString();
        return newInstance(name, value);
    }

    @NotNull
    public static AProperty newInstance(@NotNull String name, @Nullable String value) {
        return new APropertyImpl(name, value);
    }

    @NotNull
    @Override
    public String getName() {
        return this.name;
    }

    @Nullable
    @Override
    public String getValue() {
        return this.value;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NotNull Parcel out, int flags) {
        out.writeString(name);
        out.writeString(value);
    }
}
