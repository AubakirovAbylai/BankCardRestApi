package kz.abylai.bankcards.service;

import kz.abylai.bankcards.entity.Card;
import kz.abylai.bankcards.entity.Person;
import kz.abylai.bankcards.exception.CardNotFoundException;
import kz.abylai.bankcards.exception.InsufficientFundsException;
import kz.abylai.bankcards.repository.CardRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CardServiceTest {

    @Mock
    private CardRepository cardRepository;

    @InjectMocks
    private CardService cardService;

    private Person person;
    private Card fromCard;
    private Card toCard;

    @BeforeEach
    void setUp() {
        person = new Person();
        person.setId(1L);

        fromCard = new Card();
        fromCard.setNumber("4000001111111111");
        fromCard.setBalance(BigDecimal.valueOf(1000));
        fromCard.setOwner(person);

        toCard = new Card();
        toCard.setNumber("4000002222222222");
        toCard.setBalance(BigDecimal.valueOf(500));
    }

    @Test
    void testTransfer_Success() {
        when(cardRepository.findByNumberAndOwner("4000001111111111", person))
                .thenReturn(Optional.of(fromCard));
        when(cardRepository.findByNumberAndOwner("4000002222222222", person)) // ✅ исправлено
                .thenReturn(Optional.of(toCard));

        cardService.transferBetweenCards(person, "4000001111111111", "4000002222222222", BigDecimal.valueOf(300));

        assertEquals(BigDecimal.valueOf(700), fromCard.getBalance());
        assertEquals(BigDecimal.valueOf(800), toCard.getBalance());
    }


    @Test
    void testTransfer_CardNotFound() {
        when(cardRepository.findByNumberAndOwner("4000000000000000", person))
                .thenReturn(Optional.empty());

        assertThrows(CardNotFoundException.class, () -> {
            cardService.transferBetweenCards(person, "4000000000000000", "4000002222222222", BigDecimal.valueOf(100));
        });
    }

    @Test
    void testTransfer_InsufficientFunds() {
        fromCard.setBalance(BigDecimal.valueOf(50)); // недостаточно
        when(cardRepository.findByNumberAndOwner("4000001111111111", person))
                .thenReturn(Optional.of(fromCard));
        when(cardRepository.findByNumberAndOwner("4000002222222222", person)) // исправлено!
                .thenReturn(Optional.of(toCard));

        assertThrows(InsufficientFundsException.class, () -> {
            cardService.transferBetweenCards(person, "4000001111111111", "4000002222222222", BigDecimal.valueOf(300));
        });
    }

}

