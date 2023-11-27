package org.example.repository.book;

import org.example.model.book.Book;
import org.example.model.validator.Notification;

import java.util.List;
import java.util.Optional;

public interface BookRepository {

    List<Book> findAll();
    Optional<Book> findById(Long id);

    boolean save(Book book);

    void removeAll();

    Notification<Book> updateBook(Book book);
}
