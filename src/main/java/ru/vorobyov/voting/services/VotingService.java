package ru.vorobyov.voting.services;

import ru.vorobyov.voting.entities.Voting;

public interface VotingService {

    public Iterable<Voting> findAll();
    public void updateUser(int votingId, int yes, int no, int neutral, int broken);
    public void create(String theme);
    public void delete();
}
