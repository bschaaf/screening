package nl.rockstars.service;

import static nl.rockstars.config.GlobalStringResources.ENTITY_NOT_FOUND;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.internal.stubbing.defaultanswers.ReturnsEmptyValues;
import org.mockito.junit.jupiter.MockitoExtension;

import nl.rockstars.controller.exceptions.EntityNotFoundException;
import nl.rockstars.model.Artist;
import nl.rockstars.model.ArtistRepository;
import nl.rockstars.model.Song;
import nl.rockstars.model.SongRepository;

@ExtendWith(MockitoExtension.class)
public class MusicServiceFindTest {
    @Mock
    private SongRepository songRepository;
    @Mock
    private ArtistRepository artistRepository;

    @InjectMocks
    private MusicService musicService;

    @Test
    void givenSongIdWhenSongNotFoundThenEntityNotFoundExceptionIsThrown() {
        // Given a song id
        var id = 10;
        // When the song is not found in the songRepository
        when(songRepository.findById(10)).then(new ReturnsEmptyValues());
        // Then an exception is thrown
        var exception = assertThrows("Expected getSong to throw, but it didn't",
                EntityNotFoundException.class, () -> musicService.getSong(id));

        var expectedMessage = ENTITY_NOT_FOUND + id;
        var actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void givenSongIdWhenSongFoundThenReturnsSong() {
        // Given a song id
        var id = 10;
        // When the song is found in the songRepository
        when(songRepository.findById(10)).thenReturn(Optional.of(new Song()));
        // Then a song is returned
        var song = musicService.getSong(id);
        assertNotNull(song);
    }

    @Test
    void givenArtistIdWhenArtistNotFoundThenEntityNotFoundExceptionIsThrown() {
        // Given a artist id
        var id = 10;
        // When the artist is not found in the artistRepository
        when(artistRepository.findById(10)).then(new ReturnsEmptyValues());
        // Then an exception is thrown
        var exception = assertThrows("Expected getArtist to throw, but it didn't",
                EntityNotFoundException.class, () -> musicService.getArtist(id));

        var expectedMessage = ENTITY_NOT_FOUND + id;
        var actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void givenArtistIdWhenArtistFoundThenReturnsArtist() {
        // Given a artist id
        var id = 10;
        // When the artist is found in the songRepository
        when(artistRepository.findById(anyInt())).thenReturn(Optional.of(new Artist()));
        // Then an artist is returned
        var artist = musicService.getArtist(id);
        assertNotNull(artist);
    }

    @Test
    void whenSongsRequestReturnsListofSongs() {
        // When search all songs in songRepository
        when(songRepository.findAll()).thenReturn(new ArrayList<Song>());
        // A list of songs is returned
        var list = musicService.getSongs();
        assertNotNull(list);
    }

    @Test
    void whenArtistsRequestReturnsListofArtists() {
        // When search all artists in artistRepository
        when(artistRepository.findAll()).thenReturn(new ArrayList<Artist>());
        // A list of artists is returned
        var list = artistRepository.findAll();
        assertNotNull(list);
    }

    @Test
    void whenSongsByGenreRequestReturnsListofSongs() {
        // When search all songs by genre in songRepository
        when(songRepository.findAllByGenre(anyString())).thenReturn(new ArrayList<Song>());
        // A list of songs is returned
        var list = songRepository.findAllByGenre("genre");
        assertNotNull(list);
    }

    @Test
    void whenSongsByArtistRequestReturnsListofSongs() {
        // When search all songs by artist in songRepository
        when(songRepository.findAllByArtist(anyInt())).thenReturn(new ArrayList<Song>());
        // A list of songs is returned
        var list = songRepository.findAllByArtist(10);
        assertNotNull(list);
    }

    @Test
    void whenArtistbyNameRequestReturnsListofSongs() {
        // When search all artists by part of name in songRepository
        when(artistRepository.findAllByName(anyString())).thenReturn(new ArrayList<Artist>());
        // A list of artists is returned
        var list = artistRepository.findAllByName("name");
        assertNotNull(list);
    }

}