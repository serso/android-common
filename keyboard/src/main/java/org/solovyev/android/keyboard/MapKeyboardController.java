package org.solovyev.android.keyboard;

import android.content.Context;
import android.inputmethodservice.InputMethodService;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: serso
 * Date: 11/1/12
 * Time: 8:18 PM
 */
public abstract class MapKeyboardController extends AbstractKeyboardController {

	@NotNull
	private final Map<String, AKeyboard> keyboards = new HashMap<String, AKeyboard>();

    @NotNull
    @Override
    protected AKeyboardControllerState onInitializeInterface0(@NotNull InputMethodService inputMethodService) {
        synchronized (this.keyboards) {

            this.keyboards.clear();

            final List<AKeyboard> keyboards = createKeyboard(inputMethodService);
            for (AKeyboard keyboard : keyboards) {
                this.keyboards.put(keyboard.getKeyboard().getKeyboardId(), keyboard);
            }

            return AKeyboardControllerStateImpl.newDefaultState(this.keyboards.get(getDefaultKeyboardId()));
        }
    }

    @NotNull
	protected abstract List<AKeyboard> createKeyboard(@NotNull Context context);

	@NotNull
	protected abstract String getDefaultKeyboardId();
}
