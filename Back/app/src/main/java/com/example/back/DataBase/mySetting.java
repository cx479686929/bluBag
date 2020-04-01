package com.example.back.DataBase;

import org.litepal.crud.DataSupport;

public class mySetting extends DataSupport {
    private String myid;//SvrID
    private String key;//SvrKey
    private String version;
    private int sendingInterval;//发送间隔
    private int heartInterval;//心跳间隔
    private int checkExceptionInterval;//检查是否异常的间隔
    private int roomID;
    public int getRoomID() {
        return roomID;
    }

    public void setRoomID(int roomID) {
        this.roomID = roomID;
    }
    public mySetting(){

    }

    public mySetting(String myid,String key,int roomID){
        this.myid=myid;
        this.key=key;
        this.roomID=roomID;
    }
    public void setMyid(String myid) {

        this.myid = myid;
    }

    public String getKey(){

        return key;
    }

    public String getMyid() {

        return myid;
    }

    public void setKey(String key) {

        this.key = key;
    }
    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }


    public int getSendingInterval() {
        return sendingInterval;
    }

    public void setSendingInterval(int sendingInterval) {
        this.sendingInterval = sendingInterval;
    }

    public int getHeartInterval() {
        return heartInterval;
    }

    public void setHeartInterval(int heartInterval) {
        this.heartInterval = heartInterval;
    }

    public int getCheckExceptionInterval() {
        return checkExceptionInterval;
    }

    public void setCheckExceptionInterval(int checkExceptionInterval) {
        this.checkExceptionInterval = checkExceptionInterval;
    }
}
