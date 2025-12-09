package service.auth.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
public class GatewayRouteConfig implements IGatewayRouteConfig { // Implements interface (DIP)

    private final Map<String, String> serviceRoutes = new HashMap<>();

    // Use direct injection for the service URIs
    @Value("${study.service.ip}")
    private String studyServiceURI;

    @Value("${submit.service.ip}")
    private String submitServiceURI;

    @PostConstruct
    public void initRoutes() {
        // Initialize the map once from configuration properties (OCP)
        serviceRoutes.put("/api/study/", studyServiceURI);
        serviceRoutes.put("/api/submit/", submitServiceURI);
    }

    @Override
    public String resolveService(String path) {
        // Use map lookup for resolution, improving OCP
        for (Map.Entry<String, String> entry : serviceRoutes.entrySet()) {
            if (path.startsWith(entry.getKey())) {
                return entry.getValue();
            }
        }
        return null; // Not matched
    }
}