package kz.abylai.bankcards.repository;

import kz.abylai.bankcards.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role,Integer> {
    Optional<Role> findByName(String roleUser);

    Role findById(int id);
}
