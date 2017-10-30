package com.slibrary.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by 11470 on 2017/10/17.
 */

public class FileUtils {

    public static SQLiteDatabase openDataBase(Context context, String fileName, String dbName) {
        String dbPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/databases/" + dbName;
        if (!(new File(dbPath)).exists()) {
            new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/databases/").mkdirs();
            try {
                new File(dbPath).createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                FileOutputStream out = new FileOutputStream(dbPath);
                InputStream in = context.getAssets().open(fileName);
                byte[] buffer = new byte[1024];
                int readBytes = 0;
                while ((readBytes = in.read(buffer)) != -1)
                    out.write(buffer, 0, readBytes);
                in.close();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return SQLiteDatabase.openOrCreateDatabase(dbPath, null);
    }

}
