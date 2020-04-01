package com.example.back.Server;
import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import com.example.back.DataBase.DBHelper;
import com.example.back.DataBase.dateHelper;
import com.example.back.Device;
import com.example.back.MainActivity;
import com.example.back.SettingsActivity;
import com.example.back.Tools.MyLog;
import com.example.back.Tools.StringFormat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.LitePal;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;
import java.security.MessageDigest;
import java.util.ArrayList;

public class XutilsHelper extends Application {
    public String home1="https://api.ourfor.top/pocket";
    public  String home="http://39.104.110.210:8443";
    public String url=home+"/student/sign-in-all";
    //public String url="http://www.haohanxingchen.cn/blubag/recordServlet";
   // public String registeurl="http://www.haohanxingchen.cn/blubag/ResgistServlet";
    public String registeurl=home+"/agent";
    public String getRoomsurl=home+"/rooms";
    public String checkUrl=home+ "/agent/hearts";
    public String changeStateUrl=home+"/agent/state";
    public  String ID;
    public  String Key;

    public void onCreate(){
        super.onCreate();
        LitePal.initialize(this);
        x.Ext.init(this);
        x.Ext.setDebug(true);
        Log.v("post","init");


    }
    public  void initsetting(){
        ID= DBHelper.searchId();
        Key=DBHelper.searchKey();
    }

