package ru.vorobyov.voting.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.vorobyov.voting.entities.Client;

@Repository("ClientRepository")
public interface ClientRepository extends CrudRepository<Client, Integer> {
}
