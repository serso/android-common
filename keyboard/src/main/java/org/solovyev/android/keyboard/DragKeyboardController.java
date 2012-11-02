package org.solovyev.android.keyboard;

import android.content.Context;
import android.inputmethodservice.KeyboardView;
import android.view.LayoutInflater;
import android.view.inputmethod.EditorInfo;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * User: Solovyev_S
 * Date: 02.11.12
 * Time: 14:42
 */
public class DragKeyboardController extends AbstractKeyboardController {

	@NotNull
	@Override
	protected AKeyboardView createKeyboardView0(@NotNull Context context, @NotNull LayoutInflater layoutInflater) {
		return new DragAKeyboardView((KeyboardView)layoutInflater.inflate(R.layout.input, null), this, getInputMethodService());
	}

	@NotNull
	@Override
	public AKeyboardControllerState onStartInput0(@NotNull EditorInfo attribute, boolean restarting) {
		return AKeyboardControllerStateImpl.newDefaultState();
	}

	@Override
	public void onKey(int primaryCode, @Nullable int[] keyCodes) {
		//To change body of implemented methods use File | Settings | File Templates.
	}
}
