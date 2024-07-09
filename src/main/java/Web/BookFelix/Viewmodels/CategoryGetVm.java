package Web.BookFelix.Viewmodels;

import Web.BookFelix.Entities.Category;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CategoryGetVm {

    private Long id;
    private String name;

    public static CategoryGetVm from(@NotNull Category category) {
        return CategoryGetVm.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }
}
