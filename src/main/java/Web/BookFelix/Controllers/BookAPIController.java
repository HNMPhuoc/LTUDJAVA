package Web.BookFelix.Controllers;

import Web.BookFelix.Entities.Book;
import Web.BookFelix.Entities.Category;
import Web.BookFelix.Services.BookAPIService;
import Web.BookFelix.Services.BookService;
import Web.BookFelix.Services.CategoryAPIService;
import Web.BookFelix.Services.CategoryService;
import Web.BookFelix.Viewmodels.BookGetVm;
import Web.BookFelix.Viewmodels.BookPostVm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
@RestController
@RequestMapping("/api/books")
public class BookAPIController {
    @Autowired
    private BookAPIService bookService;
    @Autowired
    private CategoryAPIService categoryService;

    @GetMapping("")
    public ResponseEntity<List<BookGetVm>> getAllBooks() {
        List<Book> books = bookService.findAll();
        List<BookGetVm> bookGetVms = books.stream().map(BookGetVm::from).collect(Collectors.toList());
        return ResponseEntity.ok(bookGetVms);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookGetVm> getBookById(@PathVariable Long id) {
        Book book = bookService.findById(id);
        if (book == null) {
            return ResponseEntity.notFound().build();
        }
        BookGetVm bookGetVm = BookGetVm.from(book);
        return ResponseEntity.ok(bookGetVm);
    }

    @PostMapping
    public ResponseEntity<BookPostVm> createBook(@RequestBody BookPostVm book) {
        Book createdBook = bookService.saveBookPostVm(book);
        return ResponseEntity.ok(book);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookGetVm> updateBook(@PathVariable Long id, @RequestBody BookPostVm bookPostVm) {
        Book book = bookService.findById(id);
        if (book == null) {
            return ResponseEntity.notFound().build();
        }
        Category category = categoryService.findById(bookPostVm.getCategoryId());
        if (category == null) {
            return ResponseEntity.badRequest().body(null); // hoặc bạn có thể ném một ngoại lệ phù hợp
        }
        book.setId(id); // Ensure the ID is set correctly
        book.setTitle(bookPostVm.getTitle());
        book.setAuthor(bookPostVm.getAuthor());
        book.setPrice(bookPostVm.getPrice());
        book.setCategory(category);
        Book updatedBook = bookService.save(book);
        BookGetVm bookGetVm = BookGetVm.from(updatedBook);
        return ResponseEntity.ok(bookGetVm);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        Book book = bookService.findById(id);
        if (book == null) {
            return ResponseEntity.notFound().build();
        }
        bookService.delete(book);
        return ResponseEntity.noContent().build();
    }
}
