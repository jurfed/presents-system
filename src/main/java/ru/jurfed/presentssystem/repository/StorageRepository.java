package ru.jurfed.presentssystem.repository;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.jurfed.presentssystem.domain.Storage;

@Repository("storage")
public interface StorageRepository extends JpaRepository<Storage, String> {

}
