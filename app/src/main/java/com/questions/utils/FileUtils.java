package com.questions.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import com.slibrary.utils.MyLog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by 11470 on 2017/10/17.
 */

public class FileUtils {

    public static SQLiteDatabase openDataBase(Context context, String fileName, String dbName) {
        //Log.i("PackName", packageName);
        // String DB_PATH = String.format("/data/data/%1$s/databases/",
        // packageName);
//        String dbPath = "/data/data/"+context.getPackageName()+"/databases/"+dbName;
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
//        String dirPath= Environment.getExternalStorageDirectory().getAbsolutePath()+"/aaa/";
//        File dir=new File(dirPath);
//        if(!dir.exists()) {
//            dir.mkdirs();
//        }
//        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
//            return SQLiteDatabase.openOrCreateDatabase(dirPath + dbName, null);
//        }else
//            return null;
    }

}
