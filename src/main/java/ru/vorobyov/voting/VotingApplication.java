package ru.vorobyov.voting;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.vorobyov.voting.entities.Voting;
import ru.vorobyov.voting.services.VotingService;

@SpringBootApplication
public class VotingApplication implements CommandLineRunner {

    @Autowired
	private VotingService votingService;

	public static void main(String[] args) {

		SpringApplication.run(VotingApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		DBDemo();
	}

	private void DBDemo(){
		for(Voting voting : votingService.findAll()){
			System.out.println("id = " + voting.getId());
			System.out.println("theme = " + voting.getTheme());
			System.out.println("code = " + voting.getYes());
		}

	}
}
