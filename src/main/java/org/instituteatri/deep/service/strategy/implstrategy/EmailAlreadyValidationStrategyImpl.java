package org.instituteatri.deep.service.strategy.implstrategy;

import lombok.RequiredArgsConstructor;
import org.instituteatri.deep.model.user.User;
import org.instituteatri.deep.exception.user.EmailAlreadyExistsException;
import org.instituteatri.deep.repository.UserRepository;
import org.instituteatri.deep.service.strategy.interfaces.EmailAlreadyValidationStrategy;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailAlreadyValidationStrategyImpl implements EmailAlreadyValidationStrategy {

    private final UserRepository userRepository;

    @Override
    public void validate(String existingEmail, String newEmail, String userIdToExclude) {
        if (!existingEmail.equals(newEmail) && checkIfEmailExists(newEmail, userIdToExclude)) {
            throw new EmailAlreadyExistsException();
        }
    }

    private boolean checkIfEmailExists(String email, String userIdToExclude) {
        User user = (User) userRepository.findByEmail(email);
        return user != null && !user.getId().equals(userIdToExclude);
    }
}
