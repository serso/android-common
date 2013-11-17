package org.solovyev.android.fragments;

import android.support.v4.app.Fragment;
import org.solovyev.common.JPredicate;

import javax.annotation.Nonnull;

public class SimpleFragmentReuseCondition<F extends Fragment> extends AbstractFragmentReuseCondition<F> {

	private SimpleFragmentReuseCondition(@Nonnull Class<F> fragmentClass) {
		super(fragmentClass);
	}

	@Nonnull
	public static <F extends Fragment> JPredicate<Fragment> forClass(@Nonnull Class<F> fragmentClass) {
		return new SimpleFragmentReuseCondition<F>(fragmentClass);
	}

	@Override
	protected boolean canReuseFragment(@Nonnull F fragment) {
		return true;
	}

}
