import 'dart:convert';

import 'package:dio/dio.dart';
import 'dart:async';
import 'dart:io';
import '../config/service_url.dart';

Future getRooms() async{
  try{
    print('开始教室信息');
    Response response;
    Dio dio=new Dio();
    dio.options.contentType=ContentType.parse("application/x-www-form-urlencoded");
    response=await dio.get(servicePath['getAllRoom']);
    if(response.statusCode==200){
      return response.data;
    }else{
      throw Exception('后端接口异常');
    }
  }catch(e){
    return print('ERROR:========>${e}');
  }


}
Future RegisterSvr(String code,String version,String roomid) async{
  try{
    print('开始注册');
    Response response;
    Dio dio=new Dio();
     dio.options.contentType=ContentType.parse("application/x-www-form-urlencoded");
    //dio.options.contentType=ContentType.parse("application/json; charset=utf-8");
    var data={
      'code':code,
      'version':version,
      'roomid':roomid,
    };
    response=await dio.post(servicePath['register'],queryParameters: data);
    if(response.statusCode==200){
      return response.data;
    }else{
      throw Exception('后端接口异常');
    }
  }catch(e){
    return print('ERROR:========>${e}');
  }


}

Future signinOne(String mac,String stuId)async{
  try{
    print('开始注册');
    Response response;
    Dio dio=new Dio();
    dio.options.contentType=ContentType.parse("application/x-www-form-urlencoded");
    var data={
      'MAC':mac,
      'studId':stuId,
      
    };
    response=await dio.post(servicePath['sign-in'],queryParameters: data);
    if(response.statusCode==200){
      return response.data;
    }else{
      throw Exception('后端接口异常');
    }
  }catch(e){
    return print('ERROR:========>${e}');
  }
}
