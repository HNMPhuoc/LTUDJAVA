package Web.BookFelix.Services;

import Web.BookFelix.Entities.Book;
import Web.BookFelix.Entities.Category;
import Web.BookFelix.Repositories.IBookRepository;
import Web.BookFelix.Repositories.ICategoryRepository;
import Web.BookFelix.Viewmodels.BookPostVm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class BookAPIService {
    private final IBookRepository bookRepository;
    @Autowired
    private ICategoryRepository categoryRepository;

    @Autowired
    public BookAPIService(IBookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    public Book findById(Long id) {
        return bookRepository.findById(id).orElse(null);
    }

    public Book save(Book book) {
        return bookRepository.save(book);
    }

    public Book saveBookPostVm(BookPostVm bookPostVm) {
        Book book = new Book();
        book.setAuthor(bookPostVm.getAuthor());
        book.setPrice(bookPostVm.getPrice());
        book.setTitle(bookPostVm.getTitle());
        Category category = categoryRepository.getReferenceById(bookPostVm.getCategoryId());
        book.setCategory(category);
        return bookRepository.saveAndFlush(book);
    }

    public void delete(Book book) {
        bookRepository.delete(book);
    }
}
