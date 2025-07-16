package kz.abylai.bankcards.controller;

import kz.abylai.bankcards.entity.Person;
import kz.abylai.bankcards.entity.Transaction;
import kz.abylai.bankcards.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping
    public List<Transaction> getMyTransactions(@AuthenticationPrincipal Person person) {
        return transactionService.findAllByPerson(person);
    }
}

