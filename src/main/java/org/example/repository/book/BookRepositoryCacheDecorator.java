package org.example.repository.book;

import org.example.model.book.Book;
import org.example.model.validator.Notification;

import java.util.List;
import java.util.Optional;

public class BookRepositoryCacheDecorator extends BookRepositoryDecorator {
    private Cache<Book> cache;
    public BookRepositoryCacheDecorator(BookRepository bookRepository, Cache<Book> cache){
        super(bookRepository);
        this.cache = cache;
    }

    @Override
    public List<Book> findAll() {
        if (cache.hasResult()){
            return cache.load();
        }

        List<Book> books = decoratedRepository.findAll();
        cache.save(books);

        return books;
    }

    @Override
    public Optional<Book> findById(Long id) {
        if (cache.hasResult()){
            return cache.load()
                    .stream()
                    .filter(it -> it.getId().equals(id))
                    .findFirst();
        }

        Optional<Book> book = decoratedRepository.findById(id);

        return book;
    }

    @Override
    public boolean save(Book book) {
        cache.invalidateCache();
        return decoratedRepository.save(book);
    }

    @Override
    public void removeAll() {
        cache.invalidateCache();
        decoratedRepository.removeAll();
    }

    @Override
    public Notification<Book> updateBook(Book book) {
        return null;
    }

    @Override
    public Notification<Book> deleteBook(Long id) {
        return null;
    }

    @Override
    public Notification<Boolean> updateEmployeeActivity(Long userId, Long bookId, Integer bookQuantity) {
        return null;
    }

    @Override
    public Notification<List<String>> getEmployeeReport(Long id) {
        return null;
    }
}
