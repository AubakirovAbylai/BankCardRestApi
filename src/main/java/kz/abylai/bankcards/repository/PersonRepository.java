package kz.abylai.bankcards.repository;

import kz.abylai.bankcards.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PersonRepository extends JpaRepository<Person, Long> {
    Optional<Person> findById(Long id);

    Optional<Person> findByFullName(String fullName);

    Optional<Person> findByNumberPhone(String phoneNumber);
}
