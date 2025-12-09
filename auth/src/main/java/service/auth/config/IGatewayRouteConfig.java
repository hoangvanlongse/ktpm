package service.auth.config;

public interface IGatewayRouteConfig {
    String resolveService(String path);
}