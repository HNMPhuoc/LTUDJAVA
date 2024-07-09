package Web.BookFelix.Controllers;

import Web.BookFelix.Entities.Category;
import Web.BookFelix.Services.CategoryAPIService;
import Web.BookFelix.Viewmodels.CategoryGetVm;
import Web.BookFelix.Viewmodels.CategoryPostVm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/categories")
public class CategoryAPIController {
    @Autowired
    private CategoryAPIService categoryService;
    @GetMapping("")
    public ResponseEntity<List<CategoryGetVm>> getAllCategories() {
        List<Category> categories = categoryService.findAll();
        List<CategoryGetVm> categoryGetVms = categories.stream()
                .map(CategoryGetVm::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(categoryGetVms);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryGetVm> getCategoryById(@PathVariable Long id) {
        Category category = categoryService.findById(id);
        if (category == null) {
            return ResponseEntity.notFound().build();
        }
        CategoryGetVm categoryGetVm = CategoryGetVm.from(category);
        return ResponseEntity.ok(categoryGetVm);
    }

    @PostMapping
    public ResponseEntity<CategoryGetVm> createCategory(@RequestBody CategoryPostVm categoryPostVm) {
        Category category = new Category();
        category.setName(categoryPostVm.getName());

        Category savedCategory = categoryService.save(category);

        CategoryGetVm categoryGetVm = CategoryGetVm.from(savedCategory);
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryGetVm);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryGetVm> updateCategory(@PathVariable Long id, @RequestBody CategoryPostVm categoryPostVm) {
        Category category = categoryService.findById(id);
        if (category == null) {
            return ResponseEntity.notFound().build();
        }
        category.setName(categoryPostVm.getName());
        Category updatedCategory = categoryService.save(category);
        CategoryGetVm categoryGetVm = CategoryGetVm.from(updatedCategory);
        return ResponseEntity.ok(categoryGetVm);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        Category category = categoryService.findById(id);
        if (category == null) {
            return ResponseEntity.notFound().build();
        }
        categoryService.delete(category);

        return ResponseEntity.noContent().build();
    }
}
