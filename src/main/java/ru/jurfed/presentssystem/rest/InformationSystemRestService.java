package ru.jurfed.presentssystem.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.jurfed.presentssystem.domain.Order;
import ru.jurfed.presentssystem.domain.Storage;
import ru.jurfed.presentssystem.service.IDemeanourService;
import ru.jurfed.presentssystem.service.InformationSystemDBService;

import java.util.Calendar;
import java.util.List;

@RestController
public class InformationSystemRestService {

    @Autowired
    InformationSystemDBService informationSystemDBService;

    @Autowired
    IDemeanourService demeanourService;

    @Autowired
    ProductionRestService productionRestService;

    Calendar cal = Calendar.getInstance();


    private boolean checkDemeanour(String fio) {
        return demeanourService.getDemeanour(fio);
    }


    @RequestMapping(value = "/getAllProducts", method = RequestMethod.GET)
    public @ResponseBody
    List<Storage> getProducts() {

        return informationSystemDBService.getAllProducts();
    }

    //метод получения заказа =========================================================
    @RequestMapping(value = "/newOrder", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public Message newOrder(@RequestBody Order order) {
        String fio = order.getFio();
        String presentType = order.getProductType();
        Integer year;

        if (order.getYear() == null) {
            order.setYear(Calendar.getInstance().get(Calendar.YEAR));
        }

        year = order.getYear();


        Message message = new Message();

        if (!checkDemeanour(fio)) {
            message.setMsg("Sorry, bad behavior");
            return message;
        }

        if (!checkPreorder(year, fio)) {
            message.setMsg("Error: the order already exists");
            return message;
        }

        informationSystemDBService.createPreorder(presentType, fio, year);

        boolean presentExists = informationSystemDBService.checkAvailableProducts(presentType);

        if(presentExists){
            informationSystemDBService.transferOneProductToOrder(presentType, fio, year);
            message.setMsg("This gift was sent from the warehouse to the storage");

        }else{
            informationSystemDBService.checkMinAvailableProducts(presentType);
            message.setMsg("The gift is accepted and sent to the factory for production");
        }

        return message;
    }

    //метод проверки уже имеющегося заказа
    private boolean checkPreorder(final int year, final String fio) {
        return informationSystemDBService.checkPreorder(year, fio);
    }



}
