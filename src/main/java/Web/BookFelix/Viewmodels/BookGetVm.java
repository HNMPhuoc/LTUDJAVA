package Web.BookFelix.Viewmodels;

import Web.BookFelix.Entities.Book;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookGetVm {

    private Long id;
    private String title;
    private String author;
    private Double price;
    private CategoryGetVm category;

    public static BookGetVm from(Book book) {
        BookGetVm bookGetVm = new BookGetVm();
        bookGetVm.setId(book.getId());
        bookGetVm.setTitle(book.getTitle());
        bookGetVm.setAuthor(book.getAuthor());
        bookGetVm.setPrice(book.getPrice());

        if (book.getCategory() != null) {
            bookGetVm.setCategory(CategoryGetVm.from(book.getCategory()));
        }

        return bookGetVm;
    }
}