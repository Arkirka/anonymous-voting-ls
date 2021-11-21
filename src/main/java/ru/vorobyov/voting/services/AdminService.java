package ru.vorobyov.voting.services;

import ru.vorobyov.voting.entities.Admin;
import ru.vorobyov.voting.entities.Voting;

public interface AdminService {

    public Iterable<Admin> findAll();

}
