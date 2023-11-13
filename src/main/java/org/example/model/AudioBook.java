package org.example.model;

import java.util.Objects;

public class AudioBook extends Book{
    private Long runtime;

    public Long getRuntime() {
        return runtime;
    }

    public void setRuntime(Long runtime) {
        this.runtime = runtime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AudioBook book)) return false;
        return author.equals(book.author) && title.equals(book.title) && publishedDate.equals(book.publishedDate) && runtime.equals(book.runtime) ;
    }

    @Override
    public String toString() {
        return "AudioBook{" +
                "runtime=" + runtime + '\'' +
                ", id=" + id + '\'' +
                ", author='" + author + '\'' +
                ", title='" + title + '\'' +
                ", publishedDate=" + publishedDate +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(author, title, publishedDate,runtime);
    }
}
