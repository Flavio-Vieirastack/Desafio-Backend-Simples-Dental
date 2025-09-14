package com.simplesdental.product.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

@SpringJUnitConfig(classes = SecurityConfigTest.JwtTestConfig.class)
class SecurityConfigTest {

    static class JwtTestConfig {
        @Bean
        public KeyPair keyPair() throws Exception {
            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
            generator.initialize(2048);
            return generator.generateKeyPair();
        }

        @Bean(name = "testJwtEncoder")
        public JwtEncoder jwtEncoder(KeyPair keyPair) {
            var jwk = new com.nimbusds.jose.jwk.RSAKey.Builder((RSAPublicKey) keyPair.getPublic())
                    .privateKey((RSAPrivateKey) keyPair.getPrivate())
                    .build();
            var jwks = new com.nimbusds.jose.jwk.source.ImmutableJWKSet<>(new com.nimbusds.jose.jwk.JWKSet(jwk));
            return new NimbusJwtEncoder(jwks);
        }

        @Bean(name = "testJwtDecoder")
        public JwtDecoder jwtDecoder(KeyPair keyPair) {
            return NimbusJwtDecoder.withPublicKey((RSAPublicKey) keyPair.getPublic()).build();
        }
    }

    @Autowired
    @Qualifier("testJwtEncoder")
    private JwtEncoder jwtEncoder;

    @Autowired
    @Qualifier("testJwtDecoder")
    private JwtDecoder jwtDecoder;

    @Test
    void testJwtEncodeDecode() {
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .subject("user123")
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(3600))
                .build();

        var token = jwtEncoder.encode(JwtEncoderParameters.from(claims));
        assertThat(token.getTokenValue()).isNotNull();

        var decoded = jwtDecoder.decode(token.getTokenValue());
        assertThat(decoded.getSubject()).isEqualTo("user123");
    }
}
