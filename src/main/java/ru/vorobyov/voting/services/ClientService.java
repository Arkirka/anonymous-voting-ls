package ru.vorobyov.voting.services;

import ru.vorobyov.voting.entities.Client;

public interface ClientService {

    public Iterable<Client> findAll();
    public void create(Client client);
    public void delete();
}
