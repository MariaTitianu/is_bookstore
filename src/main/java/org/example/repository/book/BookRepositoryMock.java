package org.example.repository.book;


import org.example.model.book.Book;
import org.example.model.validator.Notification;

import java.util.List;
import java.util.Optional;

public class BookRepositoryMock implements BookRepository {
    private List<Book> books;

    public BookRepositoryMock(List<Book> test_library){
        books = test_library;
    }

    @Override
    public List<Book> findAll() {
        return books;
    }

    @Override
    public Optional<Book> findById(Long id) {
        return books.parallelStream()
                .filter(it -> it.getId().equals(id))
                .findFirst();
    }

    @Override
    public boolean save(Book book) {
        return books.add(book);
    }

    @Override
    public void removeAll() {
        books.clear();
    }

    @Override
    public Notification<Book> updateBook(Book book) {
        return null;
    }
}