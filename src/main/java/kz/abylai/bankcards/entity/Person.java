package kz.abylai.bankcards.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "Person")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotEmpty
    @Column(name = "full_name")
    @Pattern(regexp = "([A-Z]\\w+ [A-Z]\\w+)" +
            "|([А-ЯЁ][а-яё]+ [А-ЯЁ][а-яё]+)",
            message = "Прошу написать ФИО правильно")
    private String fullName;

    @NotEmpty
    @Column(name = "number_phone")
    @Pattern(regexp = "^(\\+7|8)\\d{10}$", message = "Напишите номер телефона правильно")
    private String numberPhone;

    @NotEmpty
    @Column(name = "password")
    private String password;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id")
    private Role role;

    @OneToMany(mappedBy = "owner", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Card> cards;
}
