import 'package:shared_preferences/shared_preferences.dart';

setKey(String key) async {
  SharedPreferences share=await SharedPreferences.getInstance();
  share.setString("key", key);
}

setSvrCode(String SvrCode)async{
  SharedPreferences share=await SharedPreferences.getInstance();
  share.setString("SvrCode",SvrCode);
}

setAppID(int AppId)async{
SharedPreferences share=await SharedPreferences.getInstance();
  share.setInt("AppId",AppId);
}

Future getKey()async{
  SharedPreferences share=await SharedPreferences.getInstance();
  String  key=share.getString("key");
  return key;
}

Future getSvrCode()async{
  SharedPreferences share=await SharedPreferences.getInstance();
  String  SvrCode=share.getString("SvrCode");
  return SvrCode;
}

Future getAppID()async{
  SharedPreferences share=await SharedPreferences.getInstance();
  String  AppID=share.getString("AppId");
  return AppID;
}

