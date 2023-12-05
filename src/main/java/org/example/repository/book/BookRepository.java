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

    Notification<Book> deleteBook(Long id);

    Notification<Boolean> updateEmployeeActivity(Long userId, Long bookId, Integer bookQuantity);

    Notification<List<String>> getEmployeeReport(Long id);
}
