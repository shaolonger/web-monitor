import 'dart:convert';

class ModelProfile {
  LoginUser loginUser;
  int theme;

  ModelProfile({
    this.loginUser,
    this.theme,
  });

  ModelProfile.fromJson(Map<String, dynamic> jsonStr)
      : loginUser = json.decode(jsonStr["loginUser"]),
        theme = jsonStr["theme"];

  Map<String, dynamic> toJson() => <String, dynamic>{
        "loginUser": json.encode(loginUser),
        "theme": theme,
      };
}

class LoginUser {
  int id;
  String username;
  int isAdmin;
  String token;

  LoginUser({
    this.id,
    this.username,
    this.isAdmin,
    this.token,
  });

  LoginUser.fromJson(Map<String, dynamic> json)
      : id = json["id"],
        username = json["username"],
        isAdmin = json["isAdmin"],
        token = json["token"];

  Map<String, dynamic> toJson() => <String, dynamic>{
    "id": id,
    "username": username,
    "isAdmin": isAdmin,
    "token": token,
  };
}
