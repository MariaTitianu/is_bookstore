package org.example.service.book;

import org.example.model.book.Book;
import org.example.model.validator.Notification;

import java.util.List;

public interface BookService {

    List<Book> findAll();

    Book findById(Long id);

    Notification<Book> decrementByAmount(long book, int amount);

    boolean save(Book book);

    int getAgeOfBook(Long id);

    Notification<Book> sellBook(Long userId, Long bookId, Integer bookQuantity);

    Notification<Book> update(Book book);

    Notification<Book> delete(Long id);

    Notification<String> generateEmployeeReport(Long userId);
}
