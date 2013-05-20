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

package org.solovyev.android.properties;

import org.solovyev.common.JCloneable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

/**
 * User: serso
 * Date: 8/21/12
 * Time: 2:18 PM
 */
public interface MutableAProperties extends AProperties, JCloneable<MutableAProperties>, Serializable {

	@Nonnull
	@Override
	MutableAProperties clone();

	@Nonnull
	AProperty setProperty(@Nonnull String name, @Nonnull String value);

	void setProperty(@Nonnull AProperty property);

	@Nullable
	AProperty removeProperty(@Nonnull String name);

	void clearProperties();

	@Nonnull
	Map<String, AProperty> getProperties();

	void setPropertiesFrom(@Nonnull MutableAProperties that);

	void setPropertiesFrom(@Nonnull Collection<AProperty> properties);
}
