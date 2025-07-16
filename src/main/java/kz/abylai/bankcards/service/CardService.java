package kz.abylai.bankcards.service;

import kz.abylai.bankcards.dto.CardDTO;
import kz.abylai.bankcards.entity.Card;
import kz.abylai.bankcards.entity.Person;
import kz.abylai.bankcards.enums.CardStatus;
import kz.abylai.bankcards.exception.CardNotFoundException;
import kz.abylai.bankcards.exception.InsufficientFundsException;
import kz.abylai.bankcards.exception.InvalidTransferException;
import kz.abylai.bankcards.repository.CardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Random;

@Service
@Transactional
@RequiredArgsConstructor
public class CardService {
    private final CardRepository cardRepository;
    private CardStatus cardStatus;

    public List<Card> findAll() {
        return cardRepository.findAll();
    }

    public Card findById(Long id) {
        return cardRepository.findById(id).orElse(null);
    }

    public List<Card> findByPerson(Person person) {
        return cardRepository.getCardsByOwner(person);
    }

    public Card createCard(Person person) {
        System.out.println("Попал в createNewCard");

        Card card = new Card();

        card.setNumber(generateCardNumber());
        card.setOwner(person);
        card.setStatus(CardStatus.ACTIVE);
        card.setBalance(BigDecimal.ZERO);
        card.setExpiration(LocalDate.now().plusYears(4));

        return cardRepository.save(card);
    }

    public  Card updateCard(Long id,Card card) {
        Card cardToBeUpdated = cardRepository.findById(id).orElse(null);
        card.setId(id);
        card.setOwner(cardToBeUpdated.getOwner());
        return cardRepository.save(card);
    }

    public void deleteCard(Long id) {
        cardRepository.deleteById(id);
    }

    private String generateCardNumber() {
        StringBuilder number = new StringBuilder("400000"); // например, BIN 400000
        Random random = new Random();
        while (number.length() < 16) {
            number.append(random.nextInt(10));
        }
        return number.toString();
    }

    @Transactional
    public void depositToCard(Person person, String cardNumber, BigDecimal amount) {
        Card card = cardRepository.findByNumberAndOwner(cardNumber, person)
                .orElseThrow(() -> new IllegalArgumentException("Карта не найдена или не принадлежит вам"));

        if (amount.compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("Сумма должна быть положительной");

        card.setBalance(card.getBalance().add(amount));
        cardRepository.save(card);
    }

    @Transactional
    public void transferBetweenCards(Person person, String fromCard, String toCard, BigDecimal amount) {
        if (fromCard.equals(toCard))
            throw new InvalidTransferException("Нельзя переводить на ту же карту");

        Card sender = cardRepository.findByNumberAndOwner(fromCard, person)
                .orElseThrow(() -> new CardNotFoundException("Карта списания не найдена или не принадлежит вам"));

        Card receiver = cardRepository.findByNumberAndOwner(toCard, person)
                .orElseThrow(() -> new CardNotFoundException("Карта зачисления не найдена или не принадлежит вам"));

        if (amount.compareTo(BigDecimal.ZERO) <= 0)
            throw new InvalidTransferException("Сумма должна быть положительной");

        if (sender.getBalance().compareTo(amount) < 0)
            throw new InsufficientFundsException("Недостаточно средств на карте");

        sender.setBalance(sender.getBalance().subtract(amount));
        receiver.setBalance(receiver.getBalance().add(amount));

        cardRepository.save(sender);
        cardRepository.save(receiver);
    }

}
