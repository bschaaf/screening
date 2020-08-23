package nl.rockstars.controller.exceptions;

@SuppressWarnings("serial") 
public class EntityNotDeletedException extends RuntimeException {
    
    public EntityNotDeletedException(String message) {
        super(message);
    }
}