    class MyCommonCallback implements Callback.CommonCallback<String>{
        public ArrayList<Device> devices;
        public Device device;
        public MyCommonCallback(ArrayList<Device> devices){
            this.devices=devices;
        }
        public MyCommonCallback(Device device){
            this.device=device;
        }
        @Override
        public void onSuccess(String result) {
            Log.v("post","返回成功"+result);
            //成功　得到返回数据
            try {
                JSONObject jsonObject=new JSONObject(result);
                int code=jsonObject.getInt("code");
                if(code==200){
                    JSONObject jsonObject1=jsonObject.getJSONObject("data");
                    JSONObject data=jsonObject1.getJSONObject("data");
                    JSONArray succList=data.getJSONArray("succList");
                    JSONArray failList=data.getJSONArray("failList");
//                    for(int i=0;i<devices.size();i++){
//                        Device dev=devices.get(i);
//                        String mac=dev.macAddress;
//                        mac=mac.replaceAll("[[\\s-:punct:]]","");
                        for(int j=0;j<succList.length();j++){
                            JSONObject js=succList.getJSONObject(j);
                            String BMac=js.getString("BMac");
                            String stuName=js.getString("stuName");
//                            Log.v("post",mac);
//                            if( mac.equals(BMac)){
//                                Log.v("post","yes");
//                                dev.stuName=stuName;
//                                dev.sendflag=true;
//                            }
                           // String name = new String(stuName.getBytes("utf-8"), "utf-8");
                            stuName= StringFormat.changeFormat(stuName,16);
                            String isArrive=StringFormat.changeFormat("签到",8);
                            String isInbag=StringFormat.changeFormat("入袋",6);
                            String msg=String.format("%s %s %s %s", dateHelper.getTime(),stuName,isArrive,isInbag);
                            MainActivity.toshowList.add(msg);

                        }
                    for(int i=0;i<failList.length();i++){
                        JSONObject js1=failList.getJSONObject(i);
                        String BName=js1.getString("BName");

                        BName= StringFormat.changeFormat(BName,16);
                        String isArrive=StringFormat.changeFormat("未签到",8);
                        String isInbag=StringFormat.changeFormat("入袋",6);
                      //  String name = new String(BName.getBytes("utf-8"), "utf-8");
                        String msg=String.format("%s %s %s %s",dateHelper.getTime(),BName,isArrive,isInbag);
                        MainActivity.toshowList.add(msg);
                    }
                    MyLog.v("result",result);

//                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onError(Throwable ex, boolean isOnCallback) {

            Log.v("post","senderror"+ex);
        }

        @Override
        public void onCancelled(CancelledException cex) {

        }

        @Override
        public void onFinished() {
            Log.v("post","send over");
            //device.sendflag=true;
        }
    }

    public  String str2md5(String str){
        String hashStr="";
        try {
            MessageDigest md5=MessageDigest.getInstance("MD5");
            byte[] digest=md5.digest(str.getBytes("utf-8"));
            hashStr=bytesToHexString(digest);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  hashStr;
    }

    public String bytesToHexString(byte[] bArr) {
        StringBuffer sb = new StringBuffer(bArr.length);
        String sTmp;

        for (int i = 0; i < bArr.length; i++) {
            sTmp = Integer.toHexString(0xFF & bArr[i]);
            if (sTmp.length() < 2)
                sb.append(0);
            sb.append(sTmp.toUpperCase());
        }

        return sb.toString();
    }
    public void sendArr(ArrayList<Device> devices){
        try {
            initsetting();
        JSONObject js_request=new JSONObject();
        JSONArray cantianer=new JSONArray();
        String md5="";

        //数据整理包装

         JSONObject data=new JSONObject();

         data.put("AppID", Integer.parseInt(ID));
         data.put("time",System.currentTimeMillis());
            for (int i = 0; i < devices.size(); i++) {
            Device  device2 = devices.get(i);
            JSONObject object=new JSONObject();
            object.put("BName", device2.name);
            object.put("Bdistance", device2.distance);
            object.put("BMac", device2.macAddress);
            cantianer.put(object);
        }
        data.put("devices",cantianer);
        data.put("type","test");
        js_request.put("data",data);
        Log.v("post",cantianer.toString());
        md5= str2md5(data.toString()+Key);
        js_request.put("md5",md5);
        Log.v("post",js_request.toString());
            //上传数据
            RequestParams params = new RequestParams(url);
            params.addHeader("Connection","close");
            params.setAsJsonContent(true);
            params.setBodyContent(js_request.toString());
            x.http().post(params, new MyCommonCallback(devices));
            //new MyLog("post",data.toString()).start();
        } catch (JSONException e) {
            e.printStackTrace();
    }
    }

    public void sendDevice(Device device){
       ArrayList<Device>devices=new ArrayList<Device>();
       devices.add(device);
       sendArr(devices);
    }

    public void register(String Svrcode , String roomID, SettingsActivity settingsActivity){

        Log.v("post code",Svrcode);
        RequestParams params = new RequestParams(registeurl);
        params.addHeader("Connection","close");
        params.addBodyParameter("code",Svrcode);
        params.addBodyParameter("version","1");
        params.addBodyParameter("room",roomID);
        Log.v("post",params.toString());
        x.http().post(params, new MyRegisterCallback(settingsActivity));

    }
    public class MyRegisterCallback implements Callback.CommonCallback<String>{
        SettingsActivity settingsActivity;
        public  MyRegisterCallback(SettingsActivity settingsActivity){
            this.settingsActivity=settingsActivity;
        }
        @Override
        public void onSuccess(String result) {
            Log.v("post",result);
            try {
                JSONObject res1=new JSONObject(result);
                int code=res1.getInt("code");
                if(code==200){
                    //解析数据
                    JSONObject res=res1.getJSONObject("data");
                    String SvrKey=res.getString("SvrKey");
                    int  SvrID=res.getInt("SvrId");
                    String Version=res.getString("Version");
                    String SvrCode=res.getString("SvrCode");
                    int RoomID=res.getInt("RoomID");
                    //保存相应的数据
                    DBHelper.save(SvrID+"",SvrKey,RoomID);
                    //Log.v("save",DBHelper.searchId()+ DBHelper.searchKey());
                    //
                    Toast t= Toast.makeText(settingsActivity.getApplicationContext(),"注册成功 SvrID="+SvrID+" RoomID="+RoomID,Toast.LENGTH_LONG);
                    t.show();
                    settingsActivity.refresh(res);

                }


            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onError(Throwable ex, boolean isOnCallback) {

        }

        @Override
        public void onCancelled(CancelledException cex) {

        }

        @Override
        public void onFinished() {

        }
    }

    public void getRooms(SettingsActivity settingsActivity, String searchRoomNumber){
        RequestParams params = new RequestParams(getRoomsurl);
        x.http().get(params, new getRoomsCallback(settingsActivity,searchRoomNumber));

    }
    public class getRoomsCallback  implements Callback.CommonCallback<String>{
        SettingsActivity settingsActivity;
        String searchRoomNumber;
        public getRoomsCallback(SettingsActivity settingsActivity ,String searchRoomNumber){
            this.settingsActivity=settingsActivity;
            this.searchRoomNumber=searchRoomNumber;
        }
        @Override
        public void onSuccess(String result) {
            try {
                JSONObject jsonObject=new JSONObject(result);
                Log.v("post",result);
                int code=jsonObject.getInt("code");
                if(code==200){
                    JSONArray rooms=jsonObject.getJSONArray("data");
                    settingsActivity.myRooms.clear();
                    for(int i=0;i<rooms.length();i++){
                        JSONObject room=rooms.getJSONObject(i);
                        int roomID=room.getInt("roomID");
                        String  roomName=room.getString("roomName");
                        int siteCount=room.getInt("siteCount");
                        String building=room.getString("building");
                        if(searchRoomNumber.equals(roomName)){
                            String item=roomID+"\u00A0"+building +"\u00A0"+roomName;
                            settingsActivity.myRooms.add(item);
                            Log.v("post",item);
                        }
                    }
                    Log.v("post","show");
                    settingsActivity.showRooms();

                }
            } catch (Exception e) {
                e.printStackTrace();
            }



        }

        @Override
        public void onError(Throwable ex, boolean isOnCallback) {

        }

        @Override
        public void onCancelled(CancelledException cex) {

        }

        @Override
        public void onFinished() {

        }
    }

    public void checkStatus(){
        //心跳请求
        initsetting();
        RequestParams params = new RequestParams(checkUrl);
        params.addHeader("Connection","close");
        params.addBodyParameter("SvrID",ID);
        params.addBodyParameter("time",System.currentTimeMillis()+"");
        params.addBodyParameter("isException",false+"");
        Log.v("post",params.toString());
        x.http().get(params, new checkCallback());
    }
    class checkCallback implements Callback.CommonCallback<String>{
        @Override
        public void onSuccess(String result) {
            Log.v("post",result);
            try {
                JSONObject jsonObject=new JSONObject(result);
                int code=jsonObject.getInt("code");
                if(code==200){
                 JSONObject data=jsonObject.getJSONObject("data");
                  String state=data.getString("state");
                  if(state.equals("启用")){
                    MainActivity.life=DBHelper.getCheckExceptionInterval();
                  }else{
                    MainActivity.isBan=true;
                  }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onError(Throwable ex, boolean isOnCallback) {

        }

        @Override
        public void onCancelled(CancelledException cex) {

        }

        @Override
        public void onFinished() {
        }
    }




    public void changeState(String state){
        initsetting();
        RequestParams params = new RequestParams(changeStateUrl);
        params.addHeader("Connection","close");
        params.addBodyParameter("id",ID);
        params.addBodyParameter("status",state);
        String md5=str2md5("id="+ID+"&status="+state);
        //Log.v("post","id="+ID+"&status="+state+"Md5"+md5);
        params.addBodyParameter("md5",md5.toLowerCase());
        //Log.v("post",params.toString());
        x.http().post(params, new changeStateCallback());
    }

    class changeStateCallback implements Callback.CommonCallback<String>{
        @Override
        public void onSuccess(String result) {
            Log.v("post",result);
            try {
                JSONObject jsonObject=new JSONObject(result);
                String msg=jsonObject.getString("msg");

                if(msg.equals("success")){

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onError(Throwable ex, boolean isOnCallback) {

        }

        @Override
        public void onCancelled(CancelledException cex) {

        }

        @Override
        public void onFinished() {

        }

    }

}
