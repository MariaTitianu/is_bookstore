package org.example.service;

import org.example.model.AudioBook;
import org.example.model.Book;
import org.example.model.EBook;

import java.util.List;

public interface BookService {

    List<Book> findAll();
    List<AudioBook> findAllAudioBook();
    List<EBook> findAllEBook();

    Book findById(Long id);

    boolean save(Book book);

    int getAgeOfBook(Long id);
}
