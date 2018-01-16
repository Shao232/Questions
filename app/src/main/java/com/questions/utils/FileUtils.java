package com.questions.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Base64;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by 11470 on 2017/10/17.
 */

public class FileUtils {

    public static SQLiteDatabase openDataBase(Context context, String fileName, String dbName) {
      String dbPath = "data/data/"+context.getPackageName()+"/"+dbName;



        if (!(new File(dbPath)).exists()) {
//            new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/databases/").mkdirs();
            try {
//                new File(dbPath).createNewFile();

                FileOutputStream out = new FileOutputStream(dbPath);
                InputStream in = context.getAssets().open(fileName);
                byte[] buffer = new byte[1024];
                int readBytes = 0;
                while ((readBytes = in.read(buffer)) != -1) {
                    out.write(buffer, 0, readBytes);
                }
                in.close();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return SQLiteDatabase.openOrCreateDatabase(dbPath, null);
    }

    public static byte[] loadRawDataFromURL(String u) throws Exception {
        URL url =null;
        if (!u.contains("http://")){
            url = new URL("http://"+u);
        }else {
            url = new URL(u);
        }
        MyLog.i("url>>>>"+url);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        InputStream is = conn.getInputStream();
        BufferedInputStream bis = new BufferedInputStream(is);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        final int BUFFER_SIZE = 2048;
        final int EOF = -1;

        int c;
        byte[] buf = new byte[BUFFER_SIZE];

        while (true) {
            c = bis.read(buf);
            if (c == EOF)
                break;

            baos.write(buf, 0, c);
        }
        conn.disconnect();
        is.close();

        byte[] data = baos.toByteArray();
        baos.flush();

        return data;
    }

    /**
     * 获得指定文件的byte数组
     */
    public static byte[] getBytes(String filePath){
        byte[] buffer = null;
        try {
            File file = new File(filePath);
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
            byte[] b = new byte[1000];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer;
    }

    /**
     * 根据byte数组，生成文件
     */
    public static void getFile(byte[] bfile, String filePath,String fileName) {
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        File file = null;
        try {
            File dir = new File(filePath);
            if(!dir.exists()&&dir.isDirectory()){//判断文件目录是否存在
                dir.mkdirs();
            }
            file = new File(filePath+"\\"+fileName);
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(bfile);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    /**
     * 将InputStream转换成byte数组
     * @param in InputStream
     * @return byte[]
     * @throws IOException
     */
    public static byte[] InputStreamTOByte(InputStream in) throws IOException{
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] data = new byte[1024];
        int count = -1;
        while((count = in.read(data,0,data.length)) != -1) {
            outStream.write(data, 0, count);
        }
        outStream.close();
        return outStream.toByteArray();
    }

    /**
     * 图片文件转换为指定编码的字符串
     *
     * @param imgFile  图片文件
     */
    public static String file2String(File imgFile) {
        InputStream in = null;
        byte[] data = null;
        //读取图片字节数组
        try{
            in = new FileInputStream(imgFile);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e){
            e.printStackTrace();
        }
        //对字节数组Base64编码
        return Base64.encodeToString(data,Base64.DEFAULT);//返回Base64编码过的字节数组字符串
    }

}
