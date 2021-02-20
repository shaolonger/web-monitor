class ModelLoginSetting {
  final bool isRememberPassword;
  final bool isAutoLogin;
  final String username;
  final String password;

  const ModelLoginSetting({
    this.isRememberPassword = false,
    this.isAutoLogin = false,
    this.username = "",
    this.password = "",
  });

  ModelLoginSetting.fromJson(Map<String, dynamic> json)
      : isRememberPassword = json["isRememberPassword"],
        isAutoLogin = json["isAutoLogin"],
        username = json["username"],
        password = json["password"];

  Map<String, dynamic> toJson() => <String, dynamic>{
        "isRememberPassword": isRememberPassword,
        "isAutoLogin": isAutoLogin,
        "username": username,
        "password": password,
      };
}
