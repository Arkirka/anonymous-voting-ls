package ru.vorobyov.voting.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.vorobyov.voting.entities.Client;
import ru.vorobyov.voting.entities.Voting;
import ru.vorobyov.voting.services.ClientService;
import ru.vorobyov.voting.services.VotingService;

import java.util.ArrayList;
import java.util.Map;

@RestController
@RequestMapping("clientVoting")
public class ClientVoteProcessController {

    @Autowired
    private VotingService votingService;
    @Autowired
    private ClientService clientService;

    @RequestMapping("/")
    public ModelAndView loginTest () {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("voting");
        return modelAndView;
    }

    @GetMapping("getHeader")
    public String getHeader(){

        String header = "";
        for(Voting voting : votingService.findAll()){
            header = voting.getTheme();
        }

        return header;
    }

    @PutMapping("toVote")
    public HttpStatus update(@RequestBody String votes){
        Client client = new Client();
        int votingId = -1;

        String[] votesList = votes.split(";");

        for(Voting voting : votingService.findAll()){
            votingId = voting.getId();
        }

        if(votesList.length == 1){
            if(votesList[0].equals("yes")){
                votingService.updateUser(votingId, 1, 0, 0, 0);

                client.setYes(1);
                clientService.create(client);
            } else if(votesList[0].equals("no")){
                votingService.updateUser(votingId, 0, 1, 0, 0);
                client.setNo(1);
                clientService.create(client);
            } else if(votesList[0].equals("neutral")){
                votingService.updateUser(votingId, 0, 0, 1, 0);
                client.setNeutral(1);
                clientService.create(client);
            }
        } else if(votesList.length == -1) {
            return HttpStatus.BAD_REQUEST;
        }else {
            //broken
            votingService.updateUser(votingId, 0, 0, 0, 1);

            for(String vote : votesList){
                if(vote.equals("yes"))
                    client.setYes(1);
                else if(vote.equals("no"))
                    client.setNo(1);
                else if(vote.equals("neutral"))
                    client.setNeutral(1);
            }
            clientService.create(client);
        }

        return HttpStatus.OK;
    }
}
