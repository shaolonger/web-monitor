class ModelLogCountBetweenDiffDate {
  List jsErrorLog;
  List httpErrorLog;
  List resourceLoadErrorLog;
  List customErrorLog;

  ModelLogCountBetweenDiffDate({
    this.jsErrorLog,
    this.httpErrorLog,
    this.resourceLoadErrorLog,
    this.customErrorLog,
  });

  ModelLogCountBetweenDiffDate.fromJson(Map<String, dynamic> json) {
    this.jsErrorLog = json["jsErrorLog"];
    this.httpErrorLog = json["httpErrorLog"];
    this.resourceLoadErrorLog = json["resourceLoadErrorLog"];
    this.customErrorLog = json["customErrorLog"];
  }
}
