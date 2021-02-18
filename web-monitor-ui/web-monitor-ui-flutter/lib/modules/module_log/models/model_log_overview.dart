class ModelLogOverview {
  ModelLogOverviewItem count = ModelLogOverviewItem();
  ModelLogOverviewItem affectUV = ModelLogOverviewItem();
  ModelLogOverviewItem affectUVPercent = ModelLogOverviewItem();

  ModelLogOverview({
    this.count = const ModelLogOverviewItem(),
    this.affectUV = const ModelLogOverviewItem(),
    this.affectUVPercent = const ModelLogOverviewItem(),
  });
}

class ModelLogOverviewItem {
  final dynamic yesterday;
  final dynamic today;
  final int change;
  final String rate;

  const ModelLogOverviewItem({
    this.yesterday = 0,
    this.today = 0,
    this.change = 0,
    this.rate = "-",
  });
}
