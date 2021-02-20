class ModelLoginUser {
  final int id;
  final String username;
  final int isAdmin;
  final String token;

  const ModelLoginUser({
    this.id = 0,
    this.username = "",
    this.isAdmin = 0,
    this.token = "",
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
