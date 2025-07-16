package kz.abylai.bankcards.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import kz.abylai.bankcards.entity.Card;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PersonDTO {
    @NotEmpty
    @Pattern(regexp = "([A-Z]\\w+ [A-Z]\\w+)" +
            "|([А-ЯЁ][а-яё]+ [А-ЯЁ][а-яё]+)",
            message = "Прошу написать ФИО правильно")
    private String fullName;

    @NotEmpty
    @Pattern(regexp = "^(\\+7|8)\\d{10}$", message = "Напишите номер телефона правильно")
    private String numberPhone;

    private List<CardDTO> cardsDTO;
}
