class ModelLogOverview {
  ModelLogOverviewItem count;
  ModelLogOverviewItem affectUV;
  ModelLogOverviewItem affectUVPercent;

  ModelLogOverview({
    this.count,
    this.affectUV,
    this.affectUVPercent,
  });
}

class ModelLogOverviewItem {
  dynamic yesterday;
  dynamic today;
  int change;
  String rate;

  ModelLogOverviewItem({
    this.yesterday = 0,
    this.today = 0,
    this.change = 0,
    this.rate = "-",
  });
}
