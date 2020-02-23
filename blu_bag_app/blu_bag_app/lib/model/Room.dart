class Rooms {
  int code;
  String msg;
  List<Data> data;
  Rooms({this.code, this.msg, this.data});

  Rooms.fromJson(Map<String, dynamic> json) {
    code = json['code'];
    msg = json['msg'];
    if (json['data'] != null) {
      data = new List<Data>();
      json['data'].forEach((v) {
        data.add(new Data.fromJson(v));
      });
    }
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['code'] = this.code;
    data['msg'] = this.msg;
    if (this.data != null) {
      data['data'] = this.data.map((v) => v.toJson()).toList();
    }
    return data;
  }
}

class Data {
  int roomID;
  String roomName;
  int siteCount;
  String building;

  Data({this.roomID, this.roomName, this.siteCount, this.building});

  Data.fromJson(Map<String, dynamic> json) {
    roomID = json['roomID'];
    roomName = json['roomName'];
    siteCount = json['siteCount'];
    building = json['building'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['roomID'] = this.roomID;
    data['roomName'] = this.roomName;
    data['siteCount'] = this.siteCount;
    data['building'] = this.building;
    return data;
  }
}