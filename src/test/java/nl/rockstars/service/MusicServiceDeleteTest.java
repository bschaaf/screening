package nl.rockstars.service;

import static nl.rockstars.config.GlobalStringResources.CONSTRAINT_VIOLATION_DELETE;
import static nl.rockstars.config.GlobalStringResources.DELETE_SUCCESFUL;
import static nl.rockstars.config.GlobalStringResources.DELETE_UNSUCCESFUL;
import static nl.rockstars.config.GlobalStringResources.ENTITY_NOT_FOUND;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.internal.stubbing.defaultanswers.ReturnsEmptyValues;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import nl.rockstars.controller.exceptions.EntityNotDeletedException;
import nl.rockstars.model.ArtistRepository;
import nl.rockstars.model.Song;
import nl.rockstars.model.SongRepository;

@ExtendWith(MockitoExtension.class)
public class MusicServiceDeleteTest {
    @Mock
    private SongRepository songRepository;
    @Mock
    private ArtistRepository artistRepository;

    @InjectMocks
    private MusicService musicService;

    @Test
    void givenIdAndRepositoryWhenDeleteUnknownIdThenThrowException() {
        // Given an id
        var id = 10;
        // When the song is not found in the songRepository (interchangeable with
        // articleRepository)
        when(songRepository.findById(10)).then(new ReturnsEmptyValues());
        // Then an exception is thrown
        var exception = assertThrows("Expected deleteIdFromRepository to throw, but it didn't",
                EntityNotDeletedException.class, () -> musicService.deleteIdFromRepository(id, songRepository));
        var expectedMessage = ENTITY_NOT_FOUND + id;
        var actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void givenIdAndRepositoryWhenDeleteKnownIdConstraintViolationThenThrowException() {
        // Given an id
        var id = 10;
        // When the song is found in the songRepository (interchangeable with
        // articleRepository) but an integrity violation occurs
        when(songRepository.findById(10)).thenReturn(Optional.of(new Song()));
        doThrow(new DataIntegrityViolationException("")).when(songRepository).deleteById(10);
        // Then an exception is thrown
        var exception = assertThrows("Expected deleteIdFromRepository to throw, but it didn't",
                EntityNotDeletedException.class, () -> musicService.deleteIdFromRepository(id, songRepository));
                var expectedMessage = CONSTRAINT_VIOLATION_DELETE;
        var actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void givenIdAndRepositoryWhenDeleteKnownIdAndIdStillPresentThenThrowException() {
        // Given an id
        var id = 10;
        // When the song is found in the songRepository (interchangeable with
        // articleRepository) and still present after deletion.
        when(songRepository.findById(10)).thenReturn(Optional.of(new Song())).thenReturn(Optional.of(new Song()));
        // Then an exception is thrown
        var exception = assertThrows("Expected deleteIdFromRepository to throw, but it didn't",
                EntityNotDeletedException.class, () -> musicService.deleteIdFromRepository(id, songRepository));
                var expectedMessage = DELETE_UNSUCCESFUL + id;
        var actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void givenIdAndRepositoryWhenDeleteKnownIdAndIdDeletedThenMessageSuccesful() {
        // Given an id
        var id = 10;
        // When the song is found in the songRepository (interchangeable with
        // articleRepository) and deleted.
        when(songRepository.findById(10)).thenReturn(Optional.of(new Song())).then(new ReturnsEmptyValues());
        // Then a MessageResponse is generated
        var messageResponse = musicService.deleteIdFromRepository(id, songRepository);
        var expectedMessage = DELETE_SUCCESFUL + id;
        var actualMessage = messageResponse.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

}
