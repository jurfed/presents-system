package ru.jurfed.presentssystem.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.Random;

/**
 * Service determine the behavior of
 */
@Service
public class DemeanourServiceImpl implements IDemeanourService {

    private Random random;
    private static final Logger logger = LogManager.getLogger(DemeanourServiceImpl.class);

    @Override
    public boolean getDemeanour(String fio) {
        logger.info("Getting a child's behavior");
        return new Random().nextBoolean();

    }
}
