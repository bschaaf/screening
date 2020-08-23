package nl.rockstars.controller.exceptions;

@SuppressWarnings("serial") 
public class UserNotFoundException extends RuntimeException {
    
    public UserNotFoundException(String user) {
        super(user);
    }
}
