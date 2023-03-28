package tech.jhipster.lite.sample.cucumber;

import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.time.Instant;
import java.util.Map;
import javax.crypto.SecretKey;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;

@TestConfiguration
public class CucumberAuthenticationConfiguration {

  public static final SecretKey JWT_KEY = Keys.hmacShaKeyFor(
    Decoders.BASE64.decode("OTdhNzE2OTQwNWVmYmZhOWRiOTA4MzI2ZDRmNDg1NzMwNDlhNDZmMQ==")
  );

  @Bean
  @Primary
  public JwtDecoder jwtDecoder() {
    JwtParser decoder = Jwts.parserBuilder().setSigningKey(JWT_KEY).build();

    return token ->
      new Jwt(
        "token",
        Instant.now(),
        Instant.now().plusSeconds(120),
        Map.of("issuer", "http://dev"),
        decoder.parseClaimsJws(token).getBody()
      );
  }
}
