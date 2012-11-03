package org.solovyev.android.keyboard;

import android.content.Context;
import android.inputmethodservice.InputMethodService;
import android.view.inputmethod.EditorInfo;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * User: Solovyev_S
 * Date: 02.11.12
 * Time: 14:42
 */
public class DragKeyboardController extends AbstractKeyboardController<DragAKeyboardDef> {

    @NotNull
	@Override
	protected AKeyboardView<DragAKeyboardDef> createKeyboardView0(@NotNull Context context) {
		return new DragAKeyboardView(this, getInputMethodService());
	}

    @NotNull
    @Override
    protected AKeyboardControllerState<DragAKeyboardDef> onInitializeInterface0(@NotNull InputMethodService inputMethodService) {
        return AKeyboardControllerStateImpl.newDefaultState(new AKeyboardImpl<DragAKeyboardDef>("drag_keyboard", new DragAKeyboardDef()));
    }

    @NotNull
	@Override
	public AKeyboardControllerState<DragAKeyboardDef> onStartInput0(@NotNull EditorInfo attribute, boolean restarting) {
		return getState();
	}

	@Override
	public void onKey(int primaryCode, @Nullable int[] keyCodes) {
		//To change body of implemented methods use File | Settings | File Templates.
	}
}
