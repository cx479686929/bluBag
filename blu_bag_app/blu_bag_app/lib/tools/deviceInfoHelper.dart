import 'package:device_info/device_info.dart';

Future getAndroidId() async{
  DeviceInfoPlugin deviceInfo = DeviceInfoPlugin();
AndroidDeviceInfo androidInfo = await deviceInfo.androidInfo;
//print('Running on ${androidInfo.androidId}');
return androidInfo.androidId.toString() ;
}


