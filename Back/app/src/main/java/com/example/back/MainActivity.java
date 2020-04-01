package com.example.back;
import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.back.DataBase.DBHelper;
import com.example.back.DataBase.dateHelper;
import com.example.back.Server.XutilsFactory;
import com.example.back.Server.XutilsHelper;
import com.example.back.Tools.IOHelper;
import com.example.back.Tools.MyLog;
import com.example.back.Tools.StringFormat;

import java.text.DecimalFormat;
import java.util.ArrayList;


public class MainActivity extends Activity implements View.OnClickListener ,Toolbar.OnMenuItemClickListener {
    private final int REQUEST_ENABLE_BT = 0xa01;
    private final int PERMISSION_REQUEST_COARSE_LOCATION = 0xb01;
    public static ArrayList<Device> devices=new ArrayList();
    public static ArrayList<Device> tempdevices=new ArrayList();
    public static ArrayList<String> toshowList=new ArrayList();
    private String TAG = "mainBlu";
    private int sendingInterval;//发送间隔
    private int heartInterval;//心跳间隔
    private int checkExceptionInterval;//检查是否异常的间隔
    public static ArrayAdapter<String> mAdapter;
    private BluetoothAdapter mBluetoothAdapter;
    public boolean ThreadFlag=false;
    public static boolean isBan=false;
    Toolbar toolBar;
    //public XutilsHelper myhelper=new XutilsHelper();
    public static int life=DBHelper.getCheckExceptionInterval();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolBar =this.findViewById(R.id.toolbar);
        toolBar.inflateMenu(R.menu.toolbar_menu);
        toolBar.setOnMenuItemClickListener(this);
        initConfig();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_COARSE_LOCATION);
            }
        }
        // 注册广播接收器。
        // 接收蓝牙发现
        IntentFilter filterFound = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mReceiver, filterFound);

        IntentFilter filterStart = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        registerReceiver(mReceiver, filterStart);

        IntentFilter filterFinish = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(mReceiver, filterFinish);

        mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, android.R.id.text1);
        ((ListView) findViewById(R.id.listView)).setAdapter(mAdapter);

        findViewById(R.id.init).setOnClickListener(this);
        findViewById(R.id.discovery).setOnClickListener(this);
        //findViewById(R.id.settingID).setOnClickListener(this);
        // findViewById(R.id.settingKey).setOnClickListener(this);
        // new DeviceControlThread().start();
        checkPermission();
        new DeviceCheckThread().start();
        new sendThread(this).start();
        new CheckStatusThread().start();
        new UIdisplayThread().start();
        //设置屏幕常亮
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


    }

