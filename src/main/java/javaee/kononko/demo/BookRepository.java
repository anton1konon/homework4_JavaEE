package javaee.kononko.demo;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class BookRepository {
    private final List<Book> books = new ArrayList<>();

    public void addBook(Book book) {
        books.add(book);
    }

    public Iterable<Book> allBooks() {
        return books;
    }

    public Iterable<Book> searchBooks(String query) {
        return () -> books.stream().filter(b -> b.getName().contains(query) || b.getIsbn().contains(query)).iterator();
    }
}

