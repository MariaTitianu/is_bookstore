package org.example.model;

import java.util.Objects;


public class EBook extends Book {
    private String format;

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EBook book)) return false;
        return author.equals(book.author) && title.equals(book.title) && publishedDate.equals(book.publishedDate) && format.equals(book.format) ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(author, title, publishedDate,format);
    }

    @Override
    public String toString() {
        return "EBook{" +
                "format='" + format + '\'' +
                ", id=" + id + '\'' +
                ", author='" + author + '\'' +
                ", title='" + title + '\'' +
                ", publishedDate=" + publishedDate +
                '}';
    }
}
