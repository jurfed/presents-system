package ru.jurfed.presentssystem.domain;

import javax.persistence.*;

@Entity
@Table(name = "Books")
public class Books {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private int id;

    public Books(String bookName) {
        this.bookName = bookName;
    }

    public Books() {
    }

    @Column(name = "name")
    private String bookName;

    public Books(int id, String bookName) {
        this.id = id;
        this.bookName = bookName;
    }

    public int getBookId() {
        return id;
    }

    public String getBookName() {
        return bookName;
    }

    @Override
    public String toString() {
        return "Book{" +
                "bookId=" + id +
                ", bookName='" + bookName + '\'' +
                '}';
    }
}
