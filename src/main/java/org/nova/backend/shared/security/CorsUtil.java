package org.nova.backend.shared.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Set;

public class CorsUtil {
    private static final Set<String> ALLOWED_ORIGINS = Set.of(
            "https://nova.cbnu.ac.kr",
            "http://localhost:8080",
            "http://localhost:3000",
            "http://localhost:3001",
            "http://localhost:3002"
    );

    public static void setAllowedOriginsHeader(HttpServletRequest request, HttpServletResponse response) {
        String origin = request.getHeader("Origin");

        if (isSafeOrigin(origin) && ALLOWED_ORIGINS.contains(origin)) {
            response.setHeader("Access-Control-Allow-Origin", origin);
        }

        response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
        response.setHeader("Access-Control-Allow-Credentials", "true");
    }

    private static boolean isSafeOrigin(String origin) {
        return origin != null && !origin.contains("\r") && !origin.contains("\n");
    }
} 