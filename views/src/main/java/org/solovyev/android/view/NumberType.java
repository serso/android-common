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

package org.solovyev.android.view;

import javax.annotation.Nonnull;

import java.math.BigDecimal;

/**
 * Utility enumeration used to convert between Numbers and doubles.
 *
 * @author Stephan Tittel (stephan.tittel@kom.tu-darmstadt.de)
 */
enum NumberType {

	LONG(Long.class),
	DOUBLE(Double.class),
	INTEGER(Integer.class),
	FLOAT(Float.class),
	SHORT(Short.class),
	BYTE(Byte.class),
	BIG_DECIMAL(BigDecimal.class);

	@Nonnull
	private final Class underlyingClass;

	NumberType(@Nonnull Class underlyingClass) {
		this.underlyingClass = underlyingClass;
	}

	@Nonnull
	public static <E extends Number> NumberType fromNumber(E value) throws IllegalArgumentException {

		for (NumberType numberType : NumberType.values()) {
			if (numberType.underlyingClass.isInstance(value)) {
				return numberType;
			}
		}

		throw new IllegalArgumentException("Number class '" + value.getClass().getName() + "' is not supported");
	}

	public <T extends Number> T toNumber(double value) {

		switch (this) {
			case LONG:
				return (T)new Long((long) value);
			case DOUBLE:
				return (T)new Double(value);
			case INTEGER:
				return (T)new Integer((int) value);
			case FLOAT:
				return (T)new Float((float) value);
			case SHORT:
				return (T)new Short((short) value);
			case BYTE:
				return (T)new Byte((byte) value);
			case BIG_DECIMAL:
				return (T)new BigDecimal(value);
		}

		throw new InstantiationError("can't convert " + this + " to a Number object");
	}
}
