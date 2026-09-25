package it.case_vacanze.manager.config;

import java.nio.charset.StandardCharsets;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.jwk.source.ImmutableSecret;

import it.case_vacanze.manager.exception.ErrorResponse;

// Chi può chiamare cosa. Il login restituisce un token JWT che il frontend
// manda in ogni richiesta (header "Authorization: Bearer <token>").
@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, ObjectMapper objectMapper) throws Exception {
        AuthenticationEntryPoint nonAutenticato = (request, response, ex) ->
                scriviErrore(response, objectMapper, HttpStatus.UNAUTHORIZED, "Devi effettuare il login");
        AccessDeniedHandler senzaPermessi = (request, response, ex) ->
                scriviErrore(response, objectMapper, HttpStatus.FORBIDDEN, "Non hai i permessi per questa operazione");

        http
                .csrf(csrf -> csrf.disable())          // API senza cookie di sessione: il CSRF non si applica
                .cors(Customizer.withDefaults())       // usa le regole di CorsConfig
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // pubbliche
                        .requestMatchers(HttpMethod.POST, "/clienti", "/clienti/login", "/newsletter").permitAll()
                        .requestMatchers(HttpMethod.GET, "/stanze/**", "/offerte", "/recensioni").permitAll()
                        // solo amministratore
                        .requestMatchers(HttpMethod.GET, "/clienti", "/prenotazione", "/newsletter").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/offerte").hasRole("ADMIN")
                        // tutto il resto (dati personali, prenotare, recensire): basta essere loggati
                        .anyRequest().authenticated())
                .oauth2ResourceServer(oauth -> oauth
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(ruoloDalToken()))
                        .authenticationEntryPoint(nonAutenticato))
                .exceptionHandling(e -> e
                        .authenticationEntryPoint(nonAutenticato)
                        .accessDeniedHandler(senzaPermessi));
        return http.build();
    }

    // Chiave con cui si firmano e verificano i token (almeno 32 caratteri)
    @Bean
    public SecretKey chiaveJwt(@Value("${app.jwt.secret}") String segreto) {
        return new SecretKeySpec(segreto.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
    }

    @Bean
    public JwtEncoder jwtEncoder(SecretKey chiaveJwt) {
        return new NimbusJwtEncoder(new ImmutableSecret<>(chiaveJwt));
    }

    @Bean
    public JwtDecoder jwtDecoder(SecretKey chiaveJwt) {
        return NimbusJwtDecoder.withSecretKey(chiaveJwt).macAlgorithm(MacAlgorithm.HS256).build();
    }

    private JwtAuthenticationConverter ruoloDalToken() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(ruoliDalToken());
        return converter;
    }

    // Il claim "ruolo" del token (CLIENTE / ADMIN) diventa ROLE_CLIENTE / ROLE_ADMIN.
    // Pubblico perché lo usano anche i test.
    public static JwtGrantedAuthoritiesConverter ruoliDalToken() {
        JwtGrantedAuthoritiesConverter ruoli = new JwtGrantedAuthoritiesConverter();
        ruoli.setAuthoritiesClaimName("ruolo");
        ruoli.setAuthorityPrefix("ROLE_");
        return ruoli;
    }

    // Stesso formato di GlobalExceptionHandler: { "status": 401, "messaggio": "..." }
    private static void scriviErrore(jakarta.servlet.http.HttpServletResponse response, ObjectMapper objectMapper,
                                     HttpStatus status, String messaggio) throws java.io.IOException {
        response.setStatus(status.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        objectMapper.writeValue(response.getOutputStream(), new ErrorResponse(status.value(), messaggio));
    }
}
