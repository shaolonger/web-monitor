class EnvironmentConfig {
  // ENV为运行环境，包括dev-开发环境，test-测试环境，prod-生产环境
  static const ENV = String.fromEnvironment(
    "ENV",
    defaultValue: "dev",
  );

  // URL前缀地址
  // 注意，由于在Android Emulator中无法直接访问主机的localhost，
  // 因此需要通过访问10.0.2.2的访问间接访问，
  // 或者，也可以通过在Emulator->Setting中设置Proxy代理来实现
  // 参考资料：https://stackoverflow.com/questions/5528850/how-do-you-connect-localhost-in-the-android-emulator
  static const String API_URL =
      ENV == "dev" ? "http://10.0.2.2:6001" : "http://10.0.2.2:6001";
}
