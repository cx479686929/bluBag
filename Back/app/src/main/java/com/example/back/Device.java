package com.example.back;

import android.util.Log;

import java.util.List;

public class Device {
    public String name;
    public String macAddress;
    public String distance;
    public boolean displayflag=false;
    public boolean sendflag=false;
    public int life=30;
    public String stuName;
    public Device(String name,String macAddress,String distance){
        this.name=name;
        this.macAddress=macAddress;
        this.distance=distance;
    }
    public boolean isInList(List<Device> devices){
        for(int i=0;i<devices.size();i++){
            Device device=devices.get(i);
            if(this.macAddress.equals(device.macAddress)){
                //刷新距离
                if(this.name.equals(device.name)){
                    device.name=this.name;
                }
                device.distance=this.distance;
                return true;
            }
        }
        return false;
    }
    public void reduceLife(){
        if(this.life>0)
        this.life--;
    }
    public void fullLife(){
        this.life=30;
    }
    public boolean isAlive(){
        if(this.life==0){
            return false;
        }
        else {
            return true;
        }
    }


}
