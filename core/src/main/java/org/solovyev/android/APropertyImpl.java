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
 * ---------------------------------------------------------------------
 * Contact details
 *
 * Email: se.solovyev@gmail.com
 * Site:  http://se.solovyev.org
 */

package org.solovyev.android;

import android.os.Parcel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.solovyev.common.JObject;

/**
 * User: serso
 * Date: 5/30/12
 * Time: 7:17 PM
 */
public class APropertyImpl extends JObject implements AProperty {

    @NotNull
    private String name;

    @Nullable
    private String value;

    public APropertyImpl() {
    }

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

    @NotNull
    @Override
    public APropertyImpl clone() {
        return (APropertyImpl) super.clone();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof APropertyImpl)) return false;

        APropertyImpl aProperty = (APropertyImpl) o;

        if (!name.equals(aProperty.name)) return false;
        if (value != null ? !value.equals(aProperty.value) : aProperty.value != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }
}
