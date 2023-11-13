package org.example.repository;

import org.example.model.AudioBook;
import org.example.model.Book;
import org.example.model.EBook;

import java.util.List;
import java.util.Optional;

public class BookRepositoryCacheDecorator extends BookRepositoryDecorator{
    private Cache<Book> cache;
    private Cache<EBook> cacheEbook;
    private Cache<AudioBook> cacheAudioBook;
    public BookRepositoryCacheDecorator(BookRepository bookRepository, Cache<Book> cache,Cache<EBook> cacheEbook, Cache<AudioBook> cacheAudioBook){
        super(bookRepository);
        this.cache = cache;
        this.cacheEbook = cacheEbook;
        this.cacheAudioBook = cacheAudioBook;
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
    public List<AudioBook> findAllAudioBook() {
        if (cacheAudioBook.hasResult()){
            return cacheAudioBook.load();
        }
        List<AudioBook> books = decoratedRepository.findAllAudioBook();
        cacheAudioBook.save(books);
        return books;
    }

    @Override
    public List<EBook> findAllEBook() {
        if (cacheEbook.hasResult()){
            return cacheEbook.load();
        }
        List<EBook> books = decoratedRepository.findAllEBook();
        cacheEbook.save(books);
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
}
