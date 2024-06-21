package pl.bodzioch.damian.configuration.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.proc.SecurityContext;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import pl.bodzioch.damian.dto.AppErrorResponse;
import pl.bodzioch.damian.dto.ErrorDto;
import pl.bodzioch.damian.utils.MessageResolver;

import java.util.Collections;
import java.util.List;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(RsaKeyProperties.class)
class SecurityConfig {

    private static final String ERROR_CLIENT_AUTHENTICATION_MESSAGE_CODE = "error.client.authenticationError";

    private final MessageResolver messageResolver;
    private final RsaKeyProperties rsaKeys;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, CorsConfigurationSource corsConfig) throws Exception {
        httpSecurity.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
                .oauth2ResourceServer(oAuth2 -> oAuth2.jwt(Customizer.withDefaults()))
                .cors(cors -> cors.configurationSource(corsConfig))
                .httpBasic(basicCustomizer());

        return httpSecurity.build();
    }

    @Bean
    @Primary
    @Profile("local")
    CorsConfigurationSource localCorsConfig() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Collections.singletonList("http://localhost:4200"));
        configuration.setAllowedMethods(Collections.singletonList("*"));
        configuration.setAllowedHeaders(Collections.singletonList("*"));
        configuration.setExposedHeaders(Collections.singletonList("Content-Disposition"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    @Primary
    @Profile("prod")
    CorsConfiguration prodCorsConfig() {
        return new CorsConfiguration();
    }

    @Bean
    @DependsOn(value = {"passwordEncoder", "simpleUserDetailsService"})
    public AuthenticationManager authenticationManager(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        return new ProviderManager(authenticationProvider);
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withPublicKey(rsaKeys.publicKey()).build();
    }

    @Bean
    JwtEncoder jwtEncoder() {
        JWK jwk = new RSAKey.Builder(rsaKeys.publicKey()).privateKey(rsaKeys.privateKey()).build();
        ImmutableJWKSet<SecurityContext> jwkSet = new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(jwkSet);
    }

    private Customizer<HttpBasicConfigurer<HttpSecurity>> basicCustomizer() {
        return basic -> basic.authenticationEntryPoint((request, response, authException) -> {
            String message = messageResolver.getMessage(ERROR_CLIENT_AUTHENTICATION_MESSAGE_CODE, request);
            ErrorDto error = new ErrorDto(ERROR_CLIENT_AUTHENTICATION_MESSAGE_CODE, message);
            AppErrorResponse errorResponse = new AppErrorResponse(List.of(error));
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            new ObjectMapper().registerModule(new JavaTimeModule())
                    .writeValue(response.getOutputStream(), errorResponse);
        });
    }
}
