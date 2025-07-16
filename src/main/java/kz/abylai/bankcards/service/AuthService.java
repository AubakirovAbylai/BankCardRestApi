package kz.abylai.bankcards.service;

import kz.abylai.bankcards.dto.AuthRequest;
import kz.abylai.bankcards.dto.AuthResponse;
import kz.abylai.bankcards.dto.RegisterRequest;
import kz.abylai.bankcards.entity.Person;
import kz.abylai.bankcards.entity.Role;
import kz.abylai.bankcards.repository.PersonRepository;
import kz.abylai.bankcards.repository.RoleRepository;
import kz.abylai.bankcards.security.JwtService;
import kz.abylai.bankcards.security.PersonDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final PersonRepository personRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthResponse register(RegisterRequest request){
        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(()->new RuntimeException("Роль User не найдена"));

        Person person = new Person();
        person.setFullName(request.getFullName());
        person.setNumberPhone(request.getUsername());
        person.setPassword(passwordEncoder.encode(request.getPassword()));
        person.setRole(userRole);

        personRepository.save(person);

        String token = jwtService.generateToken(new PersonDetails(person));
        return new AuthResponse(token);
    }

    public AuthResponse login(AuthRequest request){
        Person person = personRepository.findByNumberPhone(request.getPhone())
                .orElseThrow(()->new RuntimeException("Пользователь не найден"));

        if(!passwordEncoder.matches(request.getPassword(),person.getPassword())){
            throw new RuntimeException("");
        }

        String token = jwtService.generateToken(new PersonDetails(person));
        return new AuthResponse(token);
    }
}
