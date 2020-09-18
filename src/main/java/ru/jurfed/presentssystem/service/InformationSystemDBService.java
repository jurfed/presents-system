package ru.jurfed.presentssystem.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.jurfed.presentssystem.domain.Storage;
import ru.jurfed.presentssystem.repository.OrderRepository;
import ru.jurfed.presentssystem.repository.StorageRepository;

import java.util.List;

@Service
public class InformationSystemDBService {

    @Autowired
    StorageRepository storageRepository;

    @Autowired
    OrderRepository orderRepository;

    public List<Storage> getAllProducts(){
       return storageRepository.findAll();
    }




}
