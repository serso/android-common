package org.solovyev.android.keyboard;

import android.content.Context;
import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.text.InputType;
import android.view.inputmethod.EditorInfo;
import org.jetbrains.annotations.NotNull;
import org.solovyev.android.AndroidUtils;
import org.solovyev.android.view.drag.DragDirection;

import java.util.ArrayList;
import java.util.List;

/**
 * User: serso
 * Date: 11/3/12
 * Time: 11:58 PM
 */
public class LatinDragKeyboardController extends DragKeyboardController {

    private int currentKeyboard = 0;

    @NotNull
	private final List<DragAKeyboard> languageKeyboardDefs = new ArrayList<DragAKeyboard>(2);

	@NotNull
	private DragAKeyboard.KeyboardDef digitsKeyboard;

    @NotNull
    @Override
    protected AKeyboardControllerState<DragAKeyboard> onInitializeInterface0(@NotNull InputMethodService inputMethodService) {
        languageKeyboardDefs.add(createKeyboard("en", createEnglishKeyboard(inputMethodService)));
        languageKeyboardDefs.add(createKeyboard("ru", createRussianKeyboard(inputMethodService)));

		digitsKeyboard = createDigitsKeyboard(inputMethodService);

        return super.onInitializeInterface0(inputMethodService);
    }

    @Override
    protected DragAKeyboard createKeyboardDef(@NotNull Context context) {
        return languageKeyboardDefs.get(currentKeyboard);
    }

