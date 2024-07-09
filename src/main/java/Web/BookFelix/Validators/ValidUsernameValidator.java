package Web.BookFelix.Validators;

import Web.BookFelix.Entities.User;
import Web.BookFelix.Repositories.IUserRepository;
import Web.BookFelix.Services.UserService;
import Web.BookFelix.Validators.Annotations.ValidUsername;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@NoArgsConstructor
@Component
public class ValidUsernameValidator implements ConstraintValidator<ValidUsername, String> {

    @Autowired
    private IUserRepository userService;

    @Override
    public boolean isValid(String username, ConstraintValidatorContext context) {
        if (userService == null)
            return true;
        return userService.findByUsername(username).isEmpty();
    }
}
