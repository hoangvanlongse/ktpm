package service.auth.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class GatewayRouteConfig {

    @Value("${study.service.ip}")
    private String studyServiceURI;

    @Value("${submit.service.ip}")
    private String submitServiceURI;

    public String resolveService(String path) {
        if (path.startsWith("/api/study/"))
            return studyServiceURI;
        if (path.startsWith("/api/submit/"))
            return submitServiceURI;

        return null; // Not matched
    }
}
