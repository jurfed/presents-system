package ru.jurfed.presentssystem.service;

import ru.jurfed.presentssystem.domain.Manufacturing;
import ru.jurfed.presentssystem.domain.Order;
import ru.jurfed.presentssystem.Dto.ProductDto;
import ru.jurfed.presentssystem.domain.Storage;

public interface IInformationSystemDBService {

    ProductDto getAllProducts();

    boolean checkPreorder(int year, String fio);

    Manufacturing addManufacturingRequest(String productType, Integer count);

    boolean createPreorder(String productType, String fio, Integer year);

    boolean checkAvailableProducts(String productType);

    Order transferOneProductToOrder(String productType, String fio, Integer year);

    void transferFromStorageIntoOrder(String presentType);

    void checkMinAvailableProducts(String productType);

    void deleteManufacture(Manufacturing manufacturing);

    void refreshProducts(ProductDto productDto);

    void checkAvailableAfterStartUp();

    Storage getOrdersByProductType(String productType);
}
