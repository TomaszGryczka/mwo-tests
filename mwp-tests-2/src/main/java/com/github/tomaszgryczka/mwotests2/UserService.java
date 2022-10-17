package com.github.tomaszgryczka.mwotests2;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.stream.Stream;

@Service
@Component
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final EmailService emailService;

    public boolean loginUser(final String login, final String password) {
        final boolean userExists = userRepository.checkIfUserExists(login);

        if (userExists) {
            return password.equals(userRepository.getUserPasswordByLogin(login));
        } else {
            throw new IllegalArgumentException("User does not exists");
        }
    }

    public boolean registerUser(final String login, final String password, final String email) {
        boolean invalidData = Stream.of(login, password, email)
                .anyMatch(Objects::isNull) || !emailService.validateEmail(email);

        if (invalidData) {
            throw new IllegalArgumentException("Invalid registration data!");
        } else {
            final boolean userAlreadyExists = userRepository.registerUser(login, password, email);
            if(userAlreadyExists) {
                throw new IllegalArgumentException("User already exists!");
            } else {
                return true;
            }
        }
    }

    public double checkAccountBalance(final String accountId) {
        return 0;
    }
}
