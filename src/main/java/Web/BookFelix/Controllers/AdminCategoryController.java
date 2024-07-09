package Web.BookFelix.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/Admincategory")
public class AdminCategoryController {
    @GetMapping("")
    public String adminBook() {
        return "/CategoryAPI/index";
    }
}
