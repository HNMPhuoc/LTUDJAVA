package Web.BookFelix.Viewmodels;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookPostVm {

    @NotBlank(message = "Title must not be blank")
    @Size(min = 1, max = 50, message = "Title must be between 1 and 50 characters")
    private String title;

    @NotBlank(message = "Author must not be blank")
    @Size(min = 1, max = 50, message = "Author must be between 1 and 50 characters")
    private String author;

    @Positive(message = "Price must be greater than 0")
    private Double price;

    @NotNull(message = "Category must not be null")
    private Long categoryId;
}
