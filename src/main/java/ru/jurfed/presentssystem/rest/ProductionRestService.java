package ru.jurfed.presentssystem.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import ru.jurfed.presentssystem.domain.Manufacturing;
import ru.jurfed.presentssystem.domain.Order;
import ru.jurfed.presentssystem.domain.Storage;
import ru.jurfed.presentssystem.service.InformationSystemDBService;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
public class ProductionRestService {

    @Autowired
    InformationSystemDBService informationSystemDBService;


    //получили ответ от фабрики
    @RequestMapping(value = "/manufactured", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public void sendPresentToOrder(@RequestBody Manufacturing manufacturing) {
        informationSystemDBService.deleteManufacture(manufacturing);

    }


}
