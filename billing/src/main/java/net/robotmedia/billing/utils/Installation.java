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

package net.robotmedia.billing.utils;

import android.content.Context;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.UUID;

public class Installation {

	private static final String INSTALLATION = "INSTALLATION";

	private static String sID = null;

	@NotNull
	private static final Object lock = new Object();

	public synchronized static String id(@NotNull Context context) {
		if (sID == null) {

			// let's synchronize IO operations
			synchronized (lock) {
				final File installation = new File(context.getFilesDir(), INSTALLATION);
				try {
					if (!installation.exists()) {
						sID = writeInstallationFile(installation);
					} else {
						sID = readInstallationFile(installation);
					}
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		}

		return sID;
	}

	@NotNull
	private static String readInstallationFile(@NotNull File installation) throws IOException {
		byte[] bytes = null;

		RandomAccessFile in = null;
		try {
			in = new RandomAccessFile(installation, "r");
			bytes = new byte[(int) in.length()];
			in.readFully(bytes);
		} catch (IOException e) {
			if (in != null) {
				in.close();
			}
		}

		return new String(bytes);
	}

	@NotNull
	private static String writeInstallationFile(@NotNull File installation) throws IOException {
		final String id = UUID.randomUUID().toString();

		FileOutputStream out = null;
		try {
			out = new FileOutputStream(installation);
			out.write(id.getBytes());
		} finally {
			if (out != null) {
				out.close();
			}
		}

		return id;
	}
}
