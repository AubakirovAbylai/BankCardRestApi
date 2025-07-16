package kz.abylai.bankcards.controller;

import kz.abylai.bankcards.dto.AuthRequest;
import kz.abylai.bankcards.dto.AuthResponse;
import kz.abylai.bankcards.dto.RegisterRequest;
import kz.abylai.bankcards.entity.Person;
import kz.abylai.bankcards.entity.Role;
import kz.abylai.bankcards.repository.PersonRepository;
import kz.abylai.bankcards.repository.RoleRepository;
import kz.abylai.bankcards.security.JwtService;
import kz.abylai.bankcards.service.AuthService;
import kz.abylai.bankcards.service.PersonDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final PersonRepository personRepository;
    private final AuthService authService;
    private final RoleRepository roleRepository;
    private final PersonDetailsService personDetailsService;
    private final JwtService  jwtService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        // Проверка: не существует ли такой пользователь
        if (personRepository.findByNumberPhone(request.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("Пользователь уже существует");
        }

        // Получаем роль по умолчанию (например, ROLE_USER)
        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("Роль не найдена"));

        Person person = new Person();
        person.setFullName(request.getFullName());
        person.setNumberPhone(request.getUsername());
        person.setPassword(passwordEncoder.encode(request.getPassword()));
        person.setRole(userRole);

        personRepository.save(person);

        // Генерация токена
        var userDetails = personDetailsService.loadUserByUsername(person.getNumberPhone());
        String token = jwtService.generateToken(userDetails);

        return ResponseEntity.ok(new AuthResponse(token));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request){
        return ResponseEntity.ok(authService.login(request));
    }
}
