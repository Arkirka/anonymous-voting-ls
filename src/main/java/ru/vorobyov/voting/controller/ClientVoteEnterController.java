package ru.vorobyov.voting.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.vorobyov.voting.entities.Voting;
import ru.vorobyov.voting.services.VotingService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("clientEnter")
public class ClientVoteEnterController {

    @Autowired
    private VotingService votingService;

    private List<String> codesList = new ArrayList<String>();

    @GetMapping
    public List<String> list(){
        return codesList;
    }

    @GetMapping("{code}")
    public String getOne(@PathVariable String code, HttpServletRequest request){

        for(Voting voting : votingService.findAll()){
            if (code.equals(String.valueOf(voting.getCode()))) {
                if (request.getCookies() != null)
                    for (Cookie cookie : request.getCookies()){
                        if(cookie.getValue().equals(code)){
                            code = "used";
                            return code;
                        }
                    }
                code = "ok";
            }
            else
                code = "no";
        }

        return code;
    }

    @GetMapping("/setCookie")
    public String setCookie(HttpServletResponse response) {
        // create a cookie
        Voting voting = new Voting();
        for(Voting votingFromDb : votingService.findAll()){
            voting = votingFromDb;
        }
        Cookie cookie = new Cookie("code", String.valueOf(voting.getCode()));

        //add cookie to response
        response.addCookie(cookie);

        return "Ok!";
    }
}
