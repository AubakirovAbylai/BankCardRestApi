package kz.abylai.bankcards.controller;

import kz.abylai.bankcards.dto.CardDTO;
import kz.abylai.bankcards.dto.PersonDTO;
import kz.abylai.bankcards.dto.PersonResponse;
import kz.abylai.bankcards.entity.Card;
import kz.abylai.bankcards.entity.Person;
import kz.abylai.bankcards.service.PersonService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/person")
public class PersonController {
    private final PersonService personService;
    private final ModelMapper modelMapper;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping
    public PersonResponse getAllPersons() {
        return new PersonResponse(personService.findAll().stream()
                .map(this::convertToPersonDTO)
                .collect(Collectors.toList()));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/new")
    public void createPerson(@RequestBody PersonDTO personDTO) {
        Person person = convertToPerson(personDTO);
        personService.createPerson(person);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public void deletePerson(@PathVariable Long id) {
        personService.deletePerson(id);
    }

    private PersonDTO convertToPersonDTO(Person person) {
        PersonDTO dto = new PersonDTO();
        dto.setFullName(person.getFullName());
        dto.setNumberPhone(person.getNumberPhone());
        List<CardDTO> cardDTO = person.getCards().stream()
                .map(this::convertToCardDTO).collect(Collectors.toList());
        dto.setCardsDTO(cardDTO);
        return dto;
    }

    private  CardDTO convertToCardDTO(Card card) {
        return modelMapper.map(card, CardDTO.class);
    }

    private Person convertToPerson(PersonDTO personDTO) {
        return modelMapper.map(personDTO, Person.class);
    }
}


