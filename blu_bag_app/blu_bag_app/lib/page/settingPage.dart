import 'dart:convert';

import 'package:blu_bag_app/model/Room.dart';
import 'package:blu_bag_app/server/service_helper.dart';
import 'package:blu_bag_app/tools/deviceInfoHelper.dart';
import 'package:blu_bag_app/tools/sharedPreferencesHelper.dart';
import 'package:flutter/material.dart';

class settingPage extends StatefulWidget {
  @override
  _settingPageState createState() => _settingPageState();
}

class _settingPageState extends State<settingPage> {
  Rooms rooms=null;
  String Svrcode;
  @override
  void initState() {
    // TODO: implement initState
 getRooms().then((data){
      print(data.toString());
      setState(() {
       rooms=Rooms.fromJson(data);
      });
    });
getAndroidId().then((da){setState(() {
      Svrcode= da;
    });});
    ;
    super.initState();
    
   
    
  }
  @override
  

  Widget build(BuildContext context) {
    if(rooms!=null){
    return Scaffold(
      appBar: AppBar(
        title: Text('设置页面')
      ),
      body: 
      Container(
        child:ListView(children: <Widget>[
         TextFieldWidget(),
          ListView.builder(
            shrinkWrap: true,
            itemCount: rooms.data.length,
            itemBuilder: (BuildContext context, int index ){
              return Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: <Widget>[
                //Text(rooms.data[index].roomName),
                _roomItem('${rooms.data[index].roomID}',rooms.data[index].roomName,rooms.data[index].building)
              ],
            );
            })
        ],
      ) ,
      )

      
    );
  }
  else{
    return Scaffold(
      appBar: AppBar(
        title:Text('设置页面')
      ),
    );
  }

}
Widget _roomItem (String id,String name,String building){
 
  return InkWell(
    onTap: (){
      print('$id'+name+building);
     showDialog<Null>(
            context: context,
            barrierDismissible: false,
            builder: (BuildContext context) {
                return new AlertDialog(
                    content: new SingleChildScrollView(
                        child: new ListBody(
                            children: <Widget>[
                                new Text('是否注册  $building $name 教室'),
                            ],
                        ),
                    ),
                    actions: <Widget>[
                      new FlatButton(
                            child: new Text('取消'),
                            onPressed: () {
                                Navigator.of(context).pop();
                            },
                      ),
                        new FlatButton(
                            child: new Text('确定'),
                            onPressed: () {
                               Navigator.of(context).pop();
                               print(Svrcode+'1'+id);
                              registerSvr(Svrcode,'1',id).then((data1){
                                //var data=json.decode(data1);
                                if(data1['code']==200){
                                String key=data1['data']['SvrKey'];
                                int svrid=data1['data']['SvrId'];
                                print(data1['data']['SvrId']);
                                print(data1['data']['SvrKey']);
                                setKey(key);
                                setAppID(svrid);
                                }
                                //print(data['data']['SvrId']);
                                });
                               
                            },
                        ),
                    ],
                );
            },
        ).then((val) {
            print(val);
        });

    },
    child:
    Container(
      padding:EdgeInsets.fromLTRB(10.0, 0.0, 10.0, 0.0) ,
      child: Row(
    children: <Widget>[
      Container(
        
        padding:EdgeInsets.fromLTRB(10.0, 0.0, 10.0, 0.0) ,
        child: Text('$id',style: new TextStyle(fontSize:28),),
      ),
      Container(
        padding:EdgeInsets.fromLTRB(10.0, 0.0, 10.0, 0.0),
        child: Text(building,style: new TextStyle(fontSize:28)),
      ), 
      Container(
        padding:EdgeInsets.fromLTRB(10.0, 0.0, 10.0, 0.0),
        child: Text(name+'教室',style: new TextStyle(fontSize:28)),
      ),
      
       
    ],
  ) ,
  
  )
    
  );
}
  
}

class TextFieldWidget extends StatelessWidget {
 String searchRoomid;
  Widget buildTextField(TextEditingController controller) {

    // theme设置局部主题
    return Theme(
      data: new ThemeData(primaryColor: Colors.grey),
      child: new TextField(
        controller: controller,
        cursorColor: Colors.grey, // 光标颜色
        // 默认设置
        decoration: InputDecoration(
            contentPadding: const EdgeInsets.symmetric(vertical: 10.0),
            border: InputBorder.none,
            icon: Icon(Icons.search),
            hintText: "输入教室号",
            hintStyle: new TextStyle(
                fontSize: 14, color: Color.fromARGB(50, 0, 0, 0))),
        style: new TextStyle(fontSize: 14, color: Colors.black),
        onSubmitted: (text) {//内容提交(按回车)的回调
        print('submit $text');
      },
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    final controller = TextEditingController();
    controller.addListener(() {
      this.searchRoomid=controller.text;
      print('input ${controller.text} searchRoomID= $searchRoomid');
    });
    return Container(
    // 修饰搜索框, 白色背景与圆角
      decoration: new BoxDecoration(
        color: Colors.white,
        borderRadius: new BorderRadius.all(new Radius.circular(5.0)),
      ),
      alignment: Alignment.center,
      height: 50,
      padding: EdgeInsets.fromLTRB(10.0, 0.0, 10.0, 0.0),
      child: buildTextField(controller),
    );
  }


}