    @NotNull
    private DragAKeyboard.KeyboardDef createRussianKeyboard(@NotNull Context context) {
        final int notLetterBackgroundResId = R.drawable.metro_dark_button_gray;

        final DragAKeyboard.KeyboardDef result = new DragAKeyboard.KeyboardDef();

        final DragAKeyboard.RowDef firstRow = new DragAKeyboard.RowDef();
        firstRow.add(DragAKeyboardButtonDefImpl.newInstance("й", "Й", null, "1", "!"));
        firstRow.add(DragAKeyboardButtonDefImpl.newInstance("ц", "Ц", null, "2", "@"));
        firstRow.add(DragAKeyboardButtonDefImpl.newInstance("у", "У", null, "3", "#"));
        firstRow.add(DragAKeyboardButtonDefImpl.newInstance("к", "К", null, "4", "$"));
        firstRow.add(DragAKeyboardButtonDefImpl.newInstance("е", "Е", null, "5", "%"));
        firstRow.add(DragAKeyboardButtonDefImpl.newInstance("н", "Н", null, "6", "^"));
        firstRow.add(DragAKeyboardButtonDefImpl.newInstance("г", "Г", null, "7", "&"));
        firstRow.add(DragAKeyboardButtonDefImpl.newInstance("ш", "Ш", null, "8", "*"));
        firstRow.add(DragAKeyboardButtonDefImpl.newInstance("щ", "Щ", null, "9", "("));
        firstRow.add(DragAKeyboardButtonDefImpl.newInstance("з", "З", null, "0", ")"));
        firstRow.add(DragAKeyboardButtonDefImpl.newInstance("х", "Х", null, "0", ")"));
        firstRow.add(DragAKeyboardButtonDefImpl.newInstance("ъ", "Ъ", null, "0", ")"));
        result.add(firstRow);

        final DragAKeyboard.RowDef secondRow = new DragAKeyboard.RowDef();
        secondRow.add(DragAKeyboardButtonDefImpl.newInstance("ф", "Ф", null, null, null));
        secondRow.add(DragAKeyboardButtonDefImpl.newInstance("ы", "Ы", null, null, null));
        secondRow.add(DragAKeyboardButtonDefImpl.newInstance("в", "В", null, null, null));
        secondRow.add(DragAKeyboardButtonDefImpl.newInstance("а", "А", null, null, null));
        secondRow.add(DragAKeyboardButtonDefImpl.newInstance("п", "П", null, null, null));
        secondRow.add(DragAKeyboardButtonDefImpl.newInstance("р", "Р", null, null, null));
        secondRow.add(DragAKeyboardButtonDefImpl.newInstance("о", "О", null, null, null));
        secondRow.add(DragAKeyboardButtonDefImpl.newInstance("л", "Л", null, null, null));
        secondRow.add(DragAKeyboardButtonDefImpl.newInstance("д", "Д", null, null, null));
        secondRow.add(DragAKeyboardButtonDefImpl.newInstance("ж", "Ж", null, null, null));
        secondRow.add(DragAKeyboardButtonDefImpl.newInstance("э", "Э", null, null, null));
        secondRow.add(DragAKeyboardButtonDefImpl.newDrawableInstance(R.drawable.kb_delete, Keyboard.KEYCODE_DELETE, notLetterBackgroundResId));

        result.add(secondRow);

        final DragAKeyboard.RowDef thirdRow = new DragAKeyboard.RowDef();
        thirdRow.add(DragAKeyboardButtonDefImpl.newDrawableInstance(R.drawable.kb_copy, DragKeyboardController.KEYCODE_COPY, notLetterBackgroundResId));
        thirdRow.add(DragAKeyboardButtonDefImpl.newInstance("я", "Я", null, null, null));
        thirdRow.add(DragAKeyboardButtonDefImpl.newInstance("ч", "Ч", null, null, null));
        thirdRow.add(DragAKeyboardButtonDefImpl.newInstance("с", "С", null, null, null));
        thirdRow.add(DragAKeyboardButtonDefImpl.newInstance("м", "М", null, null, null));
        thirdRow.add(DragAKeyboardButtonDefImpl.newInstance("и", "И", null, null, null));
        thirdRow.add(DragAKeyboardButtonDefImpl.newInstance("т", "Т", null, null, null));
        thirdRow.add(DragAKeyboardButtonDefImpl.newInstance("ь", "Ь", null, null, null));
        thirdRow.add(DragAKeyboardButtonDefImpl.newInstance("б", "Б", null, null, null));
        thirdRow.add(DragAKeyboardButtonDefImpl.newInstance("ю", "Ю", null, null, null));
        thirdRow.add(DragAKeyboardButtonDefImpl.newInstance(",", ".", null, "!", "?", notLetterBackgroundResId));
        thirdRow.add(DragAKeyboardButtonDefImpl.newDrawableInstance(R.drawable.kb_enter, DragKeyboardController.KEYCODE_ENTER, notLetterBackgroundResId));
        result.add(thirdRow);

        final DragAKeyboard.RowDef fourthRow = new DragAKeyboard.RowDef();
        fourthRow.add(DragAKeyboardButtonDefImpl.newDrawableInstance(R.drawable.kb_paste, DragKeyboardController.KEYCODE_PASTE, notLetterBackgroundResId));

        // weight "eats" some margins => need to add them
        // 6 buttons with 1 dp margin needed for both sides + 1f for self margins= > / 2f
        int spaceMargin = AndroidUtils.toPixels(context.getResources().getDisplayMetrics(), ((6 - 1) * 1f + 1f) / 2f);

        fourthRow.add(DragAKeyboardButtonDefImpl.newInstance("-", null, null, null, null, notLetterBackgroundResId));
        fourthRow.add(DragAKeyboardButtonDefImpl.newInstance("-", null, null, null, null, notLetterBackgroundResId));

        final DragAKeyboardButtonDefImpl spaceButtonDef = DragAKeyboardButtonDefImpl.newInstance(context.getText(R.string.ru), null, ">", null, "<", notLetterBackgroundResId);
        spaceButtonDef.setKeycode((int) ' ');
        spaceButtonDef.setDirectionKeycode(DragDirection.left, DragKeyboardController.KEYCODE_PREV_KEYBOARD);
        spaceButtonDef.setDirectionKeycode(DragDirection.right, DragKeyboardController.KEYCODE_NEXT_KEYBOARD);

        spaceButtonDef.setLayoutMarginLeft(spaceMargin);
        spaceButtonDef.setLayoutMarginRight(spaceMargin);
        spaceButtonDef.setWeight(6f);

        fourthRow.add(spaceButtonDef);

        fourthRow.add(DragAKeyboardButtonDefImpl.newInstance(".", ",", null, null, null, notLetterBackgroundResId));
        fourthRow.add(createHistoryButtonDef(notLetterBackgroundResId));
        fourthRow.add(DragAKeyboardButtonDefImpl.newInstance("0", "(", null, ")", null, notLetterBackgroundResId));
        result.add(fourthRow);

        return result;
    }

