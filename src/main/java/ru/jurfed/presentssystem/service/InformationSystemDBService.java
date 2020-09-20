package ru.jurfed.presentssystem.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.jurfed.presentssystem.domain.Manufacturing;
import ru.jurfed.presentssystem.domain.Order;
import ru.jurfed.presentssystem.domain.Storage;
import ru.jurfed.presentssystem.repository.ManufacturingRepository;
import ru.jurfed.presentssystem.repository.OrderRepository;
import ru.jurfed.presentssystem.repository.StorageRepository;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

@Service
public class InformationSystemDBService {

/*    @PersistenceContext
    private EntityManager em;*/

    @Autowired
    StorageRepository storageRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    ManufacturingRepository manufacturingRepository;

    public List<Storage> getAllProducts() {
        return storageRepository.findAll();
    }

    public boolean checkPreorder(int year, String fio) {
        return orderRepository.findByYearAndAndFio(year, fio).isEmpty();
    }

    public Manufacturing addManufacturingRequest(String productType, Integer count) {
        Manufacturing manufacturing = manufacturingRepository.saveAndFlush(new Manufacturing(productType, count));
        return manufacturing;
    }

    //метод создания предзаказа и наименования товара на складе (при его отсутствии) ***************************************************************
    public void createPreorder(String productType, String fio, Integer year) {

        Optional<Storage> storage = storageRepository.findById(productType);

        if (storage.isEmpty()) {
            storageRepository.saveAndFlush(new Storage(productType));
        }

        Order order = new Order(productType, fio, year, false);
        orderRepository.saveAndFlush(order);
    }

    //Метод проверки начилия товара на складе
    public boolean checkAvailableProducts(String productType) {
        boolean isProductExists = false;
        Optional<Storage> storage = storageRepository.findById(productType);

        if (!storage.isEmpty() && storage.get().getAvailableValue() > 0) {
            isProductExists = true;
        }

        return isProductExists;

    }

    //метод перевода одного товара со склада в хранилище
    public Order transferOneProductToOrder(String productType, String fio, Integer year) {

        List<Order> orders = orderRepository.findByProductTypeAndFioAndYearAndReleased(productType, fio, year, false);

        if (orders.size() > 0) {


            Order order = orders.get(0);
            order.setReleased(true);
            orderRepository.saveAndFlush(order);


            Optional<Storage> storage = storageRepository.findById(productType);
            storage.ifPresent(storage1 -> {
                int availableValues = storage1.getAvailableValue();
                if (availableValues > 0) {
                    storage1.setAvailableValue(--availableValues);
                    storageRepository.saveAndFlush(storage1);
                }
            });
            checkMinAvailableProducts(productType);
            return orders.get(0);
        }
        return null;
    }


    //метод перевода всех необработанных товаров определенного типа со склада в хранилище
    public void transferFromStorageIntoOrder(String presentType) {

        Optional<Storage> storage = storageRepository.findById(presentType);
        storage.ifPresent(storage1 -> {
            int availableValue = storage1.getAvailableValue();
            List<Order> orders = orderRepository.findByProductTypeAndReleased(presentType, false);

            int count = availableValue <= orders.size() ? availableValue : orders.size();

            for (int i = 0; i < count; i++) {
                Order order = orders.get(i);
                order.setReleased(true);
                orderRepository.saveAndFlush(order);
            }

            storage1.setAvailableValue(storage1.getAvailableValue() - count);
            storageRepository.saveAndFlush(storage1);

        });

        checkMinAvailableProducts(presentType);
    }

    //проверка минимального кол-ва товара данного типа на складе = available + manufacruring
    public void checkMinAvailableProducts(String productType){
        Optional<Storage> storageOptional = storageRepository.findById(productType);
        int neededToManufacture = 0;
        if(!storageOptional.isEmpty()){
            Storage storage = storageOptional.get();
            int availableValue = storage.getAvailableValue();
            int minValue = storage.getMinValue();

            List<Manufacturing> sendingForProductValue = manufacturingRepository.findAllByProductType(productType);
            int manufacturingNow = sendingForProductValue.stream().mapToInt(manufacturing -> manufacturing.getCount()).sum();

            if(availableValue + manufacturingNow < minValue){
                neededToManufacture = minValue-(availableValue + manufacturingNow);
                sendManufacturingRequest(productType, neededToManufacture);
            }
        }

    }

    //послать запрос на производство и сохранить в manufacturing
    private void sendManufacturingRequest(String productType, int minValue){
        RestTemplate restTemplate = new RestTemplate();
        final String baseUrl = "http://localhost:" + 8092 + "/manufacturing";

        Manufacturing manufacturing = addManufacturingRequest(productType, minValue);
        try {
            URI uri = new URI(baseUrl);
            HttpEntity<Manufacturing> requestBody = new HttpEntity(manufacturing);
            restTemplate.postForEntity(uri,requestBody,Manufacturing.class);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    //удалить из manufacturing и обновить значение сделанных товаров в Storage
    public void deleteManufacture(Manufacturing manufacturing) {


        String productType = manufacturing.getProductType();
        Integer count = manufacturing.getCount();
        manufacturingRepository.delete(manufacturing);


        Optional<Storage> storageOptional = storageRepository.findById(productType);

        if (!storageOptional.isEmpty()) {
            Storage storage = storageOptional.get();
            storage.setAvailableValue(storage.getAvailableValue() + count);
            storageRepository.saveAndFlush(storage);
        }

        transferFromStorageIntoOrder(productType);

    }

    @EventListener(ApplicationStartedEvent.class)
    public void doSomethingAfterStartup() {
        storageRepository.findAll().forEach(storage -> {checkMinAvailableProducts(storage.getProductType());});

    }

}
