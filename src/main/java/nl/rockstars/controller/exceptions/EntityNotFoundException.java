package nl.rockstars.controller.exceptions;

@SuppressWarnings("serial") 
public class EntityNotFoundException extends RuntimeException {
    
    public EntityNotFoundException(String message) {
        super(message);
    }
}
