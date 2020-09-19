package ru.jurfed.presentssystem.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import ru.jurfed.presentssystem.service.InformationSystemDBService;

@RestController
public class ProductionRestService {


    @Autowired
    InformationSystemDBService informationSystemDBService;


    void sendRequestForCreateProducts(String Type){
//        informationSystemDBService.
    }


}