public  void initConfig(){
        this.sendingInterval= DBHelper.getHeartInterval()*60000;
        this.heartInterval=DBHelper.getHeartInterval()*60000;
        this.checkExceptionInterval=DBHelper.getCheckExceptionInterval()*60000;
        Log.v("heartInteval",this.heartInterval+"");

}
public void showToast(String text){
    Looper.prepare();
    Toast t= Toast.makeText(this,text,Toast.LENGTH_LONG);
    t.show();
    Looper.loop();
}

    // 广播接收发现蓝牙设备
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
                Log.d(TAG, "开始扫描...");
            }

            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (device != null && device.getName()!=null) {
                    // 添加到ListView的Adapter
                        int rssi = intent.getExtras().getShort(BluetoothDevice.EXTRA_RSSI);//获取额外rssi值
                        double d = Math.pow(10,((Math.abs(rssi)-59)/ (10 * 2.0)));
                        DecimalFormat  df   = new DecimalFormat("######0.00");
                        String distance = df.format(d);
                        Device device1=new Device(device.getName(),device.getAddress(),distance);
                        if(d<2){
                        tempdevices.add(device1);
                        if(!device1.isInList(devices)){
                            devices.add(device1);
                            //myhelper.sendDevice(device1);
                            new XutilsFactory(2,device1).start();
                            Log.v(TAG, device1.name);
                            }
                        else{
                        }
                        }
                }
            }

            if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                Log.d(TAG, "扫描结束.");
                //开启下一次扫描
                try {
                    Thread.sleep(100);
                    tempdevices.clear();
                    mBluetoothAdapter.startDiscovery();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    //UI处理模块
    private Handler handler=new Handler(){
        public void handleMessage(Message msg){
            super.handleMessage(msg);
            if(msg.what==0){
                Device device=(Device)msg.obj;
                Log.v("change","in "+device.name);
                mAdapter.add(device.stuName+"打卡成功");
                mAdapter.notifyDataSetChanged();
            }else if(msg.what==1){
                Device device=(Device)msg.obj;
                mAdapter.remove(device.stuName+"打卡成功");
                mAdapter.notifyDataSetChanged();
            }else if(msg.what==2){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    Button b=findViewById(R.id.discovery);
                    b.setBackgroundResource(R.drawable.btn_item_run);
                    b.setText("正在扫描");
                }
            }else if(msg.what==3){
                String s=(String)msg.obj;
                mAdapter.insert(s,0);
                //mAdapter.add(s);
                mAdapter.notifyDataSetChanged();
            }
        }
    };

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()){
            case R.id.item1://注册ID
                Intent intent1 = new Intent(MainActivity.this,SettingsActivity.class);
                startActivity(intent1);
                //Log.v("Test","点击1");
                break;
            case R.id.item2://查看日志
                Intent intent2 = new Intent(MainActivity.this,LogActivity.class);
                startActivity(intent2);
                break;
            case R.id.item3://配置参数
                Intent intent3 = new Intent(MainActivity.this,ConfigureActivity.class);
                startActivity(intent3);
                break;

        }
        return true;
    }


    class DeviceCheckThread extends Thread{
        public XutilsHelper utilhelper=new XutilsHelper();
        public void run() {
            Device device;
            while (true) {
                if (ThreadFlag) {
//                    Message msg1 = handler.obtainMessage();
//                    msg1.what = 2;
//                    handler.sendMessage(msg1);

                    for (int i = 0; i < devices.size(); i++) {
                        device = devices.get(i);
                        Log.v("ss",i+"     "+device.name);
                        if (device.sendflag == true && device.displayflag == false) {
                            Message msg = handler.obtainMessage();
                            msg.what = 0;
                            msg.obj = device;
                            handler.sendMessage(msg);
                            device.displayflag = true;
                        }

                        if (!device.isInList(tempdevices)) {
                            if (device.isAlive()) {
                                device.reduceLife();
                            } else {
                                devices.remove(i);
                                Message msg = handler.obtainMessage();
                                msg.what = 1;
                                msg.obj = device;
                                handler.sendMessage(msg);
                            }
                        } else {
                            device.fullLife();
                        }
                        try {
                            Thread.sleep(1000);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    class sendThread extends Thread {
        MainActivity m;
        public sendThread(MainActivity m){
            this.m=m;
        }
        public XutilsHelper utilhelper = new XutilsHelper();
        public void run() {
            while (true) {
                try {
                if (ThreadFlag) {
                    MainActivity.life--;
                    if(MainActivity.life<0){
                        showToast("连接异常");
                    }
//                    utilhelper.sendArr(devices);
                 new XutilsFactory(1,devices).start();
                    Thread.sleep(sendingInterval);
                    }
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    class CheckStatusThread extends Thread{
        //心跳线程
        public XutilsHelper xutilsHelper=new XutilsHelper();
        public void run(){
            while (true){
                try {
                if(ThreadFlag){

                        xutilsHelper.checkStatus();
                        if(isBan){
                            ThreadFlag=false;
                            showToast("该设备未启动，请先启用");
                        }
                        Thread.sleep(heartInterval);
                }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    class UIdisplayThread extends Thread{
        public void run(){
            while(true){
                try {
                    for (int i = 0; i < toshowList.size(); i++) {
                        String s = toshowList.get(i);
                        sendMsg(s);
                        toshowList.remove(i);
                    }
                    Thread.sleep(5000);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }
    public  void sendMsg(String listItem){
        Message msg=handler.obtainMessage();
        msg.what=3;
        msg.obj=listItem;
        handler.sendMessage(msg);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.init:
                init();
                break;

            case R.id.discovery:
                discovery();
                //checkPermission();
                this.ThreadFlag=true;
                Message msg1 = handler.obtainMessage();
                msg1.what = 2;
                handler.sendMessage(msg1);
                break;

//            case R.id.settingID:
//                //enable_discovery();
//                //showDilogID();
//                Intent intent = new Intent(MainActivity.this,SettingsActivity.class);
//                startActivity(intent);
//                break;

//            case R.id.settingKey:
//                Log.v("post","点击");
//                myhelper.checkStatus();
//                break;
//                //showDilogKey();
////                XutilsHelper helper=new XutilsHelper();
////                //helper.test();
////                helper.getRooms(this);
//                Log.v("test",DBHelper.searchId());
        }
    }
    public boolean onOptionsItemSelected(MenuItem item){
        Log.v("Test","点击");
        switch (item.getItemId()){
            case R.id.item1: Log.v("Test","点击1"); break;
            case R.id.item2:break;
            case R.id.item3:break;
        }
        return true;
    }
    // 初始化蓝牙设备
    private void init() {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        // 检查设备是否支持蓝牙设备
        if (mBluetoothAdapter == null) {
            Log.d(TAG, "设备不支持蓝牙");

            // 不支持蓝牙，退出。
            return;
        }

        // 如果用户的设备没有开启蓝牙，则弹出开启蓝牙设备的对话框，让用户开启蓝牙
        if (!mBluetoothAdapter.isEnabled()) {
            Log.d(TAG, "请求用户打开蓝牙");
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            // 接下去，在onActivityResult回调判断
        }
//        String address= Settings.System.getString(getContentResolver(), Settings.System.ANDROID_ID);
//        Device.ID=address;
//        Log.v(TAG,address);

    }
    private void checkPermission() {

        //Android平台版本，如我的版本为Android 7.1.2
        Log.v(TAG,"Build.VERSION.RELEASE----->"+Build.VERSION.RELEASE);
        //当前手机版本-API版本号
        Log.v(TAG,"android.os.Build.VERSION.SDK_INT----->"+Build.VERSION.SDK_INT);
        //android 6.0 对应的 API版本号23
        Log.v(TAG,"Build.VERSION_CODES.M----->"+Build.VERSION_CODES.M);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {//android 6.0以上
            Log.v(TAG,"测试手机版本为：android 6.0以上");

            int writePermission = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (writePermission != PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG,"测试手机版本为：android 6.0以上--->未申请--->申请读写权限");
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);

            }else{
                Log.v(TAG,"测试手机版本为：android 6.0以上--->已申请");
            }
        }else{//android 6.0以下
            Log.v(TAG,"测试手机版本为：android 6.0以下");

        }

    }
    // 启动蓝牙发现...
    private void discovery() {
        if (mBluetoothAdapter == null) {
            init();
        }
        mBluetoothAdapter.startDiscovery();
        Log.v("ppppp","启动");
    }

    // 可选方法，非必需
    // 此方法使自身的蓝牙设备可以被其他蓝牙设备扫描到，
    // 注意时间阈值。0 - 3600 秒。
    // 通常设置时间为120秒。
    private void enable_discovery() {
        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);

        // 第二个参数可设置的范围是0~3600秒，在此时间区间（窗口期）内可被发现
        // 任何不在此区间的值都将被自动设置成120秒。
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 3600);
        startActivity(discoverableIntent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == RESULT_OK) {
                Log.d(TAG, "打开蓝牙成功！");
            }

            if (resultCode == RESULT_CANCELED) {
                Log.d(TAG, "放弃打开蓝牙！");
            }

        } else {
            Log.d(TAG, "蓝牙异常！");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_COARSE_LOCATION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                }

                break;
            case 100:
                if (permissions[0].equals(Manifest.permission.WRITE_EXTERNAL_STORAGE) && grantResults[0]
                        == PackageManager.PERMISSION_GRANTED) {//允许

                    Log.v(TAG,"测试手机版本为：android 6.0以上--->未申请--->申请读写权限--->成功！");

                } else {//拒绝

                    Log.v(TAG,"测试手机版本为：android 6.0以上--->未申请--->申请读写权限--->失败！");
                    Toast.makeText(this, "请赋予读写权限，否则应用将无法使用！", Toast.LENGTH_LONG).show();
                    MainActivity.this.finish();
                }
                break;
        }
    }


}
