package com.example.back;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.back.DataBase.DBHelper;
import com.example.back.Server.XutilsFactory;

public class ConfigureActivity extends AppCompatActivity implements View.OnClickListener {
public EditText sendingIntervalEdit;
public EditText heartIntervalEdit;
public EditText checkExceptionIntervalEdit;
public Button saveBtn;
public Button banBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configure);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("配置");
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        setEdit();
        saveBtn=this.findViewById(R.id.savebtn);
        saveBtn.setOnClickListener(this);
        banBtn=this.findViewById(R.id.ban_btn);
        banBtn.setOnClickListener(this);

    }

    public boolean onSupportNavigateUp() {
        finish();
        Log.v("tttt","return");
        return super.onSupportNavigateUp();
    }
    public void setEdit(){
        sendingIntervalEdit=this.findViewById(R.id.sendingInterval_edit);
        heartIntervalEdit=this.findViewById(R.id.heartInterval_edit);
        checkExceptionIntervalEdit=this.findViewById(R.id.checkExceptionInterval_edit);
        DBHelper.initConfig();
        sendingIntervalEdit.setText(DBHelper.getSendingInterval()+"");
        heartIntervalEdit.setText(DBHelper.getHeartInterval()+"");
        checkExceptionIntervalEdit.setText(DBHelper.getCheckExceptionInterval()+"");
    }
    public void save(){
        try {
            int sendInterval=Integer.parseInt(sendingIntervalEdit.getText().toString());
            int heartInterval=Integer.parseInt(heartIntervalEdit.getText().toString());
            int checkExceptionInterval=Integer.parseInt(checkExceptionIntervalEdit.getText().toString());
            DBHelper.saveConfig(sendInterval,heartInterval,checkExceptionInterval);
            Log.v("Config","保存成功");
        }catch (Exception e){
            Log.v("Config","保存失败");
        }
    }
    public void ban(){
        new XutilsFactory(3,"禁用").start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.savebtn:
                    save();
                    break;
            case R.id.ban_btn:
                    ban();
                    break;
        }
    }
}
