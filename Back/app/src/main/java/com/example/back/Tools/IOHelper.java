package com.example.back.Tools;

import android.app.Activity;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;

import static org.litepal.LitePalApplication.getContext;

public class IOHelper {


    public static void write(){
        try {
            String path = getContext().getExternalFilesDir(null).getAbsolutePath();
            File dirFile = new File(path);
            if (!dirFile.exists()) {
                boolean mkdirs = dirFile.mkdirs();
                if (!mkdirs) {
                    Log.i("IOIO", "创建：" + mkdirs);
                } else {
                    Log.i("IOIO", "创建成功");
                }
            }
            File dir = new File(path+"/test.txt");
            if (!dir.exists()) {
                try {
                    //在指定的文件夹中创建文件
                    dir.createNewFile();
                } catch (Exception e) {
                }
            }




            File file1 = new File(path+"/test.txt");
            Log.v("IOIO","写"+Environment.getExternalStorageDirectory().toString());
            /**
             * 为了提高写入的效率，使用了字符流的缓冲区。
             * 创建了一个字符写入流的缓冲区对象，并和指定要被缓冲的流对象相关联。
             */
            //第二个参数意义是说是否以append方式添加内容
            BufferedWriter bw = new BufferedWriter(new FileWriter(file1, true));

            //使用缓冲区中的方法将数据写入到缓冲区中。
            bw.write("hello world !");
            bw.newLine();
            bw.newLine();
            bw.write("test");
            bw.write("content");
            //使用缓冲区中的方法，将数据刷新到目的地文件中去。
            bw.flush();
            //关闭缓冲区,同时关闭了FileWriter流对象
            bw.close();
            System.out.println("写入成功");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void read(){
        try {
            String path = getContext().getExternalFilesDir(null).getAbsolutePath();
            File file = new File(path+"/test.txt");
            BufferedReader br = null;
            br = new BufferedReader(new FileReader(file));

            String readline = "";
            StringBuffer sb = new StringBuffer();
            while ((readline = br.readLine()) != null) {
                //System.out.println("readline:" + readline);
                sb.append(readline);
            }
            br.close();
            Log.v("IOIO","读"+ sb.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
