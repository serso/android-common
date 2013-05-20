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

import android.os.Parcel;
import android.os.Parcelable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public final class PreviewParams implements Parcelable {

	@Nonnull
	public static final Creator<PreviewParams> CREATOR = new Creator<PreviewParams>() {

		@Override
		public PreviewParams createFromParcel(@Nonnull Parcel in) {
			return fromParcel(in);
		}

		@Override
		public PreviewParams[] newArray(int size) {
			return new PreviewParams[size];
		}
	};


	private int x;

	private int y;

	@Nullable
	private String text;

	@Nullable
	private Integer drawableResId;

	@Nonnull
	private static final Map<PreviewParams, PreviewParams> cache = new HashMap<PreviewParams, PreviewParams>();

	private PreviewParams(@Nonnull Parcel in) {
		this.x = in.readInt();
		this.y = in.readInt();
		this.text = in.readString();
		this.drawableResId = in.readInt();
	}

	private PreviewParams(int x, int y, @Nullable String text, @Nullable Integer drawableResId) {
		this.x = x;
		this.y = y;
		this.text = text;
		this.drawableResId = drawableResId;
	}

	@Nonnull
	public static PreviewParams newTextInstance(int x, int y, @Nonnull String text) {
		return fromCache(new PreviewParams(x, y, text, null));
	}

	@Nonnull
	public static PreviewParams newDrawableInstance(int x, int y, @Nonnull Integer drawableResId) {
		return fromCache(new PreviewParams(x, y, null, drawableResId));
	}

	@Nonnull
	private static PreviewParams fromParcel(@Nonnull Parcel in) {
		return fromCache(new PreviewParams(in));
	}

	@Nonnull
	private static PreviewParams fromCache(@Nonnull PreviewParams previewParams) {
		synchronized (cache) {
			final PreviewParams fromCache = cache.get(previewParams);
			if (fromCache != null) {
				synchronized (fromCache) {
					fromCache.x = previewParams.x;
					fromCache.y = previewParams.y;
				}
				return fromCache;
			} else {
				cache.put(previewParams, previewParams);
				return previewParams;
			}
		}
	}

	public int getX() {
		synchronized (this) {
			return x;
		}
	}

	public int getY() {
		synchronized (this) {
			return y;
		}
	}

	@Nullable
	public String getText() {
		return text;
	}

	@Nullable
	public Integer getDrawableResId() {
		return drawableResId;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(@Nonnull Parcel out, int flags) {
		out.writeInt(x);
		out.writeInt(y);
		out.writeString(text);
		out.writeInt(drawableResId == null ? 0 : drawableResId);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof PreviewParams)) return false;

		PreviewParams previewParams = (PreviewParams) o;

		if (drawableResId != null ? !drawableResId.equals(previewParams.drawableResId) : previewParams.drawableResId != null)
			return false;
		if (text != null ? !text.equals(previewParams.text) : previewParams.text != null) return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = text != null ? text.hashCode() : 0;
		result = 31 * result + (drawableResId != null ? drawableResId.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "PreviewParams{" +
				"x=" + x +
				", y=" + y +
				", text='" + text + '\'' +
				", drawableResId=" + drawableResId +
				'}';
	}
}
