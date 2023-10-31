package org.example;

import org.example.model.Book;
import org.example.model.builder.BookBuilder;

import java.util.Date;

public class Main {

    public static void main(String[] args){
        System.out.println("Hello 30239!");

        Book book = new BookBuilder()
                .setTitle("Amintiri din copilarie")
                .setPublishedDate(new Date())
                .setAuthor("Ion Creanga")
                .build();

        System.out.println(book);

    }
}
