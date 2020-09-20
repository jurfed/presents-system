package ru.jurfed.presentssystem.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.jurfed.presentssystem.domain.Manufacturing;
import ru.jurfed.presentssystem.service.IInformationSystemDBService;


@RestController
public class ProductionRestService implements IProductionRestService{

    @Autowired
    IInformationSystemDBService informationSystemDBService;


    //получили ответ от фабрики
    @RequestMapping(value = "/manufactured", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public void sendPresentToOrder(@RequestBody Manufacturing manufacturing) {
        informationSystemDBService.deleteManufacture(manufacturing);

    }


}
