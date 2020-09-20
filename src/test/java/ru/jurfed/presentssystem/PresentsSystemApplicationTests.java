package ru.jurfed.presentssystem;

import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;
import ru.jurfed.presentssystem.Dto.OrderDto;
import ru.jurfed.presentssystem.Dto.ProductDto;
import ru.jurfed.presentssystem.domain.Order;
import ru.jurfed.presentssystem.domain.Storage;
import ru.jurfed.presentssystem.repository.OrderRepository;
import ru.jurfed.presentssystem.repository.StorageRepository;
import ru.jurfed.presentssystem.service.IDemeanourService;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@RunWith(SpringRunner.class)
class PresentsSystemApplicationTests {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    StorageRepository storageRepository;

    @MockBean
    IDemeanourService demeanourService;

    @LocalServerPort
    int definedPort = 8091;
    final String baseUrl = "http://localhost:" + definedPort;

    @AfterEach
    public void cleanUpEach() {
        orderRepository.deleteAll();
    }

    @Test
    void getAllProducts() {
        RestTemplate restTemplate = new RestTemplate();

        URI uri = null;
        try {
            uri = new URI(baseUrl + "/getAllProducts");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        ResponseEntity<ProductDto> result = restTemplate.getForEntity(uri, ProductDto.class);
        Assert.assertEquals(200, result.getStatusCodeValue());
        Assert.assertEquals(2, result.getBody().getStorages().size());
        System.out.println();
    }

    @Test
    void sendNewOrder() {

        RestTemplate restTemplate = new RestTemplate();

        when(demeanourService.getDemeanour("Ivanov Petr Sergeevich")).thenReturn(true);
        URI uri = null;
        try {
            uri = new URI(baseUrl + "/newOrder");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        Order order = new Order("bicycle", "Ivanov Petr Sergeevich", 2019);

        HttpEntity<Order> requestBody = new HttpEntity(order);
        ResponseEntity<Order> result = restTemplate.postForEntity(uri, requestBody, Order.class);

        Assert.assertEquals(200, result.getStatusCodeValue());
        Assert.assertEquals(1, orderRepository.findAll().size());

    }

    @Test
    void sendRequestToManufacture() {
        when(demeanourService.getDemeanour("Petrov Ivan Alexeevich")).thenReturn(true);
        RestTemplate restTemplate = new RestTemplate();

        URI uri = null;
        try {
            uri = new URI(baseUrl + "/newOrder");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        Order order = new Order("book", "Petrov Ivan Alexeevich", 2020);

        HttpEntity<Order> requestBody = new HttpEntity(order);
        ResponseEntity<Order> result = restTemplate.postForEntity(uri, requestBody, Order.class);

        Assert.assertEquals(200, result.getStatusCodeValue());

        Order order1 = orderRepository.findByYearAndFio(2020, "Petrov Ivan Alexeevich").get();
        Assert.assertEquals("book", order1.getProductType());

    }

	@Test
	void sendManyOrders(){
		when(demeanourService.getDemeanour("one")).thenReturn(true);
		when(demeanourService.getDemeanour("two")).thenReturn(true);
		when(demeanourService.getDemeanour("three")).thenReturn(true);
		RestTemplate restTemplate = new RestTemplate();

		URI uri = null;
		try {
			uri = new URI(baseUrl + "/newOrders");
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}

		Order order1 = new Order("scooter", "one", 2020);
		Order order2 = new Order("scooter", "two", 2020);
		Order order3 = new Order("scooter", "three", 2020);
		List<Order> orders = new ArrayList<>();
		orders.add(order1);
		orders.add(order2);
		orders.add(order3);
		OrderDto orderDto = new OrderDto(orders);

		HttpEntity<OrderDto> requestBody = new HttpEntity(orderDto);
		restTemplate.postForEntity(uri, requestBody, OrderDto.class);

		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		orders = orderRepository.findAll();

		int releasedOrders = (int) orders.stream().filter(order -> order.isReleased()).count();
		Assert.assertEquals(3, releasedOrders);
	}

	@Test
	void badDemeanour(){
		RestTemplate restTemplate = new RestTemplate();

		when(demeanourService.getDemeanour("Ivanov Petr Sergeevich")).thenReturn(false);
		URI uri = null;
		try {
			uri = new URI(baseUrl + "/newOrder");
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}

		Order order = new Order("bicycle", "Ivanov Petr Sergeevich", 2019);

		HttpEntity<Order> requestBody = new HttpEntity(order);
		ResponseEntity<Order> result = restTemplate.postForEntity(uri, requestBody, Order.class);

		Assert.assertEquals(200, result.getStatusCodeValue());
		Assert.assertEquals(0, orderRepository.findAll().size());
	}

    @Test
    void updateMinValuesAndManufacturingProducts(){
        RestTemplate restTemplate = new RestTemplate();

        URI uri = null;
        try {
            uri = new URI(baseUrl + "/refreshProducts");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        Storage storage = new Storage("roller skates",100);
        ProductDto productDto = new ProductDto();
        productDto.addProduct(storage);

        HttpEntity<ProductDto> requestBody = new HttpEntity(productDto);
        ResponseEntity<ProductDto> result = restTemplate.postForEntity(uri, requestBody, ProductDto.class);

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        int availableValue = storageRepository.findById("roller skates").get().getAvailableValue();
        Assert.assertEquals(100, availableValue);
    }

}
