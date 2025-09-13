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
        // Create Base Decoder
        // Auto-configures a JWT decoder using Clerk’s /.well-known/jwks.json endpoint.
        NimbusReactiveJwtDecoder jwtDecoder = ReactiveJwtDecoders.fromIssuerLocation(clerkIssuerUri);

        // Add audience & issuer validation
        // Ensures that the JWT’s iss claim matches your Clerk issuer URI.
        // Prevents tokens from another identity provider from being accepted.
        OAuth2TokenValidator<Jwt> withIssuer = JwtValidators.createDefaultWithIssuer(clerkIssuerUri);
        OAuth2TokenValidator<Jwt> withAudience = new JwtClaimValidator<String>(
                "azp",
                azp -> azp != null &&
                        (azp.equals(expectedAudienceCustomer) || azp.equals(expectedAudienceAdmin))
        );

        // Both validations are applied -> Issuer must be Clerk, Audience must relevant urls
        jwtDecoder.setJwtValidator(new DelegatingOAuth2TokenValidator<>(withIssuer, withAudience)); // (withIssuer,withAudience)

        return jwtDecoder; // Return Configured Decoder
    }
}
