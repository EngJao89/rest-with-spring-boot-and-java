package engjao89.rest_with_spring_boot_and_java.config;

public interface TestConfigs {
    int SERVER_PORT = 8888;

    String HEADER_PARAM_AUTHORIZATION = "Authorization";
    String HEADER_PARAM_ORIGIN = "Origin";

    String ORIGIN_LOCAL = "http://localhost:8080";
    String ORIGIN_WEB = "http://localhost:3000";
}
