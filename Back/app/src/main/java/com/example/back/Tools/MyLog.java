package com.example.back.Tools;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static org.litepal.LitePalApplication.getContext;

public class MyLog extends Thread{
    private static Boolean MYLOG_SWITCH = true; // 日志文件总开关
    private static Boolean MYLOG_WRITE_TO_FILE = true;// 日志写入文件开关
    private static char MYLOG_TYPE = 'v';// 输入日志类型，w代表只输出告警信息等，v代表输出所有信息
    private static String MYLOG_PATH_SDCARD_DIR = "/sdcard/kantu/log";// 日志文件在sdcard中的路径
    private static int SDCARD_LOG_FILE_SAVE_DAYS = 3;// sd卡中日志文件的最多保存天数
    private static String MYLOGFILEName = "Log.txt";// 本类输出的日志文件名称
    private static SimpleDateFormat myLogSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 日志的输出格式
    private static SimpleDateFormat logfile = new SimpleDateFormat("yyyy-MM-dd");// 日志文件格式
    public Context context;
    public String tag1;
    public String msg1;
    public MyLog(String tag1,String msg1){
        this.tag1=tag1;
        this.msg1=msg1;
    }
public void run(){
    v(tag1,msg1);
}
    public static void w(String tag, Object msg) { // 警告信息
        log(tag, msg.toString(), 'w');
    }

    public static void e(String tag, Object msg) { // 错误信息
        log(tag, msg.toString(), 'e');
    }

    public static void d(String tag, Object msg) {// 调试信息
        log(tag, msg.toString(), 'd');
    }

    public static void i(String tag, Object msg) {//
        log(tag, msg.toString(), 'i');
    }

    public static void v(String tag, Object msg) {
        log(tag, msg.toString(), 'v');
    }

    public static void w(String tag, String text) {
        log(tag, text, 'w');
    }

    public static void e(String tag, String text) {
        log(tag, text, 'e');
    }

    public static void d(String tag, String text) {
        log(tag, text, 'd');
    }

    public static void i(String tag, String text) {
        log(tag, text, 'i');
    }

    public static void v(String tag, String text) {
        log(tag, text, 'v');
    }

    /**
     * 根据tag, msg和等级，输出日志
     * @param tag
     * @param msg
     * @param level
     */
    private static void log(String tag, String msg, char level) {
        if (MYLOG_SWITCH) {//日志文件总开关
            if ('e' == level && ('e' == MYLOG_TYPE || 'v' == MYLOG_TYPE)) { // 输出错误信息
                Log.e(tag, msg);
            } else if ('w' == level && ('w' == MYLOG_TYPE || 'v' == MYLOG_TYPE)) {
                Log.w(tag, msg);
            } else if ('d' == level && ('d' == MYLOG_TYPE || 'v' == MYLOG_TYPE)) {
                Log.d(tag, msg);
            } else if ('i' == level && ('d' == MYLOG_TYPE || 'v' == MYLOG_TYPE)) {
                Log.i(tag, msg);
            } else {
                Log.v(tag, msg);
            }
            if (MYLOG_WRITE_TO_FILE)//日志写入文件开关
                writeLogtoFile(String.valueOf(level), tag, msg);
        }
    }

    /**
     * 打开日志文件并写入日志
     * @param mylogtype
     * @param tag
     * @param text
     */
    private static void writeLogtoFile(String mylogtype, String tag, String text) {// 新建或打开日志文件
        Date nowtime = new Date();
        String needWriteFiel = logfile.format(nowtime)+"bluLog.txt";
        String needWriteMessage = myLogSdf.format(nowtime) + " " + mylogtype + " " + tag + " " + text;
        //File dirPath = Environment.getExternalStorageDirectory();

        //创建目录文件夹
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
        File file = new File(path,needWriteFiel);
        if (!file.exists()) {
            try {
                //在指定的文件夹中创建文件
                file.createNewFile();
            } catch (Exception e) {
            }
        }

        try {
            FileWriter filerWriter = new FileWriter(file, true);// 后面这个参数代表是不是要接上文件中原来的数据，不进行覆盖
            BufferedWriter bufWriter = new BufferedWriter(filerWriter);
            bufWriter.write(needWriteMessage);
            bufWriter.newLine();
            bufWriter.flush();
            filerWriter.flush();
            bufWriter.close();
            filerWriter.close();
        } catch (IOException e) {
            Log.v("post","出错了2");
            e.printStackTrace();
        }
    }

    /**
     * 删除制定的日志文件
     */
    public static void delFile() {// 删除日志文件
        String needDelFiel = logfile.format(getDateBefore());
        File dirPath = Environment.getExternalStorageDirectory();
        File file = new File(dirPath, needDelFiel + MYLOGFILEName);// MYLOG_PATH_SDCARD_DIR
        if (file.exists()) {
            file.delete();
        }
    }
    public static void delmyFile(){
        Date nowtime = new Date();
        String needReadFiel = logfile.format(nowtime)+"bluLog.txt";
        String path = getContext().getExternalFilesDir(null).getAbsolutePath();
        File file = new File(path,needReadFiel);
        if (file.exists()) {
            file.delete();
        }

    }
    public static String readFile(){
        try {
            Date nowtime = new Date();
            String needReadFiel = logfile.format(nowtime)+"bluLog.txt";
            Log.v("IOIO",needReadFiel);
            String path = getContext().getExternalFilesDir(null).getAbsolutePath();
            File file = new File(path,needReadFiel);
            BufferedReader br = null;
            br = new BufferedReader(new FileReader(file));

            String readline = "";
            StringBuffer sb = new StringBuffer();
            while ((readline = br.readLine()) != null) {
                //System.out.println("readline:" + readline);
                sb.append(readline+"\n");
            }
            br.close();
            Log.v("IOIO","读"+ sb.toString());
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "null";
    }
    public static String readFile(String fileDate){
        try {
            //Date nowtime = new Date();
            String needReadFiel = fileDate+"bluLog.txt";
            String path = getContext().getExternalFilesDir(null).getAbsolutePath();
            File file = new File(path,needReadFiel);
            BufferedReader br = null;
            br = new BufferedReader(new FileReader(file));

            String readline = "";
            StringBuffer sb = new StringBuffer();
            while ((readline = br.readLine()) != null) {
                //System.out.println("readline:" + readline);
                sb.append(readline+"\n");
            }
            br.close();
            Log.v("IOIO","读"+ sb.toString());
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "null";
    }

    /**
     * 得到现在时间前的几天日期，用来得到需要删除的日志文件名
     */
    private static Date getDateBefore() {
        Date nowtime = new Date();
        Calendar now = Calendar.getInstance();
        now.setTime(nowtime);
        now.set(Calendar.DATE, now.get(Calendar.DATE) - SDCARD_LOG_FILE_SAVE_DAYS);
        return now.getTime();
    }
}