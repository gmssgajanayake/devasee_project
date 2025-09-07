package com.devasee.apigateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.*;

@Configuration
public class ClerkJwtDecoderConfig {

    @Value("${clerk.issuer-uri}")
    private String clerkIssuerUri;

    @Value("${clerk.expected-audience-customer}")
    private String expectedAudienceCustomer;

    @Value("${clerk.expected-audience-admin}")
    private String expectedAudienceAdmin;

    public ReactiveJwtDecoder jwtDecoder() {
        NimbusReactiveJwtDecoder jwtDecoder = ReactiveJwtDecoders.fromIssuerLocation(clerkIssuerUri);

        // Add audience & issuer validation
        OAuth2TokenValidator<Jwt> withIssuer = JwtValidators.createDefaultWithIssuer(clerkIssuerUri);
        OAuth2TokenValidator<Jwt> withAudience = new JwtClaimValidator<String>(
                "azp",
                azp -> azp != null &&
                        (azp.equals(expectedAudienceCustomer) || azp.equals(expectedAudienceAdmin))
        );

        jwtDecoder.setJwtValidator(new DelegatingOAuth2TokenValidator<>(withIssuer, withAudience)); // (withIssuer,withAudience)

        return jwtDecoder;
    }
}
