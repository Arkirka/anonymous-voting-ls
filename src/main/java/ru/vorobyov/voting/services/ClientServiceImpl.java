package ru.vorobyov.voting.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.vorobyov.voting.entities.Client;
import ru.vorobyov.voting.repositories.ClientRepository;

@Service("clientService")
public class ClientServiceImpl implements ClientService{

    @Autowired
    public ClientRepository clientRepository;
    @Override
    public Iterable<Client> findAll() {
        return clientRepository.findAll();
    }

    @Override
    public void create(Client client) {
        clientRepository.save(client);
    }

    @Override
    public void delete() {
        clientRepository.deleteAll();
    }
}
