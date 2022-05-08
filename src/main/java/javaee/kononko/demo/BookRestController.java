package javaee.kononko.demo;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class BookRestController {

    private final BookRepository repository;

    public BookRestController(BookRepository repository) {
        this.repository = repository;
    }

    @ResponseBody
    @GetMapping("/bookList")
    public Iterable<Book> bookList(@RequestParam String query){
        return query.isEmpty() ? repository.allBooks() : repository.searchBooks(query);
    }

    @PostMapping("/addBook")
    public ResponseEntity<String> addBook(@RequestBody final BookForm form){
        if (form.getName() == null || form.getAuthor() == null || form.getIsbn() == null)
            return new ResponseEntity<>("Some of fields are missing", HttpStatus.BAD_REQUEST);
        if (!form.getIsbn().matches("[\\d*-?]+\\d$"))
            return new ResponseEntity<>("ISBN format is incorrect", HttpStatus.BAD_REQUEST);
        repository.addBook(new Book(form.getName(), form.getAuthor(), form.getIsbn()));
        return new ResponseEntity<>("Added successfully!", HttpStatus.CREATED);
    }
}

