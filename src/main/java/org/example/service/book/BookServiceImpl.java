package org.example.service.book;

import org.example.model.book.Book;
import org.example.model.validator.Notification;
import org.example.repository.book.BookRepository;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    public BookServiceImpl(BookRepository bookRepository){
        this.bookRepository = bookRepository;
    }

    @Override
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    @Override
    public Book findById(Long id) {
        return bookRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Book with id: %d not found".formatted(id)));
    }

    @Override
    public Notification<Book> decrementByAmount(long bookId, int amount) {
        Notification<Book> notification = new Notification<>();
        Optional<Book> bookOptional = bookRepository.findById(bookId);
        if(bookOptional.isPresent()) {
            Book book = bookOptional.get();
            if(book.getQuantity() - amount >= 0) {
                book.setQuantity(book.getQuantity() - amount);
                notification = bookRepository.updateBook(book);
            }
            else{
                notification.addError("Cannot buy books! (Insufficient quantity)");
            }
        }
        else{
            notification.addError("Cannot buy books! (Insufficient quantity)");
        }
        return notification;
    }

    @Override
    public boolean save(Book book) {
        return bookRepository.save(book);
    }

    @Override
    public int getAgeOfBook(Long id) {
        Book book = this.findById(id);

        LocalDate now = LocalDate.now();

        return (int) ChronoUnit.YEARS.between(book.getPublishedDate(), now);
    }
}