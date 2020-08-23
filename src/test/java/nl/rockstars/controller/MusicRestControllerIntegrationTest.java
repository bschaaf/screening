package nl.rockstars.controller;

import static nl.rockstars.config.GlobalStringResources.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import nl.rockstars.controller.exceptions.UserNotFoundException;
import nl.rockstars.controller.exceptions.UserNotValidatedException;
import nl.rockstars.controller.mapping.LoginRequest;
import nl.rockstars.controller.mapping.MessageResponse;
import nl.rockstars.service.MusicService;

@ActiveProfiles("test")
@WebMvcTest(controllers = MusicRestController.class)
public class MusicRestControllerIntegrationTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MusicService musicService;

    @Test
    void givenLoginRequestWhenLoginThenStatusOk() throws JsonProcessingException, Exception {
        // Given login request
        LoginRequest loginRequest = new LoginRequest();
        // When login is performed
        when(musicService.login(any(LoginRequest.class))).thenReturn(new MessageResponse(LOGIN_STATUS_SUCCESFUL));
        // Then http status is ok and message says succesful
        MvcResult mvcResult = mvc.perform(post("/api/music/v1/login").contentType("application/json")
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        assertTrue(mvcResult.getResponse().getContentAsString().contains(LOGIN_STATUS_SUCCESFUL));
    }

    @Test
    void givenLoginRequestWhenLoginAndNotValidatedExceptionThrownThenStatusIsNotOk() throws JsonProcessingException, Exception {
        // Given login request
        LoginRequest loginRequest = new LoginRequest("email", "password");
        // When login is performed and user not validated exception occurs
        when(musicService.login(any(LoginRequest.class))).thenThrow(new UserNotValidatedException(USER_NOT_VALIDATED + loginRequest.getEmail()));
        // Then http status is unauthorized and message says not validated
        MvcResult mvcResult = mvc.perform(post("/api/music/v1/login").contentType("application/json")
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        assertTrue(mvcResult.getResponse().getContentAsString().contains(USER_NOT_VALIDATED + loginRequest.getEmail()));
    }

    @Test
    void givenLoginRequestWhenLoginAndNotFoundExceptionThrownThenStatusIsNotOk() throws JsonProcessingException, Exception {
        // Given login request
        LoginRequest loginRequest = new LoginRequest("email", "password");
        // When login is performed and user not found exception occurs
        when(musicService.login(any(LoginRequest.class))).thenThrow(new UserNotFoundException(USER_NOT_FOUND + loginRequest.getEmail()));
        // Then http status is not found and message says not found
        MvcResult mvcResult = mvc.perform(post("/api/music/v1/login").contentType("application/json")
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        assertTrue(mvcResult.getResponse().getContentAsString().contains(USER_NOT_FOUND + loginRequest.getEmail()));
    }
}