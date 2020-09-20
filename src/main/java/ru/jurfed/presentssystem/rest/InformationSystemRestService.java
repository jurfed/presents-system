package ru.jurfed.presentssystem.rest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.jurfed.presentssystem.Dto.MessageDto;
import ru.jurfed.presentssystem.Dto.OrderDto;
import ru.jurfed.presentssystem.Dto.ProductDto;
import ru.jurfed.presentssystem.domain.*;
import ru.jurfed.presentssystem.service.IDemeanourService;
import ru.jurfed.presentssystem.service.IInformationSystemDBService;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * For process presents
 */
@RestController
public class InformationSystemRestService implements IInformationSystemRest{

    @Autowired
    IInformationSystemDBService informationSystemDBService;

    @Autowired
    IDemeanourService demeanourService;

    @Autowired
    IProductionRestService productionRestService;

    Calendar cal = Calendar.getInstance();

    private static final Logger logger = LogManager.getLogger(InformationSystemRestService.class);

    private boolean checkDemeanour(String fio) {
        return demeanourService.getDemeanour(fio);
    }

    @RequestMapping(value = "/getAllProducts", method = RequestMethod.GET)
    @ResponseBody
    public List<Storage> getProducts() {
        return informationSystemDBService.getAllProducts();
    }

    /**
     * method of processing the 1st order
     */
    @RequestMapping(value = "/newOrder", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public Message newOrder(@RequestBody Order order) {
        Message message = new Message();

            String fio = order.getFio();
            String presentType = order.getProductType();
            Integer year;

            if (order.getYear() == null) {
                order.setYear(Calendar.getInstance().get(Calendar.YEAR));
            }

            year = order.getYear();


            if (!checkPreorder(year, fio)) {
                message.setMsg("Error: the order already exists");
                return message;
            }

            if (!checkDemeanour(fio)) {
                message.setMsg("Sorry, bad behavior");
                return message;
            }

            if(!informationSystemDBService.createPreorder(presentType, fio, year)){
                message.setMsg("Error: bad request");
                return message;
            }

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

    /**
     * a method of processing a list of orders
     */
    @RequestMapping(value = "/newOrders", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public MessageDto newOrders(@RequestBody OrderDto orderDto) {
        List<Message> messages = new ArrayList<>();
        orderDto.getOrders().forEach(order -> {
            messages.add(newOrder(order));
        });

        MessageDto messageDto = new MessageDto(messages);
        return (messageDto);
    }

    /**
     * changing the minimum values
     */
    @RequestMapping(value = "/refreshProducts", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public Message refreshProducts(@RequestBody ProductDto productDto) {
        informationSystemDBService.refreshProducts(productDto);

        Message message = new Message();
        message.setMsg("minimum values changed");
        return message;
    }

    /**
     * method for checking an existing order
     */
    private boolean checkPreorder(final int year, final String fio) {
        return informationSystemDBService.checkPreorder(year, fio);
    }

    @RequestMapping(value = "/checkAvailableAfterStartUp", method = RequestMethod.GET)
    public void checkAvailableAfterStartUp(){
        informationSystemDBService.checkAvailableAfterStartUp();
    }


}
