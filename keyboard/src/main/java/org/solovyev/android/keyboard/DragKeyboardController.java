package org.solovyev.android.keyboard;

import android.content.Context;
import android.inputmethodservice.InputMethodService;
import android.view.LayoutInflater;
import android.view.inputmethod.EditorInfo;
import android.widget.LinearLayout;
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
	protected AKeyboardView<DragAKeyboardDef> createKeyboardView0(@NotNull Context context, @NotNull LayoutInflater layoutInflater) {
		return new DragAKeyboardView(new LinearLayout(context), this, getInputMethodService());
	}

    @Override
    public void onInitializeInterface(@NotNull InputMethodService inputMethodService) {
        super.onInitializeInterface(inputMethodService);
        setCurrentKeyboard(new AKeyboardImpl<DragAKeyboardDef>("drag_keyboard", new DragAKeyboardDef()));
    }

    @NotNull
    @Override
    protected AKeyboardView<DragAKeyboardDef> createDefaultKeyboardView0() {
        return new DummyAKeyboardView<DragAKeyboardDef>();
    }

    @NotNull
	@Override
	public AKeyboardControllerState<DragAKeyboardDef> onStartInput0(@NotNull EditorInfo attribute, boolean restarting) {
		return AKeyboardControllerStateImpl.newDefaultState();
	}

	@Override
	public void onKey(int primaryCode, @Nullable int[] keyCodes) {
		//To change body of implemented methods use File | Settings | File Templates.
	}
}
