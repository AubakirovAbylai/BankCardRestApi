package kz.abylai.bankcards.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "roles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @NotBlank(message = "Role name must not be empty")
    @Pattern(
            regexp = "^ROLE_[A-Z]+$",
            message = "Role name must start with 'ROLE_' and contain only uppercase letters (e.g., ROLE_ADMIN)"
    )
    private String name;

    @OneToMany(mappedBy = "role", fetch = FetchType.LAZY)
    private List<Person> personList;
}
