package ro.mpp2024.controller;

import io.jsonwebtoken.Jwts;
import ro.mpp2024.domain.PersoanaOficiu;
import ro.mpp2024.IPersoanaOficiuRepo;
import ro.mpp2024.controller.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/api/login")
@CrossOrigin(origins = "http://localhost:5175")
public class EmployeeController {
    private static final long EXPIRATION_TIME = 360000;

    @Autowired
    private IPersoanaOficiuRepo persoanaOficiuRepo;

    public static String generateJWToken(String username) {
        String token = Jwts.builder()
                .subject(username)
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(JWTUtils.getKey(), Jwts.SIG.HS512)
                .compact()
                .trim();
        System.out.println("JWT token generat: " + token);
        return token;
    }

    @PostMapping
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        String username = loginRequest.username();
        String password = loginRequest.password();

        PersoanaOficiu persoana = persoanaOficiuRepo.findByUsername(username).orElse(null);

        if (persoana == null || !persoana.getPassword().equals(password)) {
            System.out.println("Autentificare eșuată pentru " + username);
            return ResponseEntity.status(401).body("Autentificare eșuată. Verifică datele introduse.");
        }

        System.out.println("Autentificare reușită. Generare token...");
        String token = generateJWToken(username);
        return ResponseEntity.ok(token);
    }

}
