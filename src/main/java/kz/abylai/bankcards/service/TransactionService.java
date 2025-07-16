package kz.abylai.bankcards.service;

import kz.abylai.bankcards.entity.Person;
import kz.abylai.bankcards.entity.Transaction;
import kz.abylai.bankcards.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class TransactionService {
    private TransactionRepository transactionRepository;

    public List<Transaction> findAllByPerson(Person person){
        return transactionRepository.findAllByPerson(person);
    }

    public Transaction findById(Long id) {
        return transactionRepository.findById(id).orElse(null);
    }
}
