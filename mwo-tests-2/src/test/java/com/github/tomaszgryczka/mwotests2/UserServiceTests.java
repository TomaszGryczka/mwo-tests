package com.github.tomaszgryczka.mwotests2;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
public class UserServiceTests {
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private EmailService emailService;
    @Autowired
    private UserService userService;

    @Test
    public void should_LoginUser_When_CorrectCredentialsAreGiven() {
        // given
        final String login = "TEST";
        final String password = "TEST";

        Mockito.when(userRepository.checkIfUserExists(login)).thenReturn(true);
        Mockito.when(userRepository.getUserPasswordByLogin(login)).thenReturn(password);

        // when
        final boolean isLoggedIn = userService.loginUser(login, password);

        // then
        Assertions.assertTrue(isLoggedIn);
    }

    @Test
    public void should_NotLoginUser_When_BadPasswordIsGiven() {
        // given
        final String login = "TEST";
        final String userPassword = "TEST";
        final String badPassword = "TEST1";

        Mockito.when(userRepository.checkIfUserExists(login)).thenReturn(true);
        Mockito.when(userRepository.getUserPasswordByLogin(login)).thenReturn(userPassword);

        // when
        final boolean isLoggedIn = userService.loginUser(login, badPassword);

        // then
        Assertions.assertFalse(isLoggedIn);
    }

    @Test
    public void should_ThrowException_When_UserWithGivenLoginDoesNotExists() {
        // given
        final String badlogin = "TEST";
        final String password = "TEST";

        Mockito.when(userRepository.checkIfUserExists(badlogin)).thenReturn(false);

        // when
        Assertions.assertThrows(IllegalArgumentException.class, () -> userService.loginUser(badlogin, password));
    }

    @Test
    public void should_ThrowsException_When_GivenNullRegistrationData() {
        // given
        final String notNullLogin = "TEST";
        final String nullLogin = null;

        final String notNullPassword = "TEST";
        final String nullPassword = null;

        final String notNullEmail = "test@test.com";
        final String nullEmail = null;

        Mockito.when(emailService.validateEmail(notNullEmail)).thenReturn(true);
        Mockito.when(emailService.validateEmail(nullEmail)).thenReturn(false);

        // when
        Assertions.assertThrows(IllegalArgumentException.class, () -> userService.registerUser(nullLogin, notNullPassword, notNullEmail));
        Assertions.assertThrows(IllegalArgumentException.class, () -> userService.registerUser(notNullLogin, nullPassword, notNullEmail));
        Assertions.assertThrows(IllegalArgumentException.class, () -> userService.registerUser(notNullLogin, notNullPassword, nullEmail));
        Assertions.assertThrows(IllegalArgumentException.class, () -> userService.registerUser(nullLogin, nullPassword, nullEmail));
    }

    @Test
    public void should_ThrowException_When_EmailIsInvalid() {
        // given
        final String validLogin = "TEST";
        final String validPassword = "TEST";
        final String invalidEmail = "HBDYŻ";

        Mockito.when(emailService.validateEmail(invalidEmail)).thenReturn(false);

        // when
        Assertions.assertThrows(IllegalArgumentException.class, () -> userService.registerUser(validLogin, validPassword, invalidEmail));
    }

    @Test
    public void should_RegisterUser_When_UserDoesNotExists() {
        // given
        final String login = "TEST";
        final String password = "TEST";
        final String email = "TEST";

        Mockito.when(emailService.validateEmail(email)).thenReturn(true);
        Mockito.when(userRepository.registerUser(login, password, email)).thenReturn(false);

        // when
        final boolean result = userService.registerUser(login, password, email);

        // then
        Assertions.assertTrue(result);
    }

    @Test
    public void should_ThrowException_When_TryingToRegisterExistingUser() {
        // given
        final String login = "TEST";
        final String password = "TEST";
        final String email = "TEST";

        Mockito.when(emailService.validateEmail(email)).thenReturn(true);
        Mockito.when(userRepository.registerUser(login, password, email)).thenReturn(true);

        // when
        Assertions.assertThrows(IllegalArgumentException.class, () -> userService.registerUser(login, password, email));
    }
}
