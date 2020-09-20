package ru.jurfed.presentssystem.service;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.jurfed.presentssystem.domain.Manufacturing;
import ru.jurfed.presentssystem.domain.Order;
import ru.jurfed.presentssystem.domain.Storage;
import ru.jurfed.presentssystem.repository.ManufacturingRepository;
import ru.jurfed.presentssystem.repository.OrderRepository;
import ru.jurfed.presentssystem.repository.StorageRepository;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Service
public class InformationSystemDBService {

    @PersistenceContext
    private EntityManager em;

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

    public Storage getPresent(String presentType) {
        Optional<Storage> present = storageRepository.findById(presentType);
        if (present.isEmpty()) {
            storageRepository.saveAndFlush(new Storage(presentType));
        }
        return present.get();

    }

    public boolean checkProductInStorage(String presentType) {
        int availableValue;
        Optional<Storage> present = storageRepository.findById(presentType);

        boolean presentExists = !present.isEmpty();

        if (presentExists) {
            availableValue = present.get().getAvailableValue();
            if (availableValue <= 0) {
                presentExists = false;
            }
        } else {
            storageRepository.saveAndFlush(new Storage(presentType));
        }


        return presentExists;

    }

    public List<Manufacturing> getManufacturing(String presentType) {
        List<Manufacturing> manufacturing = manufacturingRepository.findAllByProductType(presentType);
        return manufacturing;
    }


    public Manufacturing addManufacturingRequest(String productType, Integer count) {
        Manufacturing manufacturing = manufacturingRepository.saveAndFlush(new Manufacturing(productType, count));
        return manufacturing;
    }

    //удалить из manufacturing и обновить значение сделанных товаров в Storage
    public void deleteManufacture(Manufacturing manufacturing) {

        EntityTransaction transaction = em.getTransaction();
        transaction.begin();

        String productType = manufacturing.getProductType();
        Integer count = manufacturing.getCount();
        em.remove(manufacturing);


        Optional<Storage> storageOptional = storageRepository.findById(productType);

        if (!storageOptional.isEmpty()) {
            Storage storage = storageOptional.get();
            storage.setAvailableValue(storage.getAvailableValue() + count);
            em.merge(storage);
        }
        transaction.commit();

        transferFromStorageIntoOrder(productType);

    }

//перенести товар со склада в заказы и обновить кол-во доступных, а затем выполнить проверку
    public void transferFromStorageIntoOrder(String presentType) {
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();

        Optional<Storage> storage = storageRepository.findById(presentType);
        storage.ifPresent(storage1 -> {
            int availableValue = storage1.getAvailableValue();
            List<Order> orders = orderRepository.findByProductTypeAndReleased(presentType,false);

            int count = availableValue <= orders.size() ? availableValue : orders.size();

            for(int i=0;i<count; i++){
                Order order = orders.get(i);
                order.setReleased(true);
                em.merge(order);
            }

            storage1.setAvailableValue(storage1.getAvailableValue() - count);
            em.merge(storage1);

        });
        transaction.commit();

//        checkProductInStorage(presentType);
    }

}
