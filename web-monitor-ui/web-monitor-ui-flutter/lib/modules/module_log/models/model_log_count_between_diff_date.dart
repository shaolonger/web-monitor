class ModelLogCountBetweenDiffDate {
  List<Map<String, dynamic>> jsErrorLog;
  List<Map<String, dynamic>> httpErrorLog;
  List<Map<String, dynamic>> resourceLoadErrorLog;
  List<Map<String, dynamic>> customErrorLog;

  ModelLogCountBetweenDiffDate({
    this.jsErrorLog,
    this.httpErrorLog,
    this.resourceLoadErrorLog,
    this.customErrorLog,
  });

  ModelLogCountBetweenDiffDate.fromJson(
      Map<String, List<Map<String, dynamic>>> json) {
    this.jsErrorLog = json["jsErrorLog"];
    this.httpErrorLog = json["httpErrorLog"];
    this.resourceLoadErrorLog = json["resourceLoadErrorLog"];
    this.customErrorLog = json["customErrorLog"];
  }
}
