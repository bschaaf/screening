package nl.rockstars.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.internal.stubbing.defaultanswers.ReturnsEmptyValues;
import org.mockito.junit.jupiter.MockitoExtension;

import nl.rockstars.model.Artist;
import nl.rockstars.model.ArtistRepository;
import nl.rockstars.model.Song;
import nl.rockstars.model.SongRepository;
import nl.rockstars.model.dto.ArtistDTO;
import nl.rockstars.model.dto.SongDTO;

@ExtendWith(MockitoExtension.class)
public class MusicServiceCreateTest {
    @Mock
    private SongRepository songRepository;
    @Mock
    private ArtistRepository artistRepository;

    @InjectMocks
    private MusicService musicService;

    @Test
    void givenSongDTOWhenCreateThenReturnSong() {
        // Given a song and song dto
        var songDTO = new SongDTO();
        // When song is created
        when(songRepository.save(any(Song.class))).thenReturn(new Song());
        var song = musicService.createSong(songDTO);
        // Then a song is returned
        assertNotNull(song);
    }

    @Test
    void givenArtistDTOWhenCreateThenReturnArtist() {
        // Given an artist and artist dto
        var artistDTO = new ArtistDTO();
        // When artist is created
        when(artistRepository.save(any(Artist.class))).thenReturn(new Artist());
        var artist = musicService.createArtist(artistDTO);
        // Then an artist is returned
        assertNotNull(artist);
    }

    @Test
    void givenNewArtistNameAndGenreWhenInsertThenReturnArticle() {
        // Given a new artist
        var artistName = "artist";
        var genre = "genre";
        // When artist is inserted
        when(artistRepository.findOptionalByName(anyString())).then(new ReturnsEmptyValues());
        when(artistRepository.save(any(Artist.class))).thenReturn(Artist.builder().name(artistName).genre(genre).build());
        var artist = musicService.insertArtist(artistName, genre);
        // Then the new artist is returned
        assertNotNull(artist);
        assertEquals(artistName, artist.getName());
        assertEquals(genre, artist.getGenre());
    }

    @Test
    void givenExistingArtistNameAndGenreWhenInsertThenReturnArticle() {
        // Given an existing artist 
        var artistName = "artist";
        var genre = "genre";
        // When artist is inserted
        when(artistRepository.findOptionalByName(anyString())).thenReturn(Optional.of(new Artist()));
        var artist = musicService.insertArtist(artistName, genre);
        // Then an artist is returned
        assertNotNull(artist);
        assertNull(artist.getName());
        assertNull(artist.getGenre());
    }

    @Test
    void givenNewArtistNameWhenInsertThenReturnArticle() {
        // Given a new artist
        var artistName = "artist";
        // When artist is inserted
        when(artistRepository.findOptionalByName(anyString())).then(new ReturnsEmptyValues());
        when(artistRepository.save(any(Artist.class))).thenReturn(Artist.builder().name(artistName).build());
        var artist = musicService.insertArtist(artistName);
        // Then the new artist is returned
        assertNotNull(artist);
        assertEquals(artistName, artist.getName());
    }

    @Test
    void givenExistingArtistNameWhenInsertThenReturnArticle() {
        // Given an existing artist 
        var artistName = "artist";
        // When artist is inserted
        when(artistRepository.findOptionalByName(anyString())).thenReturn(Optional.of(new Artist()));
        var artist = musicService.insertArtist(artistName);
        // Then an artist is returned
        assertNotNull(artist);
        assertNull(artist.getName());
    }
}