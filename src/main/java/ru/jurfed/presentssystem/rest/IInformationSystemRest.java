package ru.jurfed.presentssystem.rest;

import org.springframework.web.bind.annotation.RequestBody;
import ru.jurfed.presentssystem.Dto.MessageDto;
import ru.jurfed.presentssystem.Dto.OrderDto;
import ru.jurfed.presentssystem.Dto.ProductDto;
import ru.jurfed.presentssystem.domain.Message;
import ru.jurfed.presentssystem.domain.Order;

public interface IInformationSystemRest {

    ProductDto getProducts();

    Message newOrder(@RequestBody Order order);

    MessageDto newOrders(@RequestBody OrderDto orderDto);

    Message refreshProducts(@RequestBody ProductDto productDto);

    public void checkAvailableAfterStartUp();

}
