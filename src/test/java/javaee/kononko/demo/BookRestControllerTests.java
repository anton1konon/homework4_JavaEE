package javaee.kononko.demo;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

@WebMvcTest(BookRestController.class)
public class BookRestControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookRepository bookRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void addBook() throws Exception {
        var form = new BookForm("Anton", "Kononko", "3216726742278");
        var json = objectMapper.writeValueAsString(form);
        mockMvc.perform(MockMvcRequestBuilders.post("/addBook")
                .contentType("application/json")
                .content(json))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    void postEmptyError() throws Exception {
        var json = "{}";
        mockMvc.perform(MockMvcRequestBuilders.post("/addBook")
                .contentType("application/json")
                .content(json))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void postNoNameError() throws Exception {
        var json = "{\"author\":\"aa\",\"isbn\":\"abc\"}";
        mockMvc.perform(MockMvcRequestBuilders.post("/addBook")
                .contentType("application/json")
                .content(json))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void postNoAuthorError() throws Exception {
        var json = "{\"name\":\"aa\",\"isbn\":\"abc\"}";
        mockMvc.perform(MockMvcRequestBuilders.post("/addBook")
                .contentType("application/json")
                .content(json))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void postNoIsbnError() throws Exception {
        var json = "{\"name\":\"aa\",\"author\":\"abc\"}";
        mockMvc.perform(MockMvcRequestBuilders.post("/addBook")
                .contentType("application/json")
                .content(json))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void postWrongIsbnError() throws Exception {
        var form = new BookForm("Anton", "Kononko", "BBB84F");
        var json = objectMapper.writeValueAsString(form);
        mockMvc.perform(MockMvcRequestBuilders.post("/addBook")
                .contentType("application/json")
                .content(json))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void allBooks() throws Exception {
        var books = List.of(
                new Book("Anton", "Kononko", "3216726742278"),
                new Book("The Great Gatsby", "F. Scott Fitzgerald", "9780521485470")
        );

        var json = objectMapper.writeValueAsString(books);

        Mockito.when(bookRepository.allBooks()).thenReturn(books);

        mockMvc.perform(MockMvcRequestBuilders.get("/bookList?query="))
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().json(json));

    }

    @Test
    void searchBooks() throws Exception {
        var books = List.of(
                new Book("Anton", "Kononko", "3216726742278"),
                new Book("The Great Gatsby", "F. Scott Fitzgerald", "9780521485470")
        );

        var json = objectMapper.writeValueAsString(books);

        Mockito.when(bookRepository.searchBooks("9780")).thenReturn(books);

        mockMvc.perform(MockMvcRequestBuilders.get("/bookList?query=9780"))
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().json(json));

    }

    @Test
    void searchBooksEmpty() throws Exception {
        Mockito.when(bookRepository.searchBooks("9780")).thenReturn(List.of());

        mockMvc.perform(MockMvcRequestBuilders.get("/bookList?query=9780"))
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().json("[]"));
    }
}
