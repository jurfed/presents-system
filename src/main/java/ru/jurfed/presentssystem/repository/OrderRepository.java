package ru.jurfed.presentssystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.jurfed.presentssystem.domain.Order;
import ru.jurfed.presentssystem.domain.Storage;

import java.util.List;

@Repository("order")
public interface OrderRepository  extends JpaRepository<Order, Integer> {

    List<Order> findAll();

}
