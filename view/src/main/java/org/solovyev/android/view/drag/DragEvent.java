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

package org.solovyev.android.view.drag;

import android.view.MotionEvent;
import org.jetbrains.annotations.NotNull;
import org.solovyev.common.math.Point2d;

public class DragEvent {

	@NotNull
	private final Point2d startPoint;

	@NotNull
	private final MotionEvent motionEvent;

	public DragEvent(@NotNull Point2d startPoint, @NotNull MotionEvent motionEvent) {
		this.startPoint = startPoint;
		this.motionEvent = motionEvent;
	}

	/**
	 * @return motion event started at start point
	 */
	@NotNull
	public MotionEvent getMotionEvent() {
		return motionEvent;
	}

	/**
	 * @return start point of dragging
	 */
	@NotNull
	public Point2d getStartPoint() {
		return startPoint;
	}


}
