class ModelUserInfo {
  final int id;
  final String username;
  final String phone;
  final String icon;
  final int gender;
  final String email;
  final int isAdmin;
  final String createTime;
  final String updateTime;

  const ModelUserInfo({
    this.id = 0,
    this.username = "",
    this.phone = "",
    this.icon = "",
    this.gender = 0,
    this.email = "",
    this.isAdmin = 0,
    this.createTime = "",
    this.updateTime = "",
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