    @NotNull
    private DragAKeyboardButtonDefImpl createHistoryButtonDef(int notLetterBackgroundResId) {
        final DragAKeyboardButtonDefImpl historyButtonDef = DragAKeyboardButtonDefImpl.newInstance(null, null, "↷", null, "↶", notLetterBackgroundResId);
		historyButtonDef.setDirectionKeycode(DragDirection.left, AbstractKeyboardController.KEYCODE_UNDO);
		historyButtonDef.setDirectionKeycode(DragDirection.right, AbstractKeyboardController.KEYCODE_REDO);
        return historyButtonDef;
    }

    @NotNull
    private DragAKeyboard.KeyboardDef createEnglishKeyboard(@NotNull Context context) {
        final int notLetterBackgroundResId = R.drawable.metro_dark_button_gray;

        final DragAKeyboard.KeyboardDef result = new DragAKeyboard.KeyboardDef();

        final DragAKeyboard.RowDef firstRow = new DragAKeyboard.RowDef();
        firstRow.add(DragAKeyboardButtonDefImpl.newInstance("q", "Q", null, "1", "!"));
        firstRow.add(DragAKeyboardButtonDefImpl.newInstance("w", "W", null, "2", "@"));
        firstRow.add(DragAKeyboardButtonDefImpl.newInstance("e", "E", null, "3", "#"));
        firstRow.add(DragAKeyboardButtonDefImpl.newInstance("r", "R", null, "4", "$"));
        firstRow.add(DragAKeyboardButtonDefImpl.newInstance("t", "T", null, "5", "%"));
        firstRow.add(DragAKeyboardButtonDefImpl.newInstance("y", "Y", null, "6", "^"));
        firstRow.add(DragAKeyboardButtonDefImpl.newInstance("u", "U", null, "7", "&"));
        firstRow.add(DragAKeyboardButtonDefImpl.newInstance("i", "I", null, "8", "*"));
        firstRow.add(DragAKeyboardButtonDefImpl.newInstance("o", "O", null, "9", "("));
        firstRow.add(DragAKeyboardButtonDefImpl.newInstance("p", "P", null, "0", ")"));
        result.add(firstRow);

        final DragAKeyboard.RowDef secondRow = new DragAKeyboard.RowDef();
        secondRow.add(DragAKeyboardButtonDefImpl.newInstance("a", "A", null, null, null));
        secondRow.add(DragAKeyboardButtonDefImpl.newInstance("s", "S", null, null, null));
        secondRow.add(DragAKeyboardButtonDefImpl.newInstance("d", "D", null, null, null));
        secondRow.add(DragAKeyboardButtonDefImpl.newInstance("f", "F", null, null, null));
        secondRow.add(DragAKeyboardButtonDefImpl.newInstance("g", "G", null, null, null));
        secondRow.add(DragAKeyboardButtonDefImpl.newInstance("h", "H", null, null, null));
        secondRow.add(DragAKeyboardButtonDefImpl.newInstance("j", "J", null, null, null));
        secondRow.add(DragAKeyboardButtonDefImpl.newInstance("k", "K", null, null, null));
        secondRow.add(DragAKeyboardButtonDefImpl.newInstance("l", "L", null, null, null));

        secondRow.add(DragAKeyboardButtonDefImpl.newDrawableInstance(R.drawable.kb_delete, Keyboard.KEYCODE_DELETE, notLetterBackgroundResId));

        result.add(secondRow);

        final DragAKeyboard.RowDef thirdRow = new DragAKeyboard.RowDef();
        thirdRow.add(DragAKeyboardButtonDefImpl.newDrawableInstance(R.drawable.kb_copy, DragKeyboardController.KEYCODE_COPY, notLetterBackgroundResId));
        thirdRow.add(DragAKeyboardButtonDefImpl.newInstance("z", "Z", null, null, null));
        thirdRow.add(DragAKeyboardButtonDefImpl.newInstance("x", "X", null, null, null));
        thirdRow.add(DragAKeyboardButtonDefImpl.newInstance("c", "C", null, null, null));
        thirdRow.add(DragAKeyboardButtonDefImpl.newInstance("v", "V", null, null, null));
        thirdRow.add(DragAKeyboardButtonDefImpl.newInstance("b", "B", null, null, null));
        thirdRow.add(DragAKeyboardButtonDefImpl.newInstance("n", "N", null, null, null));
        thirdRow.add(DragAKeyboardButtonDefImpl.newInstance("m", "M", null, null, null));
        thirdRow.add(DragAKeyboardButtonDefImpl.newInstance(",", ".", null, "!", "?", notLetterBackgroundResId));
        thirdRow.add(DragAKeyboardButtonDefImpl.newDrawableInstance(R.drawable.kb_enter, DragKeyboardController.KEYCODE_ENTER, notLetterBackgroundResId));
        result.add(thirdRow);

        final DragAKeyboard.RowDef fourthRow = new DragAKeyboard.RowDef();
        fourthRow.add(DragAKeyboardButtonDefImpl.newDrawableInstance(R.drawable.kb_paste, DragKeyboardController.KEYCODE_PASTE, notLetterBackgroundResId));

        // weight "eats" some margins => need to add them
        // 4 buttons with 1 dp margin needed for both sides + 1f for self margins= > / 2f
        int spaceMargin = AndroidUtils.toPixels(context.getResources().getDisplayMetrics(), ((4 - 1) * 1f + 1f) / 2f);

        fourthRow.add(DragAKeyboardButtonDefImpl.newInstance("-", null, null, null, null, notLetterBackgroundResId));
        fourthRow.add(DragAKeyboardButtonDefImpl.newInstance("-", null, null, null, null, notLetterBackgroundResId));

        final DragAKeyboardButtonDefImpl spaceButtonDef = DragAKeyboardButtonDefImpl.newInstance(context.getText(R.string.en), null, ">", null, "<", notLetterBackgroundResId);
        spaceButtonDef.setKeycode((int) ' ');
        spaceButtonDef.setDirectionKeycode(DragDirection.left, DragKeyboardController.KEYCODE_PREV_KEYBOARD);
        spaceButtonDef.setDirectionKeycode(DragDirection.right, DragKeyboardController.KEYCODE_NEXT_KEYBOARD);

        spaceButtonDef.setLayoutMarginLeft(spaceMargin);
        spaceButtonDef.setLayoutMarginRight(spaceMargin);
        spaceButtonDef.setWeight(4f);

        fourthRow.add(spaceButtonDef);

        fourthRow.add(DragAKeyboardButtonDefImpl.newInstance(".", ",", null, null, null, notLetterBackgroundResId));
        fourthRow.add(createHistoryButtonDef(notLetterBackgroundResId));
        fourthRow.add(DragAKeyboardButtonDefImpl.newInstance("0", "(", null, ")", null, notLetterBackgroundResId));
        result.add(fourthRow);

        return result;
    }

