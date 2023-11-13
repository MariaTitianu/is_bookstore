package org.example;

import org.example.database.DatabaseConnectionFactory;
import org.example.model.Book;
import org.example.model.builder.BookBuilder;
import org.example.repository.BookRepository;
import org.example.repository.BookRepositoryCacheDecorator;
import org.example.repository.BookRepositoryMySQL;
import org.example.repository.Cache;
import org.example.service.BookService;
import org.example.service.BookServiceImpl;

import java.time.LocalDate;

public class Main {

    public static void main(String[] args){
        BookRepository bookRepository = new BookRepositoryCacheDecorator(
                new BookRepositoryMySQL(DatabaseConnectionFactory.getConnectionWrapper(true).getConnection()),
                new Cache<>(),new Cache<>(),new Cache<>()
        );

        BookService bookService = new BookServiceImpl(bookRepository);

        Book book = new BookBuilder()
                .setAuthor("Cezar Petrescu")
                .setTitle("Fram Ursul Polar")
                .setPublishedDate(LocalDate.of(2010, 6, 2))
                .build();

        bookService.save(book);

        System.out.println(bookService.findAll());
        System.out.println(bookService.findAllAudioBook());
        System.out.println(bookService.findAllEBook());
        System.out.println(bookService.findAll());
        System.out.println(bookService.findAllAudioBook());
        System.out.println(bookService.findAllEBook());
//        System.out.println(bookService.getAgeOfBook(1L));

    }
}
