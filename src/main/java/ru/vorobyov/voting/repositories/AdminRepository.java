package ru.vorobyov.voting.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.vorobyov.voting.entities.Admin;

@Repository("AdminRepository")
public interface AdminRepository extends CrudRepository<Admin, Integer> {
}
