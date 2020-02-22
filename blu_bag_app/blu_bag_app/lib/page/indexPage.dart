import 'package:blu_bag_app/page/homePage.dart';
import 'package:blu_bag_app/page/settingPage.dart';
import 'package:flutter/material.dart';
import 'package:flutter/cupertino.dart';

class IndexPage extends StatefulWidget {
  @override
  _IndexPageState createState() => _IndexPageState();
}

class _IndexPageState extends State<IndexPage> {
  //底部导航栏
  final List<BottomNavigationBarItem> bottomTabs=[
    BottomNavigationBarItem(
        icon: Icon(CupertinoIcons.group_solid),
        title: Text('home')
    ),
    BottomNavigationBarItem(
        icon: Icon(CupertinoIcons.settings),
        title: Text('setting')
    )
    
  ];

  final List tabBodies=[
    homePage(),
    settingPage()
  ];
  int currentIndex=0;
  var currentPage;

  void initState(){
    currentPage=tabBodies[currentIndex];
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Color.fromRGBO(244,245,245,1.0),
      bottomNavigationBar: BottomNavigationBar(
        type: BottomNavigationBarType.fixed,
        currentIndex: currentIndex,
        items: bottomTabs,
        onTap: (index){
          setState(() {
            currentIndex=index;
            currentPage=tabBodies[index];
          });
        },
      ),
      body: currentPage,
    );

  }
}

