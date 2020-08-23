package nl.rockstars.controller;

import static nl.rockstars.config.GlobalStringResources.LOGIN_STATUS_SUCCESFUL;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import nl.rockstars.controller.exceptions.KeyNotAuthenticatedException;
import nl.rockstars.controller.mapping.LoginRequest;
import nl.rockstars.controller.mapping.MessageResponse;
import nl.rockstars.service.MusicService;

/**
 * This class performs unit tests on the authentication elements of the
 * MusicRestController. Since most business logic is delegated to the
 * MusicService further tests of the controller can be found in the integration
 * tests.
 */

@ExtendWith(MockitoExtension.class)
public class MusicRestControllerAuthenticationTest {
    @Mock
    MusicService musicService;

    @InjectMocks
    private MusicRestController controller;

    @Test
    void givenRequestWhenLoginThenGenerateToken() {
        // Given login request
        var loginRequest = new LoginRequest("email", "password");

        // When login
        when(musicService.login(any(LoginRequest.class))).thenReturn(new MessageResponse(LOGIN_STATUS_SUCCESFUL));
        ResponseEntity<MessageResponse> response = controller.login(loginRequest);
        // Then a token and MessageResponse are generated.
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals(1, MusicRestController.tokens.size());
        assertTrue(response.getHeaders().containsKey("authentication_key"));
        assertTrue(StringUtils.isNotBlank(response.getHeaders().get("authentication_key").get(0)));
        assertTrue(response.getBody().getMessage().contains(LOGIN_STATUS_SUCCESFUL));
    }

    @Test
    void givenKeyWhenAuthenticateSuccesfullThenReturn() {
        // Given key
        var key = "key";
        // When key is in tokens
        MusicRestController.tokens.add(key);
        // Then no exception is thrown
        assertDoesNotThrow(() -> controller.authenticate(key));
        // Cleanup
        MusicRestController.tokens.remove(key);
    }

    @Test
    void givenKeyWhenAuthenticateNotSuccesfullThenThrowException() {
        // Given key
        var key = "key";
        // When key is not in tokens
        // Then an exception is thrown
        assertThrows("Expected authenticate to throw, but it didn't",
                KeyNotAuthenticatedException.class, () -> controller.authenticate(key));
    }

}