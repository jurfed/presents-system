package ru.jurfed.presentssystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.jurfed.presentssystem.domain.Manufacturing;
import ru.jurfed.presentssystem.domain.Order;

import java.util.List;
import java.util.Optional;

@Repository("manufacturing")
public interface ManufacturingRepository extends JpaRepository<Manufacturing, Integer> {

    List<Manufacturing> findAllByProductType(String productType);

}
