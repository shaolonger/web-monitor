class EnvironmentConfig {
  /// ENV为运行环境，包括dev-开发环境，test-测试环境，prod-生产环境
  static const ENV = String.fromEnvironment(
    "ENV",
    defaultValue: "dev",
  );
}
