package nl.rockstars.controller.exceptions;

@SuppressWarnings("serial") 
public class UserNotValidatedException extends RuntimeException {
    
    public UserNotValidatedException(String user) {
        super(user);
    }
}
