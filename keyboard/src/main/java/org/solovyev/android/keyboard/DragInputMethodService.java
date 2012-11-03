package org.solovyev.android.keyboard;

/**
 * User: serso
 * Date: 11/3/12
 * Time: 2:43 PM
 */
public class DragInputMethodService extends AbstractInputMethodService {

    public DragInputMethodService() {
        super(new CalculatorKeyboardController());
    }
}
