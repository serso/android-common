package org.solovyev.android.fragments;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.TextView;
import org.solovyev.android.Views;
import org.solovyev.common.Builder;
import org.solovyev.common.JPredicate;
import org.solovyev.common.text.Strings;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * User: serso
 * Date: 8/17/13
 * Time: 10:54 PM
 */
public class SimpleMultiPaneFragmentManager extends MultiPaneFragmentManager {

	public SimpleMultiPaneFragmentManager(@Nonnull FragmentActivity activity, @Nonnull Class<? extends Fragment> emptyFragmentClass) {
		super(activity, R.id.acl_content_first_pane, emptyFragmentClass, "empty-fragment");
	}

	public void setSecondFragment(@Nonnull Class<? extends Fragment> fragmentClass,
								  @Nullable Bundle fragmentArgs,
								  @Nullable JPredicate<Fragment> reuseCondition,
								  @Nonnull String fragmentTag,
								  boolean addToBackStack) {
		setFragment(R.id.acl_content_second_pane, MultiPaneFragmentDef.newInstance(fragmentTag, addToBackStack, ReflectionFragmentBuilder.forClass(getActivity(), fragmentClass, fragmentArgs), reuseCondition));
	}

	public void setSecondFragment(@Nonnull Builder<Fragment> fragmentBuilder,
								  @Nullable JPredicate<Fragment> reuseCondition,
								  @Nonnull String fragmentTag) {
		setFragment(R.id.acl_content_second_pane, MultiPaneFragmentDef.newInstance(fragmentTag, false, fragmentBuilder, reuseCondition));
	}

	public void emptifySecondFragment() {
		emptifyFragmentPane(R.id.acl_content_second_pane);
	}

	public boolean isDualPane() {
		return getActivity().findViewById(R.id.acl_content_second_pane) != null;
	}

	public boolean isTriplePane() {
		return getActivity().findViewById(R.id.acl_content_third_pane) != null;
	}

	public boolean isFirstPane(@Nullable View parent) {
		return parent != null && parent.getId() == R.id.acl_content_first_pane;
	}

	public boolean isSecondPane(@Nullable View parent) {
		return parent != null && parent.getId() == R.id.acl_content_second_pane;
	}

	public boolean isThirdPane(@Nullable View parent) {
		return parent != null && parent.getId() == R.id.acl_content_third_pane;
	}

	public void onCreateView(@Nullable View paneParent, @Nonnull View pane) {
		final Activity activity = getActivity();

		if (this.isDualPane()) {
			if (this.isFirstPane(paneParent)) {
				pane.setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.acl_border_right));
				// border may add padding => set to zeros
				pane.setPadding(0, 0, 0, 0);
			} else if (this.isSecondPane(paneParent)) {
				pane.setBackgroundColor(activity.getResources().getColor(R.color.acl_bg));
			} else if (this.isTriplePane() && this.isThirdPane(paneParent)) {
				if (Views.getScreenOrientation(activity) == Configuration.ORIENTATION_LANDSCAPE) {
					pane.setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.acl_border_left));
				} else {
					pane.setBackgroundColor(activity.getResources().getColor(R.color.acl_bg));
				}
			}
		} else {
			pane.setBackgroundColor(activity.getResources().getColor(R.color.acl_bg));
		}

		final TextView fragmentTitleTextView = (TextView) pane.findViewById(R.id.acl_fragment_title);
		if (fragmentTitleTextView != null) {
			if (this.isDualPane()) {
				final CharSequence fragmentTitle = fragmentTitleTextView.getText();
				if (Strings.isEmpty(fragmentTitle)) {
					fragmentTitleTextView.setVisibility(View.GONE);
				} else {
					fragmentTitleTextView.setText(String.valueOf(fragmentTitle).toUpperCase());
					fragmentTitleTextView.setVisibility(View.VISIBLE);
				}
			} else {
				fragmentTitleTextView.setVisibility(View.GONE);
			}
		}
	}
}
