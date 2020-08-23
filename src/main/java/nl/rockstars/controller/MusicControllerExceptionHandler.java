package nl.rockstars.controller;

import static nl.rockstars.config.GlobalStringResources.*;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import nl.rockstars.controller.exceptions.EntityNotDeletedException;
import nl.rockstars.controller.exceptions.EntityNotFoundException;
import nl.rockstars.controller.exceptions.KeyNotAuthenticatedException;
import nl.rockstars.controller.exceptions.UserNotFoundException;
import nl.rockstars.controller.exceptions.UserNotValidatedException;
import nl.rockstars.controller.mapping.MessageResponse;

/**
 * Exception handler for exceptions occurring in the Music Controller
 */
@RestControllerAdvice
public class MusicControllerExceptionHandler {
    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public MessageResponse handleUserNotFoundException(UserNotFoundException exception) {
        return new MessageResponse(exception.getMessage());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public MessageResponse handleEntityNotFoundException(EntityNotFoundException exception) {
        return new MessageResponse(exception.getMessage());
    }

    @ExceptionHandler(UserNotValidatedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public MessageResponse handleUserNotValidatedException(UserNotValidatedException exception) {
        return new MessageResponse(exception.getMessage());
    }

    @ExceptionHandler(KeyNotAuthenticatedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public MessageResponse keyNotValidatedException(KeyNotAuthenticatedException exception) {
        return new MessageResponse(INVALID_KEY);
    }

    @ExceptionHandler(EntityNotDeletedException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public MessageResponse entityNotDeletedException(EntityNotDeletedException exception) {
        return new MessageResponse(exception.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public MessageResponse genericException(Exception exception) {
        return new MessageResponse(exception.getMessage());
    }
}