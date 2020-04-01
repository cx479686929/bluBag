package com.example.back.Tools;

import java.io.UnsupportedEncodingException;

public class StringFormat {
    public static String changeFormat(String msg,int needLen){
        try {
            int len=msg.length();
            int bytelen=msg.getBytes("UTF-8").length;
            int count=(bytelen-len)/2;//中文个数*2 也就等于中文占的格数
            int needspace= needLen-len-count;//需要补的空格数
            if(needspace<0){//空格超出范围
                msg=msg.substring(0,len+needspace-1);
                return msg;
            }
            for(int i=0;i<needspace;i++){
                msg=msg+" ";
            }
            return msg;

        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;

    }
}
