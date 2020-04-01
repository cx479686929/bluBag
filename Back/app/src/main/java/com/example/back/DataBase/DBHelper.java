package com.example.back.DataBase;

import android.util.Log;

import org.litepal.crud.DataSupport;

public class DBHelper{
    //public static SQLiteDatabase db = Connector.getDatabase();
    public static void init(){
        mySetting setting=new mySetting();
        setting.save();
        initConfig();
    }
    public static void save(String ID,String key,int roomID){
//        DataSupport.deleteAll(mySetting.class);
//        mySetting setting=new mySetting(ID,key,roomID);
//        setting.save();
        mySetting m=DataSupport.findFirst(mySetting.class);
        m.setMyid(ID);
        m.setKey(key);
        m.setRoomID(roomID);
        m.save();
    }
    public static String searchId(){
        mySetting s= DataSupport.findFirst(mySetting.class);
        return s.getMyid();
    }
    public static String searchKey(){
        mySetting s= DataSupport.findFirst(mySetting.class);
        return s.getKey();
    }
    public static void editeid(String id){
        mySetting s=DataSupport.findFirst(mySetting.class);
        s.setMyid(id);
        s.save();
    }
    public static void editekey(String key){
        mySetting s=DataSupport.findFirst(mySetting.class);
        s.setKey(key);
        s.save();

    }

    public static void edite(String id,String key){
        mySetting s=DataSupport.findFirst(mySetting.class);
        s.setMyid(id);
        s.setKey(key);
        s.save();
    }
    public static void initConfig(){
        mySetting s=DataSupport.findFirst(mySetting.class);
        if (s.getHeartInterval()==0){
            s.setHeartInterval(1);
        }
        if(s.getCheckExceptionInterval()==0){
            s.setCheckExceptionInterval(5);
        }
        if(s.getSendingInterval()==0){
            s.setSendingInterval(1);
        }
        Log.v("Data",s.getSendingInterval()+"");
        s.save();
    }
    public static  int getHeartInterval(){
        mySetting s=DataSupport.findFirst(mySetting.class);
        return  s.getHeartInterval();
    }
    public static  int getSendingInterval(){
        mySetting s=DataSupport.findFirst(mySetting.class);
        return  s.getSendingInterval();
    }
    public static int getCheckExceptionInterval(){
        mySetting s = null;
        s=DataSupport.findFirst(mySetting.class);
        if(s==null){
            init();
        }
        s=DataSupport.findFirst(mySetting.class);
        return s.getCheckExceptionInterval();

    }
    public static void saveConfig(int SendingInterval,int HeartInterval,int CheckExceptionInterval){
        mySetting s=DataSupport.findFirst(mySetting.class);
        s.setSendingInterval(SendingInterval);
        s.setHeartInterval(HeartInterval);
        s.setCheckExceptionInterval(CheckExceptionInterval);
        s.save();
    }


}
