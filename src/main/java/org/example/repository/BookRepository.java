package org.example.repository;

import org.example.model.AudioBook;
import org.example.model.Book;
import org.example.model.EBook;

import java.util.List;
import java.util.Optional;

public interface BookRepository {

    List<Book> findAll();

    List<AudioBook> findAllAudioBook();
    List<EBook> findAllEBook();

    Optional<Book> findById(Long id);

    boolean save(Book book);

    void removeAll();
}
