package fr.d2factory.libraryapp.book;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

/**
 * A simple representation of a book
 */
public class Book {

    @JsonProperty
    private String title;
    @JsonProperty
    private String author;
    @JsonProperty
    private ISBN isbn;

    public Book() {
    }

    public Book(String title, String author, ISBN isbn) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public ISBN getIsbn() {
        return isbn;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return Objects.equals(title, book.title) &&
                Objects.equals(author, book.author) &&
                Objects.equals(isbn, book.isbn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, author, isbn);
    }
}
