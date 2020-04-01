package com.example.back.Server;

import com.example.back.Device;

import java.util.ArrayList;

public class XutilsFactory extends Thread {
    public static XutilsHelper helper=new XutilsHelper();
    public int type;
    public ArrayList<Device> devices;
    public Device device;
    public String state;
    public XutilsFactory(int type,ArrayList<Device> devices){
        this.type=type;
        this.devices=devices;
    }
    public XutilsFactory(int type,Device device){
        this.type=type;
        this.device=device;

    }
    public XutilsFactory(int type,String state){
        this.type=type;
        this.state=state;
    }
    public void run(){
        switch (type){
            case 1:helper.sendArr(devices);
                    break;
            case 2:helper.sendDevice(device);
                    break;
            case 3:helper.changeState(state);
                    break;
        }
    }
}
