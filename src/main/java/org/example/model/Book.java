package org.example.model;

import java.util.Date;
import java.util.Objects;

// Java Bean
// POJO - Plain Old Java Object - nu extinde nicio clasă, nu implemnetează nicio interfață și nu are nicio adnotare

public class Book {

    private Long id;

    private String author;

    private String title;

    private Date publishedDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(Date publishedDate) {
        this.publishedDate = publishedDate;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", author='" + author + '\'' +
                ", title='" + title + '\'' +
                ", publishedDate=" + publishedDate +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Book book)) return false;
        return author.equals(book.author) && title.equals(book.title) && publishedDate.equals(book.publishedDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(author, title, publishedDate);
    }
}
