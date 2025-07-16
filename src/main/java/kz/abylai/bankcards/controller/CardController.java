package kz.abylai.bankcards.controller;

import kz.abylai.bankcards.dto.CardDTO;
import kz.abylai.bankcards.entity.Card;
import kz.abylai.bankcards.entity.Person;
import kz.abylai.bankcards.security.PersonDetails;
import kz.abylai.bankcards.service.CardService;
import kz.abylai.bankcards.service.PersonService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cards")
public class CardController {
    private final CardService cardService;
    private final PersonService personService;
    private final ModelMapper modelMapper;

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public CardResponse getMyCards(Authentication authentication) {
        String username = authentication.getName();// имя из токена
        Person person = personService.findByPhone(username);
        List<Card> cards= cardService.findByPerson(person);
        return new CardResponse(cards.stream().map(this::convertToCardDTO)
                .collect(Collectors.toList()));
    }

    // Дополнительно: для админа — просмотр всех карт
    @GetMapping("/all")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public CardResponse getAllCards() {
        return new CardResponse(cardService.findAll().stream().map(this::convertToCardDTO)
                .collect(Collectors.toList()));
    }

    @PostMapping("/new")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<CardDTO> createNewCard(@AuthenticationPrincipal PersonDetails personDetails) {
        Person person = personDetails.getPerson();

        try{
            Card card = cardService.createCard(person);
            CardDTO cardDTO = convertToCardDTO(card);

            return ResponseEntity.ok(cardDTO);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    private Card convertToCard(CardDTO dto) {
        return modelMapper.map(dto, Card.class);
    }
    private CardDTO convertToCardDTO(Card card) {
        CardDTO cardDTO = new CardDTO();
        cardDTO.setNumber(maskCardNumber(card.getNumber()));
        cardDTO.setStatus(card.getStatus());
        cardDTO.setBalance(card.getBalance());
        cardDTO.setExpiration(card.getExpiration());
        return cardDTO;
    }

    private String maskCardNumber(String fullNumber) {
        return "**** **** **** " + fullNumber.substring(12);
    }

    @PostMapping("/deposit")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<String> depositToCard(@AuthenticationPrincipal PersonDetails personDetails,
                                                @RequestParam String cardNumber,
                                                @RequestParam BigDecimal amount) {
        try {
            cardService.depositToCard(personDetails.getPerson(), cardNumber, amount);
            return ResponseEntity.ok("Счет пополнен на " + amount + "₸");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/transfer")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<String> transferBetweenCards(@AuthenticationPrincipal PersonDetails personDetails,
                                                       @RequestParam String fromCard,
                                                       @RequestParam String toCard,
                                                       @RequestParam BigDecimal amount) {
        try {
            cardService.transferBetweenCards(personDetails.getPerson(), fromCard, toCard, amount);
            return ResponseEntity.ok("Перевод выполнен успешно");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}

