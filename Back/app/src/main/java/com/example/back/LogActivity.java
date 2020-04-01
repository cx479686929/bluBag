package com.example.back;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.back.Tools.MyLog;

import java.util.Calendar;
import java.util.Date;

public class LogActivity extends AppCompatActivity {
   public TextView textView;
   public Button choseDateBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("日志信息");
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
       textView=this.findViewById(R.id.LogText);
        choseDateBtn=this.findViewById(R.id.choseDateBtn);
        choseDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
       getLogtext();
    }
public void showDialog(){
    DatePickerDialog dp = new DatePickerDialog(this,
            new DatePickerDialog.OnDateSetListener(){
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                     //Toast.makeText(LogActivity.this, "年：" + year+"\n月：" + monthOfYear+"\n日：" + dayOfMonth, Toast. LENGTH_LONG).show();
                     getLogtext(year,monthOfYear+1,dayOfMonth);

                }
            }, 2020, 3, 29);
    dp.getDatePicker(); //获取dialog中的DatePicker控件。
    Date date=new Date();
    //date.getYear();
    Calendar c = Calendar.getInstance();
    c.get(Calendar.YEAR);
    dp.updateDate(c.get(Calendar.YEAR), c.get(Calendar.MONTH),c.get(Calendar.DAY_OF_MONTH)); //更新dialog的年月日。
    dp.show();

}
   public void getLogtext(){
        new Thread(){
            public void run(){
               String mylog= MyLog.readFile();
               Message msg=handler.obtainMessage();
               msg.what=0;
               msg.obj=mylog;
               handler.sendMessage(msg);
            }
        }.start();
   }
   public void getLogtext(final int year, final int month, final int day){
       new Thread(){
           public void run(){
               String mylog= MyLog.readFile(year+"-"+String.format("%02d",month)+"-"+String.format("%02d",day));
               Message msg=handler.obtainMessage();
               msg.what=0;
               msg.obj=mylog;
               handler.sendMessage(msg);
           }
       }.start();
   }
    public boolean onSupportNavigateUp() {
        finish();
        Log.v("tttt","return");
        return super.onSupportNavigateUp();


    }
   public  Handler handler=new Handler(){
       public void handleMessage(Message msg) {
           super.handleMessage(msg);
           if (msg.what == 0) {
               String mylog=(String)msg.obj;
               textView.setText(mylog);

           }
       }
   };
}
