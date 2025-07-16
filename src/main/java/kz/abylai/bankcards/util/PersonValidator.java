package kz.abylai.bankcards.util;

import kz.abylai.bankcards.entity.Person;
import kz.abylai.bankcards.service.PersonDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class PersonValidator implements Validator {
    private final PersonDetailsService personDetailsService;

    @Override
    public boolean supports(Class<?> clazz) {
        return Person.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Person person = (Person) target;

        try {
            personDetailsService.loadUserByUsername(person.getFullName());
        } catch (UsernameNotFoundException e) {
            throw new RuntimeException(e);
        }

        errors.rejectValue("fullName", null, "Full name is required");
    }
}
