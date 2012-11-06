package org.solovyev.android.keyboard;

import android.os.Parcel;
import android.os.Parcelable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public final class PreviewParams implements Parcelable {

	@NotNull
	public static final Creator<PreviewParams> CREATOR = new Creator<PreviewParams>() {

		@Override
		public PreviewParams createFromParcel(@NotNull Parcel in) {
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

	@NotNull
	private static final Map<PreviewParams, PreviewParams> cache = new HashMap<PreviewParams, PreviewParams>();

	private PreviewParams(@NotNull Parcel in) {
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

	@NotNull
	public static PreviewParams newTextInstance(int x, int y, @NotNull String text) {
		return fromCache(new PreviewParams(x, y, text, null));
	}

	@NotNull
	public static PreviewParams newDrawableInstance(int x, int y, @NotNull Integer drawableResId) {
		return fromCache(new PreviewParams(x, y, null, drawableResId));
	}

	@NotNull
	private static PreviewParams fromParcel(@NotNull Parcel in) {
		return fromCache(new PreviewParams(in));
	}

	@NotNull
	private static PreviewParams fromCache(@NotNull PreviewParams previewParams) {
		synchronized (cache) {
			final PreviewParams fromCache = cache.get(previewParams);
			if (fromCache != null) {
				return fromCache;
			} else {
				cache.put(previewParams, previewParams);
				return previewParams;
			}
		}
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
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
	public void writeToParcel(@NotNull Parcel out, int flags) {
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
}
