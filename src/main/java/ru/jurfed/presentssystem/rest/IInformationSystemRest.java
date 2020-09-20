package ru.jurfed.presentssystem.rest;

import org.springframework.web.bind.annotation.RequestBody;
import ru.jurfed.presentssystem.domain.*;

import java.util.List;

public interface IInformationSystemRest {

    List<Storage> getProducts();

    Message newOrder(@RequestBody Order order);

    MessageDto newOrders(@RequestBody OrderDto orderDto);

    Message refreshProducts(@RequestBody ProductDto productDto);

    public void checkAvailableAfterStartUp();

}