	@NotNull
	private DragAKeyboard.KeyboardDef createDigitsKeyboard(@NotNull Context context) {
		final int notLetterBackgroundResId = R.drawable.metro_dark_button_gray;

		final DragAKeyboard.KeyboardDef result = new DragAKeyboard.KeyboardDef();

		final DragAKeyboard.RowDef firstRow = new DragAKeyboard.RowDef();
		firstRow.add(DragAKeyboardButtonDefImpl.newInstance("1", null));
		firstRow.add(DragAKeyboardButtonDefImpl.newInstance("2", null));
		firstRow.add(DragAKeyboardButtonDefImpl.newInstance("3", null));
		firstRow.add(DragAKeyboardButtonDefImpl.newDrawableInstance(R.drawable.kb_delete, Keyboard.KEYCODE_DELETE, notLetterBackgroundResId));
		result.add(firstRow);

		final DragAKeyboard.RowDef secondRow = new DragAKeyboard.RowDef();
		secondRow.add(DragAKeyboardButtonDefImpl.newInstance("4", null));
		secondRow.add(DragAKeyboardButtonDefImpl.newInstance("5", null));
		secondRow.add(DragAKeyboardButtonDefImpl.newInstance("6", null));
		secondRow.add(DragAKeyboardButtonDefImpl.newDrawableInstance(R.drawable.kb_copy, DragKeyboardController.KEYCODE_COPY, notLetterBackgroundResId));
		result.add(secondRow);

		final DragAKeyboard.RowDef thirdRow = new DragAKeyboard.RowDef();
		thirdRow.add(DragAKeyboardButtonDefImpl.newInstance("7", null));
		thirdRow.add(DragAKeyboardButtonDefImpl.newInstance("8", null));
		thirdRow.add(DragAKeyboardButtonDefImpl.newInstance("9", null));
		thirdRow.add(DragAKeyboardButtonDefImpl.newDrawableInstance(R.drawable.kb_paste, DragKeyboardController.KEYCODE_PASTE, notLetterBackgroundResId));
		result.add(thirdRow);

		final DragAKeyboard.RowDef fourthRow = new DragAKeyboard.RowDef();
		fourthRow.add(createHistoryButtonDef(notLetterBackgroundResId));
		fourthRow.add(DragAKeyboardButtonDefImpl.newInstance("0", "(", null, ")", null, notLetterBackgroundResId));
		fourthRow.add(DragAKeyboardButtonDefImpl.newInstance(".", ",", null, null, null, notLetterBackgroundResId));
		fourthRow.add(DragAKeyboardButtonDefImpl.newDrawableInstance(R.drawable.kb_enter, DragKeyboardController.KEYCODE_ENTER, notLetterBackgroundResId));
		result.add(fourthRow);

		return result;
	}

