/*
 * Copyright 2013 serso aka se.solovyev
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Contact details
 *
 * Email: se.solovyev@gmail.com
 * Site:  http://se.solovyev.org
 */

package net.robotmedia.billing;

import javax.annotation.Nonnull;

/**
* User: serso
* Date: 1/17/12
* Time: 1:29 PM
*/

/**
 * Possible response codes from billing service
 */
public enum ResponseCode {

	RESULT_OK(0),
	RESULT_USER_CANCELED(1),
	RESULT_SERVICE_UNAVAILABLE(2),
	RESULT_BILLING_UNAVAILABLE(3),
	RESULT_ITEM_UNAVAILABLE(4),
	RESULT_DEVELOPER_ERROR(5),
	RESULT_ERROR(6);

	private final int response;

	ResponseCode(int response) {
		this.response = response;
	}

	public static boolean isOk(int response) {
		return ResponseCode.RESULT_OK.response == response;
	}

	@Nonnull
	public static ResponseCode valueOf(int response) {
		for (ResponseCode responseCode : values()) {
			if (responseCode.response == response) {
				return responseCode;
			}
		}

		return RESULT_ERROR;
	}
}
