package ru.jurfed.presentssystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.jurfed.presentssystem.domain.Order;

import java.util.List;
import java.util.Optional;

@Repository("order")
public interface OrderRepository extends JpaRepository<Order, Integer> {

    List<Order> findAll();

    Optional<Order> findByYearAndFio(int year, String fio);

    List<Order> findByProductTypeAndReleased(String productType, boolean released);

    List<Order> findByProductTypeAndFioAndYearAndReleased(String productType, String fio, int year, boolean released);

}
