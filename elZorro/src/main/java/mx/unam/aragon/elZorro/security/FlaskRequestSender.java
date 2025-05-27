package mx.unam.aragon.elZorro.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class FlaskRequestSender {

    @Value("${flask.jwt.secret}")
    private String jwtSecret;

    @Value("${flask.base.url}")
    private String flaskBaseUrl;

    private final RestTemplate restTemplate;

    public FlaskRequestSender(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    private String generateJwtToken() {
        return Jwts.builder()
                .setSubject("spring-app")
                .setIssuedAt(new Date())
                .signWith(
                        Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8)),
                        SignatureAlgorithm.HS256
                )
                .compact();
    }

    private HttpHeaders buildHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(generateJwtToken());
        return headers;
    }

    public ResponseEntity<byte[]> postToFlask(String endpoint, Object requestBody) {
        try {
            String url = flaskBaseUrl + endpoint;

            HttpEntity<Object> requestEntity = new HttpEntity<>(requestBody, buildHeaders());

            return restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    requestEntity,
                    byte[].class
            );

        } catch (ResourceAccessException e) {
            throw new RuntimeException("Timeout al conectar con Flask", e);
        } catch (HttpClientErrorException e) {
            throw new RuntimeException("Error del cliente: " + e.getResponseBodyAsString(), e);
        } catch (HttpServerErrorException e) {
            throw new RuntimeException("Error del servidor Flask: " + e.getStatusCode(), e);
        }
    }

    public ResponseEntity<String> getFromFlask(String endpoint) {
        String url = flaskBaseUrl + endpoint;
        HttpEntity<Void> requestEntity = new HttpEntity<>(buildHeaders());

        try {
            return restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
        }
    }
}