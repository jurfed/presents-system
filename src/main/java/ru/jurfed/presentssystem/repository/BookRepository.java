package ru.jurfed.presentssystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.jurfed.presentssystem.domain.Books;
import ru.jurfed.presentssystem.domain.Storage;

import java.util.List;

@Repository("Books")
public interface BookRepository extends JpaRepository<Books, Integer> {



}
