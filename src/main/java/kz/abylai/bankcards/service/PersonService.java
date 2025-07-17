package kz.abylai.bankcards.service;

import jakarta.persistence.EntityNotFoundException;
import kz.abylai.bankcards.dto.PersonDTO;
import kz.abylai.bankcards.entity.Person;
import kz.abylai.bankcards.repository.PersonRepository;
import kz.abylai.bankcards.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class PersonService {
    private final PersonRepository personRepository;
    private final RoleRepository roleRepository;

    public List<Person> findAll() {
        return personRepository.findAll();
    }

    public Person findById(Long id) {
        return personRepository.findById(id).orElse(null);
    }

    public Person findByPhone(String phoneNumber) {
        return personRepository.findByNumberPhone(phoneNumber).orElse(null);
    }

    @Transactional
    public Person updatePerson(Long id, Person person) {
        Person personToBeUpdated = findById(id);
        person.setId(id);
        return personRepository.save(person);
    }

    @Transactional
    public void deletePerson(Long id) {
        personRepository.deleteById(id);
    }

    public void createPerson(PersonDTO personDTO) {
        Person person = new Person();
        person.setFullName(personDTO.getFullName());
        person.setNumberPhone(personDTO.getNumberPhone());
        person.setPassword("password");
        person.setRole(roleRepository.findById(1));

        personRepository.save(person);
    }
}
