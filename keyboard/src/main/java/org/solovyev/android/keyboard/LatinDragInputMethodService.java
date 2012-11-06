package org.solovyev.android.keyboard;

/**
 * User: serso
 * Date: 11/4/12
 * Time: 12:05 AM
 */
public class LatinDragInputMethodService extends AbstractAKeyboardInputMethodService {

    public LatinDragInputMethodService() {
        super(new LatinDragKeyboardController());
    }
}
