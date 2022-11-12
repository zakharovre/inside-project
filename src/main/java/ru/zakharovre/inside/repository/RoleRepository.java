package ru.zakharovre.inside.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.zakharovre.inside.entity.Role;

@Repository
public interface RoleRepository extends CrudRepository<Role, Long> {
}
