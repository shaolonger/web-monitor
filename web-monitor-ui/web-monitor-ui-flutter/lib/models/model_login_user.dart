class ModelLoginUser {
  int id;
  String username;
  int isAdmin;
  String token;

  ModelLoginUser({
    this.id,
    this.username,
    this.isAdmin,
    this.token,
  });

  ModelLoginUser.fromJson(Map<String, dynamic> json)
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