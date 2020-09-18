package ru.jurfed.presentssystem.repository;



import ru.jurfed.presentssystem.domain.Books;

import java.util.List;

public interface BookService {
    List<Books> getBooks();

    Books getBookById(int id);

    void createTable();

    public int count2();

    void insertBook(int id, String bookName);

    int getCount();
}
