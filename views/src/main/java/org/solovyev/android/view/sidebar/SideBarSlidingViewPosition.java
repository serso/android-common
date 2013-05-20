package org.solovyev.android.view.sidebar;

import javax.annotation.Nonnull;

enum SideBarSlidingViewPosition {

	left(0),
	top(1),
	right(2),
	bottom(3);

	private final int id;

	SideBarSlidingViewPosition(int id) {
		this.id = id;
	}

	@Nonnull
	public static SideBarSlidingViewPosition getById(int id) {
		for (SideBarSlidingViewPosition slidingViewPosition : values()) {
			if (slidingViewPosition.id == id) {
				return slidingViewPosition;
			}
		}

		throw new IllegalArgumentException("Sliding view position with id: " + id + " could not be found!");
	}
}
