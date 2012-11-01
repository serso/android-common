package org.solovyev.android.keyboard;

/**
 * User: serso
 * Date: 11/1/12
 * Time: 8:09 PM
 */
public class LatinInputMethodService extends AbstractInputMethodService {

    public LatinInputMethodService() {
        super(new LatinKeyboardProvider());
    }

}
