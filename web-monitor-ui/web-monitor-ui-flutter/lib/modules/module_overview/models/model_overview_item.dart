class ModelOverviewListItem {
  int projectId;
  String projectName;
  String projectIdentifier;
  ModelOverviewData data;

  ModelOverviewListItem({
    this.projectId,
    this.projectName,
    this.projectIdentifier,
    this.data,
  });

  ModelOverviewListItem.fromJson(Map<String, dynamic> json) {
    this.projectId = json["projectId"];
    this.projectName = json["projectName"];
    this.projectIdentifier = json["projectIdentifier"];
    this.data = ModelOverviewData.fromJson(json["data"]);
  }
}

class ModelOverviewData {
  int jsErrorLogCount;
  int httpErrorLogCount;
  int resourceLoadErrorLogCount;
  int customErrorLogCount;

  ModelOverviewData({
    this.jsErrorLogCount,
    this.httpErrorLogCount,
    this.resourceLoadErrorLogCount,
    this.customErrorLogCount,
  });

  ModelOverviewData.fromJson(Map<String, dynamic> json) {
    this.jsErrorLogCount = json["jsErrorLogCount"];
    this.httpErrorLogCount = json["httpErrorLogCount"];
    this.resourceLoadErrorLogCount = json["resourceLoadErrorLogCount"];
    this.customErrorLogCount = json["customErrorLogCount"];
  }
}
