package ru.jurfed.presentssystem.rest;

import org.springframework.web.bind.annotation.RequestBody;
import ru.jurfed.presentssystem.domain.Manufacturing;

public interface IProductionRestService {

    void sendPresentToOrder(@RequestBody Manufacturing manufacturing);

}
