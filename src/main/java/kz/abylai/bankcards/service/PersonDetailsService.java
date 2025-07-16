package kz.abylai.bankcards.service;

import kz.abylai.bankcards.entity.Person;
import kz.abylai.bankcards.repository.PersonRepository;
import kz.abylai.bankcards.security.PersonDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PersonDetailsService implements UserDetailsService {
    private final PersonRepository personRepository;


    @Override
    public PersonDetails loadUserByUsername(String phone) throws UsernameNotFoundException {
        Person person = personRepository.findByNumberPhone(phone).orElse(null);

        if (person == null) {
            throw new UsernameNotFoundException("Person not found");
        }

        return new PersonDetails(person);
    }
}
