package Web.BookFelix.Services;

import Web.BookFelix.Entities.Category;
import Web.BookFelix.Repositories.ICategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class CategoryAPIService {
    @Autowired
    private ICategoryRepository categoryRepository;

    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    public Category findById(Long id) {
        return categoryRepository.findById(id).orElse(null);
    }

    public Category save(Category category) {
        return categoryRepository.saveAndFlush(category);
    }

    public void delete(Category category) {
        categoryRepository.delete(category);
    }
}
