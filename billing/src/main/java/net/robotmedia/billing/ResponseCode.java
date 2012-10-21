package net.robotmedia.billing;

import org.jetbrains.annotations.NotNull;

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

	@NotNull
	public static ResponseCode valueOf(int response) {
		for (ResponseCode responseCode : values()) {
			if (responseCode.response == response) {
				return responseCode;
			}
		}

		return RESULT_ERROR;
	}
}
