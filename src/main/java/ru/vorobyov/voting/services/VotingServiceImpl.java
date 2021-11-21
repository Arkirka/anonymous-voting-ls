package ru.vorobyov.voting.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.vorobyov.voting.entities.Voting;
import ru.vorobyov.voting.repositories.VotingRepository;

import java.util.Date;
import java.util.Optional;

@Service("votingService")
public class VotingServiceImpl implements VotingService{

    @Autowired
    private  VotingService votingService;

    @Autowired
    private VotingRepository votingRepository;
    @Override
    public Iterable<Voting> findAll() {
        return votingRepository.findAll();
    }

    @Override
    public void updateUser(int votingId, int yes, int no, int neutral, int broken) {
        Optional<Voting> votingFromDb = votingRepository.findById(votingId);
        Voting voting = votingFromDb.get();
        voting.setYes( voting.getYes() + yes );
        voting.setNo( voting.getNo() + no );
        voting.setNeutral( voting.getNeutral() + neutral );
        voting.setBroken( voting.getBroken() + broken );
        votingRepository.save(voting);
    }

    @Override
    public void create(String theme) {
        Date date1 = new Date();
        int max = 8000 - 1000;
        int code = (int) (Math.random() * ++max) + 1000;
        code += date1.getHours() + date1.getMinutes() + date1.getSeconds() + date1.getDay();

        Voting voting = new Voting();
        voting.setTheme(theme);
        voting.setCode(code);
        votingRepository.save(voting);
    }

    @Override
    public void delete() {
        votingRepository.deleteAll();
    }


}
