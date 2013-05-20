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

package org.solovyev.android.list;

import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.ListView;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * User: serso
 * Date: 4/15/12
 * Time: 10:34 PM
 */
public class ListAnimator {

	public static enum AnimationType {
		left_to_right {
			@Nonnull
			@Override
			public Animation create() {
				return new TranslateAnimation(
						Animation.RELATIVE_TO_SELF, 0.0f,
						Animation.RELATIVE_TO_SELF, -1.0f,
						Animation.RELATIVE_TO_SELF, 0.0f,
						Animation.RELATIVE_TO_SELF, 0.0f);
			}
		},
		right_to_left {
			@Nonnull
			@Override
			public Animation create() {
				return new TranslateAnimation(
						Animation.RELATIVE_TO_SELF, 0.0f,
						Animation.RELATIVE_TO_SELF, 1.0f,
						Animation.RELATIVE_TO_SELF, 0.0f,
						Animation.RELATIVE_TO_SELF, 0.0f);
			}
		},
		top_to_bottom {
			@Nonnull
			@Override
			public Animation create() {
				return new TranslateAnimation(
						Animation.RELATIVE_TO_SELF, 0.0f,
						Animation.RELATIVE_TO_SELF, 0.0f,
						Animation.RELATIVE_TO_SELF, 0.0f,
						Animation.RELATIVE_TO_SELF, 1.0f);
			}
		};

		@Nonnull
		public abstract Animation create();
	}

	@Nonnull
	private final ListView listView;

	@Nonnull
	private final AnimationType animationType;

	@Nullable
	private final Runnable postAction;

	public ListAnimator(@Nonnull ListView listView, @Nonnull AnimationType animationType, @Nullable Runnable postAction) {
		this.listView = listView;
		this.animationType = animationType;
		this.postAction = postAction;
	}

	public void animate() {

		final AnimationSet animationSet = new AnimationSet(false);

		final AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0.25f);
		alphaAnimation.setDuration(350);
		animationSet.addAnimation(alphaAnimation);

		final Animation moveAnimation = animationType.create();
		moveAnimation.setDuration(500);
		animationSet.addAnimation(moveAnimation);

		animationSet.setRepeatCount(0);

		if (postAction != null) {
			animationSet.setAnimationListener(new Animation.AnimationListener() {
				@Override
				public void onAnimationStart(Animation animation) {
				}

				@Override
				public void onAnimationEnd(Animation animation) {
					postAction.run();
				}

				@Override
				public void onAnimationRepeat(Animation animation) {
				}
			});
		}

		listView.startAnimation(animationSet);
	}
}
