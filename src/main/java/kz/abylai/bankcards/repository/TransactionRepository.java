package kz.abylai.bankcards.repository;

import kz.abylai.bankcards.entity.Person;
import kz.abylai.bankcards.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction,Long> {
    public List<Transaction> findAllByPerson(Person person);
}
