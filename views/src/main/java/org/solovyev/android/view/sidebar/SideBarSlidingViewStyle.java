package org.solovyev.android.view.sidebar;

import javax.annotation.Nonnull;

enum SideBarSlidingViewStyle {
	hover(0),
	push(1);

	private final int id;

	SideBarSlidingViewStyle(int id) {
		this.id = id;
	}

	@Nonnull
	public static SideBarSlidingViewStyle getById(int id) {
		for (SideBarSlidingViewStyle style : SideBarSlidingViewStyle.values()) {
			if (style.id == id) {
				return style;
			}
		}

		throw new IllegalArgumentException("Id " + id + " is not supported");
	}
}
