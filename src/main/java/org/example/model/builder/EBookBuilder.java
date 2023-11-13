package org.example.model.builder;

import org.example.model.EBook;

import java.time.LocalDate;

public class EBookBuilder extends BookBuilder {
    private final EBook book;

    public EBookBuilder() {
        book = new EBook();
    }

    public EBookBuilder setFormat(String format) {
        book.setFormat(format);
        return this;
    }
    @Override
    public EBookBuilder setId(Long id) {
        book.setId(id);
        return this;
    }

    @Override
    public EBookBuilder setAuthor(String author) {
        book.setAuthor(author);
        return this;
    }

    @Override
    public EBookBuilder setTitle(String title) {
        book.setTitle(title);
        return this;
    }

    @Override
    public EBookBuilder setPublishedDate(LocalDate publishedDate) {
        book.setPublishedDate(publishedDate);
        return this;
    }


    public EBook build() {
        return book;
    }
}

