package kz.abylai.bankcards.repository;

import kz.abylai.bankcards.dto.CardDTO;
import kz.abylai.bankcards.entity.Card;
import kz.abylai.bankcards.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CardRepository extends JpaRepository<Card,Long> {
    public List<Card> getCardsByOwner(Person person);

    Optional<Card> findByNumberAndOwner(String number, Person owner);

    Optional<Card> findByNumber(String number);
}
