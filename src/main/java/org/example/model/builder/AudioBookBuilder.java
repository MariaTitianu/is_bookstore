package org.example.model.builder;

import org.example.model.book.AudioBook;

import java.time.LocalDate;

public class AudioBookBuilder extends BookBuilder{
    private AudioBook book;

    public AudioBookBuilder(){
        book = new AudioBook();
    }

    public AudioBookBuilder setRuntime(Long rt){
        book.setRuntime(rt);
        return this;
    }

    @Override
    public AudioBookBuilder setId(Long id) {
        book.setId(id);
        return this;
    }

    @Override
    public AudioBookBuilder setAuthor(String author) {
        book.setAuthor(author);
        return this;
    }

    @Override
    public AudioBookBuilder setTitle(String title) {
        book.setTitle(title);
        return this;
    }

    @Override
    public AudioBookBuilder setPublishedDate(LocalDate publishedDate) {
        book.setPublishedDate(publishedDate);
        return this;
    }

    public AudioBook build(){
        return book;
    }
}
