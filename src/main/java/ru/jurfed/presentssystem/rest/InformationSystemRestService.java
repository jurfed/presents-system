package ru.jurfed.presentssystem.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.jurfed.presentssystem.domain.Order;
import ru.jurfed.presentssystem.domain.Storage;
import ru.jurfed.presentssystem.service.IDemeanourService;
import ru.jurfed.presentssystem.service.InformationSystemDBService;

import java.util.List;

@RestController
public class InformationSystemRestService {

    @Autowired
    InformationSystemDBService informationSystemDBService;

    @Autowired
    IDemeanourService demeanourService;


    @RequestMapping(value = "/newOrder", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public Message newOrder(@RequestBody Order order) {
        String fio = order.getFio();
        Message message = new Message("dsfsdf");

        if(!checkDemeanour(fio)){
            message.setMsg("error");
        }

        this.checkAvailablePresents(order.getProductType());
        return message;
    }

    @RequestMapping(value = "/getAllProducts", method = RequestMethod.GET)
    public @ResponseBody
    List<Storage> getProducts() {

        return informationSystemDBService.getAllProducts();
    }


    private boolean checkDemeanour(String fio) {
        return demeanourService.getDemeanour(fio);
    }

    private void checkAvailablePresents(String presentType) {
        var products = informationSystemDBService.getAllProducts();
    }


}
