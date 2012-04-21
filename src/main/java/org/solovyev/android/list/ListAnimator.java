package org.solovyev.android.list;

import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.ListView;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * User: serso
 * Date: 4/15/12
 * Time: 10:34 PM
 */
public class ListAnimator {

    public static enum AnimationType {
        left_to_right {
            @NotNull
            @Override
            public Animation create() {
                return new TranslateAnimation(
                        Animation.RELATIVE_TO_SELF, 0.0f,
                        Animation.RELATIVE_TO_SELF, -1.0f,
                        Animation.RELATIVE_TO_SELF, 0.0f,
                        Animation.RELATIVE_TO_SELF, 0.0f);
            }
        },
        right_to_left{
            @NotNull
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
            @NotNull
            @Override
            public Animation create() {
                return new TranslateAnimation(
                        Animation.RELATIVE_TO_SELF, 0.0f,
                        Animation.RELATIVE_TO_SELF, 0.0f,
                        Animation.RELATIVE_TO_SELF, 0.0f,
                        Animation.RELATIVE_TO_SELF, 1.0f);
            }
        };

        @NotNull
        public abstract Animation create();
    }

    @NotNull
    private final ListView listView;

    @NotNull
    private final AnimationType animationType;

    @Nullable
    private final Runnable postAction;

    public ListAnimator(@NotNull ListView listView, @NotNull AnimationType animationType, @Nullable Runnable postAction) {
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
