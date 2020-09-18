package ru.jurfed.presentssystem.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.jurfed.presentssystem.domain.Books;
import ru.jurfed.presentssystem.domain.Order;
import ru.jurfed.presentssystem.domain.OrderDto;
import ru.jurfed.presentssystem.domain.Storage;
import ru.jurfed.presentssystem.repository.BookRepository;
import ru.jurfed.presentssystem.repository.BookServiceImpl;
import ru.jurfed.presentssystem.repository.OrderRepository;
import ru.jurfed.presentssystem.repository.StorageRepository;
import ru.jurfed.presentssystem.service.InformationSystemDBService;

import java.util.List;

@RestController
public class InformationSystemRestService {

    @Autowired
    InformationSystemDBService informationSystemDBService;

    @Autowired
    StorageRepository storageRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    BookServiceImpl bookService;

    @Autowired
    BookRepository bookRepository;

    @RequestMapping(value = "/order", method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_VALUE)
    public Order listPage(@RequestBody Order order) {

        return order;
    }

    @RequestMapping(value = "/products", method = RequestMethod.GET)
    public @ResponseBody List<Storage> getProducts() {
        storageRepository.saveAndFlush(new Storage("bicycle"));
        orderRepository.saveAndFlush(new Order("bicycle","asdf",2020));
        return informationSystemDBService.getAllProducts();
    }

    @RequestMapping(value = "/orders", method = RequestMethod.GET)
    public @ResponseBody List<Order> getOrders() {
//        List<Books> books = bookService.getBooks();

        List<Books> books2 = bookRepository.findAll();
        return orderRepository.findAll();
    }

}
