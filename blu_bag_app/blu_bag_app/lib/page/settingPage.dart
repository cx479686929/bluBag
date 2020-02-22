import 'package:flutter/material.dart';

class settingPage extends StatefulWidget {
  @override
  _settingPageState createState() => _settingPageState();
}

class _settingPageState extends State<settingPage> {
  
  @override
  void initState() {
    // TODO: implement initState
    super.initState();
    
  }
  @override
  

  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('设置页面')
      ),
      body: 
      Container(child:Column(children: <Widget>[
          TextFieldWidget(),

        ],
      ) ,
      )

      
    );
  }


  
}

class TextFieldWidget extends StatelessWidget {
  Widget buildTextField() {
    // theme设置局部主题
    return Theme(
      data: new ThemeData(primaryColor: Colors.grey),
      child: new TextField(
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
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    return Container(
    // 修饰搜索框, 白色背景与圆角
      decoration: new BoxDecoration(
        color: Colors.white,
        borderRadius: new BorderRadius.all(new Radius.circular(5.0)),
      ),
      alignment: Alignment.center,
      height: 50,
      padding: EdgeInsets.fromLTRB(10.0, 0.0, 10.0, 0.0),
      child: buildTextField(),
    );
  }
}