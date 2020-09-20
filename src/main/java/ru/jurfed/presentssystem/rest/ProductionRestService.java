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

    void sendRequestForCreateProducts(String productType){

        Storage storage = informationSystemDBService.getPresent(productType);
        int availableValue = storage.getAvailableValue();
        int minValue = storage.getMinValue();
        List<Manufacturing> sendingForProductValue = informationSystemDBService.getManufacturing(productType);
        int manufacturingValue = (int) sendingForProductValue.stream().map(manufacturing -> manufacturing.getCount()).count();

        if(availableValue + manufacturingValue < minValue){
            sendManufacturingRequest(productType, minValue);
        }

    }

    //послать запрос на производство и сохранить в manufacturing
    private void sendManufacturingRequest(String productType, int minValue){
        RestTemplate restTemplate = new RestTemplate();
        final String baseUrl = "http://localhost:" + 8091 + "/manufactured";

        Manufacturing manufacturing = informationSystemDBService.addManufacturingRequest(productType, minValue);
        try {
            URI uri = new URI(baseUrl);
            HttpEntity<Manufacturing> requestBody = new HttpEntity(manufacturing);
            restTemplate.postForEntity(uri,requestBody,Manufacturing.class);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

    }

    //получили ответ от фабрики
    @RequestMapping(value = "/manufactured", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public void sendPresentToOrder(@RequestBody Manufacturing manufacturing) {
        informationSystemDBService.deleteManufacture(manufacturing);
        System.out.println();
//        informationSystemDBService.updateStorage;
    }




}
