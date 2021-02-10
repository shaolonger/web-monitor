class ModelProjectInfo {
  int id;
  String projectName;
  String projectIdentifier;
  String description;
  String accessType;
  String activeFuncs;
  int isAutoUpload;
  String notifyDtToken;
  String notifyEmail;
  String createTime;
  String updateTime;

  ModelProjectInfo({
    this.id,
    this.projectName,
    this.projectIdentifier,
    this.description,
    this.accessType,
    this.activeFuncs,
    this.isAutoUpload,
    this.notifyDtToken,
    this.notifyEmail,
    this.createTime,
    this.updateTime,
  });

  ModelProjectInfo.fromJson(Map<String, dynamic> json)
      : id = json["id"],
        projectName = json["projectName"],
        projectIdentifier = json["projectIdentifier"],
        description = json["description"],
        accessType = json["accessType"],
        activeFuncs = json["activeFuncs"],
        isAutoUpload = json["isAutoUpload"],
        notifyDtToken = json["notifyDtToken"],
        notifyEmail = json["notifyEmail"],
        createTime = json["createTime"],
        updateTime = json["updateTime"];

  Map<String, dynamic> toJson() => <String, dynamic>{
        "id": id,
        "projectName": projectName,
        "projectIdentifier": projectIdentifier,
        "description": description,
        "accessType": accessType,
        "activeFuncs": activeFuncs,
        "isAutoUpload": isAutoUpload,
        "notifyDtToken": notifyDtToken,
        "notifyEmail": notifyEmail,
        "createTime": createTime,
        "updateTime": updateTime,
      };
}
