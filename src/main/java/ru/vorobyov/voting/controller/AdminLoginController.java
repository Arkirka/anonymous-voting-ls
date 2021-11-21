package ru.vorobyov.voting.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.vorobyov.voting.entities.Admin;
import ru.vorobyov.voting.services.AdminService;

@RestController  //warning
@RequestMapping("adminLogin")
public class AdminLoginController {

    @Autowired
    private AdminService adminService;

    @RequestMapping("/")
    public ModelAndView getPage () {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("adminlogin");
        return modelAndView;
    }

    @PutMapping("authed")
    public HttpStatus update(@RequestBody String authString){

        String[] authList = authString.split(";");
        String login = "";
        String password = "";
        String code = "";

        if(authList.length == 2){
            for(Admin admin : adminService.findAll()){
                login = admin.getLogin();
                password = admin.getPassword();
            }

            if(authList[0].equals(login) && authList[1].equals(password))
                return HttpStatus.OK;
            else
                return HttpStatus.NOT_ACCEPTABLE;
        } else
            return HttpStatus.NO_CONTENT;
    }
}
