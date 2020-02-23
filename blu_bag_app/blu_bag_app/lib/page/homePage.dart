import 'package:blu_bag_app/server/service_helper.dart';
import 'package:blu_bag_app/tools/deviceInfoHelper.dart';
import 'package:blu_bag_app/tools/sharedPreferencesHelper.dart';
import 'package:flutter/material.dart';

class homePage extends StatefulWidget {
  @override
  _homePageState createState() => _homePageState();
}

class _homePageState extends State<homePage> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar:AppBar(
        title: Text("工作页面"),
      ) ,
      body: RaisedButton(onPressed:(){
        
        print("hello");
        //getRooms().then((data){print(data);});
       // getAndroidId().then((data){print(data);});
       registerSvr('523f4869fdb6a0a1','1','1');
       getSvrCode().then((data){print("this Svr code ="+data);});
       
        },
      child: Text("测试"),
      ),
    );
  }
}