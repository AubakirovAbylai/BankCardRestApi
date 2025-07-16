package kz.abylai.bankcards.dto;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import kz.abylai.bankcards.entity.Person;
import kz.abylai.bankcards.enums.CardStatus;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class CardDTO {
    @NotEmpty
    private String number;

    private LocalDate expiration;

    private CardStatus status;

    @Column(name = "balance")
    @Min(0)
    private BigDecimal balance;
}
