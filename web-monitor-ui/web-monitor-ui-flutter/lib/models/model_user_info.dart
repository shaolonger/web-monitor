class ModelUserInfo {
  int id;
  String username;
  String phone;
  String icon;
  int gender;
  String email;
  int isAdmin;
  String createTime;
  String updateTime;

  ModelUserInfo({
    this.id,
    this.username,
    this.phone,
    this.icon,
    this.gender,
    this.email,
    this.isAdmin,
    this.createTime,
    this.updateTime,
  });

  ModelUserInfo.fromJson(Map<String, dynamic> json)
      : id = json["id"],
        username = json["username"],
        phone = json["phone"],
        icon = json["icon"],
        gender = json["gender"],
        email = json["email"],
        isAdmin = json["isAdmin"],
        createTime = json["createTime"],
        updateTime = json["updateTime"];

  Map<String, dynamic> toJson() => <String, dynamic>{
        "id": id,
        "username": username,
        "phone": phone,
        "icon": icon,
        "gender": gender,
        "email": email,
        "isAdmin": isAdmin,
        "createTime": createTime,
        "updateTime": updateTime,
      };
}
