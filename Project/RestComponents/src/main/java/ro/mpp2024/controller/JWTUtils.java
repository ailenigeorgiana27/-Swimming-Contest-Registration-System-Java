package ro.mpp2024.controller;


import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class JWTUtils {
    private static final String SECRET;

    static {
        Properties prop = new Properties();
        try (InputStream inputStream = JWTUtils.class.getClassLoader().getResourceAsStream("bd.config")) {
            prop.load(inputStream);
            SECRET = prop.getProperty("jwt.secret");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private static final SecretKey KEY = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));

    public static void validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(KEY)
                    .build()
                    .parseSignedClaims(token);

        } catch (SecurityException e) {
            System.out.println("Invalid JWT signature: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Invalid JWT token: " + e.getMessage());
        }
    }

    public static SecretKey getKey() {
        return KEY;
    }

}