    @Override
    protected void handlePrevKeyboard() {
        super.handlePrevKeyboard();
        currentKeyboard -= 1;
        if ( currentKeyboard < 0 ) {
            currentKeyboard = languageKeyboardDefs.size() - 1;
        }
        setCurrentKeyboard(getCurrentLanguageKeyboard());
    }

    @Override
    protected void handleNextKeyboard() {
        super.handleNextKeyboard();
        currentKeyboard += 1;
        if ( currentKeyboard >= languageKeyboardDefs.size() ) {
            currentKeyboard = 0;
        }

        setCurrentKeyboard(getCurrentLanguageKeyboard());
    }

	private DragAKeyboard getCurrentLanguageKeyboard() {
		return languageKeyboardDefs.get(currentKeyboard);
	}

	@NotNull
	private DragAKeyboard createKeyboard(@NotNull String keyboardId, @NotNull DragAKeyboard.KeyboardDef keyboardDef) {
		return new DragAKeyboard(keyboardId, keyboardDef);
	}

	@NotNull
	@Override
	public AKeyboardControllerState<DragAKeyboard> onStartInput0(@NotNull EditorInfo attribute, boolean restarting) {
		final AKeyboardControllerState<DragAKeyboard> result;

		// We are now going to initialize our state based on the type of
		// text being edited.
		switch (attribute.inputType & InputType.TYPE_MASK_CLASS) {
			case InputType.TYPE_CLASS_NUMBER:
			case InputType.TYPE_CLASS_DATETIME:
				// Numbers and dates default to the symbols keyboard, with
				// no extra features.
				result = AKeyboardControllerStateImpl.newDefaultState(createKeyboard("digits", digitsKeyboard));
				break;

			case InputType.TYPE_CLASS_PHONE:
				// Phones will also default to the symbols keyboard, though
				// often you will want to have a dedicated phone keyboard.
				result = AKeyboardControllerStateImpl.newDefaultState(createKeyboard("digits", digitsKeyboard));
				break;

			case InputType.TYPE_CLASS_TEXT:
				// This is general text editing.  We will default to the
				// normal alphabetic keyboard, and assume that we should
				// be doing predictive text (showing candidates as the
				// user types).
				boolean prediction = true;
				boolean completion = false;

				// We now look for a few special variations of text that will
				// modify our behavior.
				int variation = attribute.inputType & InputType.TYPE_MASK_VARIATION;
				if (variation == InputType.TYPE_TEXT_VARIATION_PASSWORD ||
						variation == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
					// Do not display predictions / what the user is typing
					// when they are entering a password.
					prediction = false;
				}

				if (variation == InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
						|| variation == InputType.TYPE_TEXT_VARIATION_URI
						|| variation == InputType.TYPE_TEXT_VARIATION_FILTER) {
					// Our predictions are not useful for e-mail addresses
					// or URIs.
					prediction = false;
				}

				if ((attribute.inputType & InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE) != 0) {
					// If this is an auto-complete text view, then our predictions
					// will not be shown and instead we will allow the editor
					// to supply their own.  We only show the editor's
					// candidates when in fullscreen mode, otherwise relying
					// own it displaying its own UI.
					prediction = false;
					completion = getInputMethodService().isFullscreenMode();
				}

				result = AKeyboardControllerStateImpl.newInstance(prediction, completion, getCurrentLanguageKeyboard());

				// We also want to look at the current state of the editor
				// to decide whether our alphabetic keyboard should start out
				// shifted.
				updateShiftKeyState(attribute);
				break;

			default:
				// For all unknown input types, default to the alphabetic
				// keyboard with no special features.
				updateShiftKeyState(attribute);
				result = AKeyboardControllerStateImpl.newDefaultState(getCurrentLanguageKeyboard());
		}

		return result;
	}
}
