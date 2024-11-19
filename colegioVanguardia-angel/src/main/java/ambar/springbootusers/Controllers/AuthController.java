package ambar.springbootusers.Controllers;

import ambar.springbootusers.Modelos.userGeneral;
import ambar.springbootusers.Repositories.UserGeneralRepository;
import ambar.springbootusers.Security.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ambar.springbootusers.Security.JwtUtil;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserGeneralRepository userGeneralRepository;

    @Autowired
    private JwtUtil jwtUtil;

    public String convertirSHA256(String password) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-256");
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
        byte[] hash = md.digest(password.getBytes());
        StringBuffer sb = new StringBuffer();
        for(byte b : hash) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody userGeneral loginRequest) {
        userGeneral user = userGeneralRepository.getUserGeneralByCorreo(loginRequest.getCorreo());

        if (user != null && user.getPassword().equals(this.convertirSHA256(loginRequest.getPassword()))) {
            Map<String, Object> claims = new HashMap<>();
            claims.put("id", user.get_id());
            claims.put("correo", user.getCorreo());
            claims.put("rolId", user.getRol().get_id());


            String token = jwtUtil.generateToken(claims, user.getCorreo());

            return ResponseEntity.ok(token);
        } else {
            return ResponseEntity.status(401).body("Credenciales incorrectas");
        }   
    }
}
