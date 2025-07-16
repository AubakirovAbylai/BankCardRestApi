package kz.abylai.bankcards.service;

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

    public List<Transaction> findAll(){
        return transactionRepository.findAll();
    }

    public Transaction findById(Long id) {
        return transactionRepository.findById(id).orElse(null);
    }
}
