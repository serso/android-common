/*
 * Copyright (c) 2009-2011. Created by serso aka se.solovyev.
 * For more information, please, contact se.solovyev@gmail.com
 */

package org.solovyev.android;

/**
 * User: serso
 * Date: 9/13/11
 * Time: 12:08 AM
 */

/**
 * Interface for controlling the cursor position
 */
public interface CursorControl {

	/**
	 * Method sets the cursor to the beginning
	 */
	public void setCursorOnStart();

	/**
	 * Method sets the cursor to the end
	 */
	public void setCursorOnEnd();

	/**
	 * Method moves cursor to the left of current position
	 */
	public void moveCursorLeft();

	/**
	 * Method moves cursor to the right of current position
	 */
	public void moveCursorRight();
}
