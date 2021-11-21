package ru.vorobyov.voting.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.vorobyov.voting.entities.Voting;

@Repository("VotingRepository")
public interface VotingRepository extends CrudRepository<Voting, Integer> {

}
