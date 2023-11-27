package org.example.model.book;

import java.time.LocalDate;

// Java Bean
// POJO - Plain Old Java Object - nu extinde nicio clasă, nu implemnetează nicio interfață și nu are nicio adnotare

public class Book {

    protected Long id;

    protected String author;

    protected String title;

    protected LocalDate publishedDate;
    protected Integer quantity;

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

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

    public LocalDate getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(LocalDate publishedDate) {
        this.publishedDate = publishedDate;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", author='" + author + '\'' +
                ", title='" + title + '\'' +
                ", publishedDate=" + publishedDate +
                ", quantity=" + quantity +
                '}';
    }
}
