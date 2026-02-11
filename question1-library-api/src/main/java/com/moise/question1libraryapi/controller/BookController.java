package com.moise.question1libraryapi.controller;

import com.moise.question1libraryapi.model.Book;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private List<Book> bookList = new ArrayList<>();

    public BookController() {
        bookList.add(new Book(1L, "JAVA", "Manzi Moise", "978-0132350884", 2020));
        bookList.add(new Book(2L, "HTML & CSS", "Cyusa Kevin", "978-0134685991", 2021));
        bookList.add(new Book(3L, "Spring Boot", "Sam David", "978-1617294945", 2022));
    }

    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks() {
        return ResponseEntity.ok(bookList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        return bookList.stream()
                .filter(book -> book.getId().equals(id))
                .findFirst()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    public ResponseEntity<List<Book>> searchBooksByTitle(@RequestParam String title) {
        List<Book> results = bookList.stream()
                .filter(book -> book.getTitle().toLowerCase().contains(title.toLowerCase()))
                .toList();

        return ResponseEntity.ok(results);
    }

    @PostMapping
    public ResponseEntity<Book> addBook(@RequestBody Book newBook) {
        newBook.setId((long) (bookList.size() + 1));
        bookList.add(newBook);
        return ResponseEntity.status(HttpStatus.CREATED).body(newBook);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        boolean removed = bookList.removeIf(book -> book.getId().equals(id));

        if (removed) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
