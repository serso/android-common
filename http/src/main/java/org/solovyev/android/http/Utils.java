package org.solovyev.android.http;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Utils {

    // todo serso: move
    public static void copyStream(@NotNull InputStream is, @NotNull OutputStream os) throws IOException {
        final int bufferSize = 1024;
        byte[] bytes = new byte[bufferSize];

        while (true) {
            int count = is.read(bytes, 0, bufferSize);
            if (count == -1) {
                break;
            }
            os.write(bytes, 0, count);
        }
    }
}