package it.case_vacanze.manager.services;

import java.time.Duration;
import java.time.Instant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import it.case_vacanze.manager.entity.Clienti;

// Crea il token JWT restituito al login
@Service
public class TokenService {

    private final JwtEncoder jwtEncoder;
    private final Duration durata;

    public TokenService(JwtEncoder jwtEncoder, @Value("${app.jwt.durata-ore:8}") long durataOre) {
        this.jwtEncoder = jwtEncoder;
        this.durata = Duration.ofHours(durataOre);
    }

    public String creaToken(Clienti cliente) {
        Instant adesso = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .subject(cliente.getEmail())            // diventa authentication.getName()
                .claim("ruolo", cliente.getRuolo().name())
                .issuedAt(adesso)
                .expiresAt(adesso.plus(durata))
                .build();
        JwsHeader header = JwsHeader.with(MacAlgorithm.HS256).build();
        return jwtEncoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue();
    }
}
