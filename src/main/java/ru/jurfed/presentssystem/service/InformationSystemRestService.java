package ru.jurfed.presentssystem.service;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class InformationSystemRestService {


    @GetMapping("/configs")
    public String listPage(Model model) {

        return "config";
    }

}
