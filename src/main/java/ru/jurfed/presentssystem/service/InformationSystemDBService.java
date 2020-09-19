package ru.jurfed.presentssystem.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.jurfed.presentssystem.domain.Storage;
import ru.jurfed.presentssystem.repository.OrderRepository;
import ru.jurfed.presentssystem.repository.StorageRepository;

import java.util.List;
import java.util.Optional;

@Service
public class InformationSystemDBService {

    @Autowired
    StorageRepository storageRepository;

    @Autowired
    OrderRepository orderRepository;

    public List<Storage> getAllProducts() {
        return storageRepository.findAll();
    }

    public boolean checkPreorder(int productType, String fio) {
        return orderRepository.findByYearAndAndFio(productType, fio).isEmpty();
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
        }else{
            storageRepository.saveAndFlush(new Storage(presentType));
        }


        return presentExists;

    }


}
