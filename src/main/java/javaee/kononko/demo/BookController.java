package javaee.kononko.demo;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class BookController {

    private final BookRepository repository;

    public BookController(BookRepository repository) {
        this.repository = repository;
    }

    @RequestMapping("/")
    public String allBooks(Model model){
        var books = repository.allBooks();
        model.addAttribute("books", books);
        return "books";
    }
}