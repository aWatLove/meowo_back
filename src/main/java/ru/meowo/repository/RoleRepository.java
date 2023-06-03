package ru.meowo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.meowo.model.ERole;
import ru.meowo.model.Role;

import java.util.Optional;

public interface RoleRepository extends MongoRepository<Role, String> {
    Optional<Role> findByName(ERole name);
}
