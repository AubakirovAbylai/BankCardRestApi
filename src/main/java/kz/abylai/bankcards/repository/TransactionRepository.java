package kz.abylai.bankcards.repository;

import kz.abylai.bankcards.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction,Long> {
}
