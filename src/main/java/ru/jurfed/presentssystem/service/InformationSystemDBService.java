package ru.jurfed.presentssystem.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import ru.jurfed.presentssystem.domain.Manufacturing;
import ru.jurfed.presentssystem.domain.Order;
import ru.jurfed.presentssystem.Dto.ProductDto;
import ru.jurfed.presentssystem.domain.Storage;
import ru.jurfed.presentssystem.repository.ManufacturingRepository;
import ru.jurfed.presentssystem.repository.OrderRepository;
import ru.jurfed.presentssystem.repository.StorageRepository;

import javax.naming.InvalidNameException;
import java.net.URI;
import java.util.List;
import java.util.Optional;

/**
 * Database management service
 */
@Service
@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = InvalidNameException.class)
public class InformationSystemDBService implements IInformationSystemDBService{

    private static final Logger logger = LogManager.getLogger(InformationSystemDBService.class);

    @Autowired
    StorageRepository storageRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    ManufacturingRepository manufacturingRepository;

    public ProductDto getAllProducts() {
        ProductDto productDto = new ProductDto(storageRepository.findAll());
        return productDto;
    }

    public boolean checkPreorder(int year, String fio) {
        return orderRepository.findByYearAndFio(year, fio).isEmpty();
    }

    public Manufacturing addManufacturingRequest(String productType, Integer count) {
        Manufacturing manufacturing = manufacturingRepository.saveAndFlush(new Manufacturing(productType, count));
        return manufacturing;
    }

    /**
     * method for creating a pre-order
     */
    public boolean createPreorder(String productType, String fio, Integer year) {

        if(productType==null){
            return false;
        }

        Optional<Storage> storage = storageRepository.findById(productType);

        if (storage.isEmpty()) {
            storageRepository.saveAndFlush(new Storage(productType));
        }

        if(fio==null){
            return false;
        }

        Order order = new Order(productType, fio, year, false);
        orderRepository.saveAndFlush(order);

        logger.info("Pre-order was created");
        return true;
    }

    /**
     * Method for checking product availability in storage
     */
    public boolean checkAvailableProducts(String productType) {
        boolean isProductExists = false;
        Optional<Storage> storage = storageRepository.findById(productType);

        if (!storage.isEmpty() && storage.get().getAvailableValue() > 0) {
            isProductExists = true;
        }

        logger.info("checking the product in the storage is completed");
        return isProductExists;

    }

    /**
     *method for transferring a single present from a storage to a store
     */
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
            logger.info("the present is moved to the order");
            return orders.get(0);
        }
        logger.error("error: the present is not found");
        return null;
    }

    /**
     * method for transferring all unprocessed presents of a certain type from a storage to the order
     */
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
        logger.info("all unprocessed presents where moved from a storage to order");
        checkMinAvailableProducts(presentType);
    }

    /**
     * checking the minimum quantity of this type of present in stock = available + manufacturing
     */
    public void checkMinAvailableProducts(String productType) {
        Optional<Storage> storageOptional = storageRepository.findById(productType);
        int neededToManufacture = 0;
        if (!storageOptional.isEmpty()) {
            Storage storage = storageOptional.get();
            int availableValue = storage.getAvailableValue();
            int minValue = storage.getMinValue();

            List<Manufacturing> sendingForProductValue = manufacturingRepository.findAllByProductType(productType);
            int manufacturingNow = sendingForProductValue.stream().mapToInt(manufacturing -> manufacturing.getCount()).sum();

            if (availableValue + manufacturingNow < minValue) {
                neededToManufacture = minValue - (availableValue + manufacturingNow);
                sendManufacturingRequest(productType, neededToManufacture);
            }
        }
        logger.info("the minimum quantity of this type of present was checked");
    }

    /**
     *
     * send a request for production and save it in manufacturing
     */
    private void sendManufacturingRequest(String productType, int minValue) {
        RestTemplate restTemplate = new RestTemplate();
        final String baseUrl = "http://localhost:" + 8092 + "/manufacturing";

        Manufacturing manufacturing = addManufacturingRequest(productType, minValue);
        try {
            URI uri = new URI(baseUrl);
            HttpEntity<Manufacturing> requestBody = new HttpEntity(manufacturing);
            restTemplate.postForEntity(uri, requestBody, Manufacturing.class);
            logger.info("the present has been sents for production");
        } catch (Exception e) {

            logger.error("\n****   Error: manufacturing server doesn't exist. \n****   Please run first manufacturing server");
            System.exit(0);
        }
    }

    /**
     * updating the available values of presents in Storage
     */
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

    /**
     * change the minimum values of the presents
     */
    public void refreshProducts(ProductDto productDto) {

        List<Storage> storages = productDto.getStorages();

        storages.forEach(storage -> {
            if (storage.getAvailableValue() == null) {
                storage.setAvailableValue(0);
            }
            if (storage.getMinValue() == null) {
                storage.setMinValue(1);
            }
        });

        storageRepository.saveAll(storages);
        storageRepository.findAll().forEach(storage -> {
            checkMinAvailableProducts(storage.getProductType());
        });

        logger.info("minimum values changed");
    }

    /**
     * determining the minimum number of orders in stock
     */
    @EventListener(ApplicationStartedEvent.class)
    public void checkAvailableAfterStartUp() {
        try{
            storageRepository.findAll().forEach(storage -> {
                checkMinAvailableProducts(storage.getProductType());
            });
        }catch (Exception e){
            logger.error("\n****   Error: manufacturing server doesn't exist. \n****   Please run first manufacturing server");
            System.exit(0);
        }

    }

    @Override
    public Storage getOrdersByProductType(String productType) {
        Optional<Storage> storage = storageRepository.findById(productType);
        if(!storage.isEmpty()){
            return storage.get();
        }
        return null;

    }

}
