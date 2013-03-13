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

package org.solovyev.android.properties;

import android.os.Parcel;
import org.solovyev.common.JObject;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * User: serso
 * Date: 5/30/12
 * Time: 7:17 PM
 */
final class APropertyImpl extends JObject implements AProperty {

    /*
    **********************************************************************
    *
    *                           STATIC
    *
    **********************************************************************
    */

    public static Creator<APropertyImpl> CREATOR = new Creator<APropertyImpl>() {
        @Override
        public APropertyImpl createFromParcel(@Nonnull Parcel in) {
            return fromParcel(in);
        }

        @Override
        public APropertyImpl[] newArray(int size) {
            return new APropertyImpl[size];
        }
    };

    /*
    **********************************************************************
    *
    *                           FIELDS
    *
    **********************************************************************
    */

    @Nonnull
    private String name;

    @Nullable
    private String value;

    /*
    **********************************************************************
    *
    *                           CONSTRUCTORS
    *
    **********************************************************************
    */

    private APropertyImpl() {
    }

    private APropertyImpl(@Nonnull String name, @Nullable String value) {
        this.name = name;
        this.value = value;
    }

    @Nonnull
    private static APropertyImpl fromParcel(@Nonnull Parcel in) {
        final String name = in.readString();
        final String value = in.readString();
        return (APropertyImpl)newInstance(name, value);
    }

    @Nonnull
    public static AProperty newInstance(@Nonnull String name, @Nullable String value) {
        return new APropertyImpl(name, value);
    }

    /*
    **********************************************************************
    *
    *                           METHODS
    *
    **********************************************************************
    */

    @Nonnull
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
    public void writeToParcel(@Nonnull Parcel out, int flags) {
        out.writeString(name);
        out.writeString(value);
    }

    @Nonnull
    @Override
    public APropertyImpl clone() {
        return (APropertyImpl) super.clone();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final APropertyImpl that = (APropertyImpl) o;

        if (!name.equals(that.name)) return false;
        if (value != null ? !value.equals(that.value) : that.value != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }
}
