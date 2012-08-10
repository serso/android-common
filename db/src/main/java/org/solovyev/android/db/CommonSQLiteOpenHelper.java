package org.solovyev.android.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import org.jetbrains.annotations.NotNull;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Scanner;

/**
 * User: serso
 * Date: 6/3/12
 * Time: 4:35 PM
 */
public class CommonSQLiteOpenHelper extends SQLiteOpenHelper {

    private static final String TAG = "DbOperation";

    @NotNull
    protected final Context context;

    @NotNull
    private String databaseName;

    private int version;

    public CommonSQLiteOpenHelper(@NotNull Context context, @NotNull SQLiteOpenHelperConfiguration configuration) {
        super(context, configuration.getName(), configuration.getCursorFactory(), configuration.getVersion());
        this.context = context;
        this.databaseName = configuration.getName();
        this.version = configuration.getVersion();
    }

    @Override
    public void onCreate(@NotNull SQLiteDatabase db) {
        onUpgrade(db, 0, this.version);
    }

    @Override
    public void onUpgrade(@NotNull SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "Upgrading database, old version: " + oldVersion + ", new version: " + newVersion);

        final DecimalFormat decimalFormat = new DecimalFormat("000");

        for (int version = oldVersion + 1; version <= newVersion; version++) {
            try {
                // prepare version based postfix
                final String fileVersionPostfix = decimalFormat.format(version);

                final String fileName = "db_" + databaseName + "_" + fileVersionPostfix + ".sql";

                Log.d(TAG, "Reading " + fileName + "...");

                // read sqls from file
                final String sqls = convertStreamToString(context.getAssets().open(fileName));

                Log.d(TAG, fileName + " successfully read, size: " + sqls.length());


                // batch execute
                new BatchDbTransaction(sqls, ";\n").batchQuery(db);

            } catch (FileNotFoundException e) {
                Log.d(TAG, e.getMessage());
                // ok, probably file not exists
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
                throw new RuntimeException(e);
            }
        }
    }

    @NotNull
    public String convertStreamToString(java.io.InputStream is) {
        try {
            return new Scanner(is, "UTF-8").useDelimiter("\\A").next();
        } catch (java.util.NoSuchElementException e) {
            return "";
        }
    }
}

