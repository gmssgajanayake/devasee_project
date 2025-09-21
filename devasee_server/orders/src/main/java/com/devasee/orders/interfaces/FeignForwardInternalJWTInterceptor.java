package com.devasee.orders.interfaces;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
public class FeignForwardInternalJWTInterceptor implements RequestInterceptor {

    private static final String INTERNAL_JWT_HEADER = "X-Internal-JWT";

    @Override
    public void apply(RequestTemplate requestTemplate) {
        // Get the current HTTP request
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        if (attrs != null) {
            HttpServletRequest request = attrs.getRequest();
            String internalJwt = request.getHeader(INTERNAL_JWT_HEADER);
            if (internalJwt != null) {
                // Forward the internal JWT to Inventory
                requestTemplate.header(INTERNAL_JWT_HEADER, internalJwt);
            }
        }
    }
}
