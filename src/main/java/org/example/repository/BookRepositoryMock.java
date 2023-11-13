package org.example.repository;


import org.example.model.AudioBook;
import org.example.model.Book;
import org.example.model.EBook;

import java.util.List;
import java.util.Optional;

public class BookRepositoryMock implements BookRepository{
    private List<Book> books;

    public BookRepositoryMock(List<Book> test_library){
        books = test_library;
    }

    @Override
    public List<Book> findAll() {
        return books;
    }

    @Override
    public List<AudioBook> findAllAudioBook() {
        return null;
    }

    @Override
    public List<EBook> findAllEBook() {
        return null;
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
}