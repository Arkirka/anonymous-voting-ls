package ru.vorobyov.voting.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.vorobyov.voting.services.VotingService;

@RestController  //warning
@RequestMapping("createVoting")
public class CreateVotingController {
    @Autowired
    public VotingService votingService;

    @RequestMapping("/")
    public ModelAndView getPage () {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("createvoting");
        return modelAndView;
    }

    @PutMapping("createQuestion")
    public HttpStatus update(@RequestBody String question){
        if(question.isBlank() || question.isEmpty())
            return HttpStatus.NO_CONTENT;
        else {
            votingService.create(question);
            return HttpStatus.OK;
        }
    }
}
