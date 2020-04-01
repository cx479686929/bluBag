package com.example.back;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.back.Server.XutilsHelper;

import org.json.JSONObject;

import java.util.ArrayList;


public class SettingsActivity extends AppCompatActivity implements View.OnClickListener{
    private ArrayAdapter<String> mAdapter;
    public ArrayAdapter<String> roomsAdapter;
    public ArrayList<String> myRooms=new ArrayList<String>();
    public ListView listView;
    public String roomNumber;
    public String RoomID="dontselect";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
//        getSupportFragmentManager()
//                .beginTransaction()
//                .replace(R.id.settings, new SettingsFragment())
//                .commit();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        listView=this.findViewById(R.id.settingList);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //view.setActivated(true);
                for(int i =0;i<parent.getCount();i++){
                    parent.getChildAt(i).setBackgroundColor(Color.WHITE);
                }
                view.setBackgroundColor(Color.parseColor("#7457ED"));
                String result = parent.getItemAtPosition(position).toString();
               try {
                   int end=result.indexOf("\u00A0");
                   if(end>0)
                   RoomID=result.substring(0,end);
               }catch (Exception e){
                   e.printStackTrace();
               }



                Log.v("select",RoomID+result);
            }
        });
//        mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, android.R.id.text1);
//        ((ListView) findViewById(R.id.settingList)).setAdapter(mAdapter);
        roomsAdapter =new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, android.R.id.text1);
        ((ListView) findViewById(R.id.settingList)).setAdapter(roomsAdapter);

    }

    public boolean onSupportNavigateUp() {
        finish();
        Log.v("tttt","return");
        return super.onSupportNavigateUp();


    }

    private Handler handler=new Handler(){
        public void handleMessage(Message msg){
            super.handleMessage(msg);
            if(msg.what==0){
//                try {
//                    JSONObject res= (JSONObject) msg.obj;
//                    String  SvrKey = res.getString("SvrKey");
//                    int  SvrID=res.getInt("SvrId");
//                    String Version=res.getString("Version");
//                    String SvrCode=res.getString("SvrCode");
//                    mAdapter.add("SvrKey:"+SvrKey);
//                    mAdapter.add("SvrID:"+SvrID);
//                    mAdapter.add("Version:"+Version);
//                    mAdapter.add("SvrCode:"+SvrCode);
//                    mAdapter.notifyDataSetChanged();
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
            }
           else   if(msg.what==1){
                Log.v("post","showRooms1");
                roomsAdapter.clear();
                roomsAdapter.addAll(myRooms);
                roomsAdapter.notifyDataSetChanged();
            }
        }
    };

    public void refresh(JSONObject res){

        Message msg = handler.obtainMessage();
        msg.what = 0;
        msg.obj = res;
        handler.sendMessage(msg);

    }
    public void showRooms(){
        Message msg = handler.obtainMessage();
        msg.what = 1;
        handler.sendMessage(msg);
        Log.v("post","showRooms");
    }

    @Override
    public void onClick(View view) {
        try {


        XutilsHelper xutilsHelper=new XutilsHelper();
        switch (view.getId()){
            case R.id.registBtn:
                if(RoomID.equals("dontselect")){
                    Toast t= Toast.makeText(getBaseContext(),"请先选择教室",Toast.LENGTH_LONG);
                    t.show();
                }else{
                String ANDROID_ID = Settings.System.getString(getContentResolver(), Settings.System.ANDROID_ID);
                Log.v("androidID",ANDROID_ID);
                //XutilsHelper xutilsHelper=new XutilsHelper();

               xutilsHelper.register(ANDROID_ID,RoomID,this);
               xutilsHelper.changeState("启用");
                }
                break;
            case R.id.search_button:
                EditText text= this.findViewById(R.id.editText);
                roomNumber=text.getText().toString();
                Log.v("text",roomNumber);
                xutilsHelper.getRooms(this,roomNumber);
                break;
        } }catch (Exception e){
            e.printStackTrace();
        }
    }
}