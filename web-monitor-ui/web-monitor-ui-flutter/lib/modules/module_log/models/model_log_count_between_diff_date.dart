class ModelLogCountBetweenDiffDate {
  List<dynamic> jsErrorLog;
  List<dynamic> httpErrorLog;
  List<dynamic> resourceLoadErrorLog;
  List<dynamic> customErrorLog;

  ModelLogCountBetweenDiffDate({
    this.jsErrorLog,
    this.httpErrorLog,
    this.resourceLoadErrorLog,
    this.customErrorLog,
  });

  ModelLogCountBetweenDiffDate.fromJson(
      Map<String, dynamic> json) {
    this.jsErrorLog = json["jsErrorLog"];
    this.httpErrorLog = json["httpErrorLog"];
    this.resourceLoadErrorLog = json["resourceLoadErrorLog"];
    this.customErrorLog = json["customErrorLog"];
  }
}
