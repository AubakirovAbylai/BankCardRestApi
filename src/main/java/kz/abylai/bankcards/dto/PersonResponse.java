package kz.abylai.bankcards.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
public class PersonResponse {
    private List<PersonDTO> people;
}


