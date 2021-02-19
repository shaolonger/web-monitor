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

  ModelLoginUser.fromJson(Map<String, dynamic> json) {
    this.id = json["id"];
    this.username = json["username"];
    this.isAdmin = json["isAdmin"];
    this.token = json["token"];
  }

  Map<String, dynamic> toJson() => <String, dynamic>{
        "id": id,
        "username": username,
        "isAdmin": isAdmin,
        "token": token,
      };
}
