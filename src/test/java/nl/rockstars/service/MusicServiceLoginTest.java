package nl.rockstars.service;

import static nl.rockstars.config.GlobalStringResources.LOGIN_STATUS_SUCCESFUL;
import static nl.rockstars.config.GlobalStringResources.USER_NOT_FOUND;
import static nl.rockstars.config.GlobalStringResources.USER_NOT_VALIDATED;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.internal.stubbing.defaultanswers.ReturnsEmptyValues;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import nl.rockstars.controller.exceptions.UserNotFoundException;
import nl.rockstars.controller.exceptions.UserNotValidatedException;
import nl.rockstars.controller.mapping.LoginRequest;
import nl.rockstars.model.User;
import nl.rockstars.model.UserRepository;

@ExtendWith(MockitoExtension.class)
public class MusicServiceLoginTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private MusicService musicService;

    @Test
    void givenLoginRequestWhenEmailNotFoundThenUserNotFoundExceptionIsThrown() {
        // Given a login request
        var request = new LoginRequest("email", "password");
        // When the emailadress is not found in the userrepository
        when(userRepository.findOptionalByEmail(anyString())).then(new ReturnsEmptyValues());
        // Then an exception is thrown
        var exception = assertThrows("Expected login to throw, but it didn't",
                UserNotFoundException.class, () -> musicService.login(request));

        var expectedMessage = USER_NOT_FOUND + request.getEmail();
        var actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void givenLoginRequestWhenPasswordInvalidThenUserNotFoundExceptionIsThrown() {
        // Given a login request
        var request = new LoginRequest("email", "password");
        // When the password doesn't match
        when(userRepository.findOptionalByEmail(anyString())).thenReturn(Optional.of(new User("user","password")));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);
        // Then an exception is thrown
        var exception = assertThrows("Expected login to throw, but it didn't",
                UserNotValidatedException.class, () -> musicService.login(request));

        var expectedMessage = USER_NOT_VALIDATED + request.getEmail();
        var actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void givenLoginRequestWhenPasswordValidThenMessageSuccesful() {
        // Given a login request
        var request = new LoginRequest("email", "password");
        // When the password does match
        when(userRepository.findOptionalByEmail(anyString())).thenReturn(Optional.of(new User("user","password")));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        // Then a MessageResponse is generated
        var messageResponse = musicService.login(request);
        var expectedMessage = LOGIN_STATUS_SUCCESFUL;
        var actualMessage = messageResponse.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

}