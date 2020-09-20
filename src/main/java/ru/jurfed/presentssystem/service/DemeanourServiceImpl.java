package ru.jurfed.presentssystem.service;

import org.springframework.stereotype.Service;

import java.util.Random;

/**
 * Service determine the behavior of
 */
@Service
public class DemeanourServiceImpl implements IDemeanourService {

    private Random random;

    @Override
    public boolean getDemeanour(String fio) {
       return new Random().nextBoolean();

    }
}
