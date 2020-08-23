package nl.rockstars.service;

import static nl.rockstars.config.GlobalStringResources.ENTITY_NOT_FOUND;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
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

import nl.rockstars.controller.exceptions.EntityNotFoundException;
import nl.rockstars.model.Artist;
import nl.rockstars.model.ArtistRepository;
import nl.rockstars.model.Song;
import nl.rockstars.model.SongRepository;
import nl.rockstars.model.dto.ArtistDTO;
import nl.rockstars.model.dto.SongDTO;

@ExtendWith(MockitoExtension.class)
public class MusicServiceUpdateTest {
    @Mock
    private SongRepository songRepository;
    @Mock
    private ArtistRepository artistRepository;

    @InjectMocks
    private MusicService musicService;

    @Test
    void givenSongIdAndSongDTOWhenSongNotFoundThenEntityNotFoundExceptionIsThrown() {
        // Given a song id and song dto
        var id = 10;
        var songDTO = new SongDTO();
        // When the song is not found in the songRepository
        when(songRepository.findById(10)).then(new ReturnsEmptyValues());
        // Then an exception is thrown
        var exception = assertThrows("Expected updateSong to throw, but it didn't",
        EntityNotFoundException.class, () -> musicService.updateSong(id, songDTO));

        var expectedMessage = ENTITY_NOT_FOUND + id;
        var actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void givenSongIdAndSongDTOWhenUpdateThenReturnsSong() {
        // Given a song id and song dto
        var id = 10;
        var songDTO = new SongDTO();
        // When the song is found in the songRepository
        when(songRepository.findById(10)).thenReturn(Optional.of(new Song()));
        when(songRepository.save(any(Song.class))).thenReturn(new Song());
        // Then a song is returned
        var song = musicService.updateSong(id, songDTO);
        assertNotNull(song);
    }

    @Test
    void givenSongAndSongDTOWhenUpdateThenSongUpdated() {
        // Given a song and song dto
        var song = Song.builder().name("name1").album("album1").build();
        var songDTO = new SongDTO();
        // When the songDTO contains partial updates
        songDTO.setYear(2020);
        songDTO.setAlbum("album2");
        // Then an updated song is returned
        musicService.transferSong(songDTO, song);
        assertEquals("name1", song.getName());
        assertEquals("album2", song.getAlbum());
        assertEquals(2020, song.getYear());
        assertNull(song.getShortName());
    }

    @Test
    void givenSongDTOandSongWhenUpdateNewArtistThenUpdateArtist() {
        // Given a song and song dto
        var songDTO = new SongDTO();
        var song = Song.builder().artist(Artist.builder().name("artist1").build()).build();
        // When the song dto contains new artist
        songDTO.setArtist("artist2");
        var artist = Artist.builder().name("artist2").build();
        when(artistRepository.findOptionalByName(anyString())).thenReturn(Optional.of(artist));
        // Then a song with the updated artist is returned
        musicService.transferArtist(songDTO, song);
        assertEquals("artist2", song.getArtist().getName());
    }

    @Test
    void givenSongDTOandSongWhenUpdateSamArtistThenNotUpdateArtist() {
        // Given a song and song dto
        var songDTO = new SongDTO();
        var song = Song.builder().artist(Artist.builder().name("artist1").build()).build();
        // When the song dto contains new artist
        songDTO.setArtist("artist1");
        // Then the song is not modified and no mock is needed
        musicService.transferArtist(songDTO, song);
        assertEquals("artist1", song.getArtist().getName());
    }

    @Test
    void givenArtistIdAndArtistDTOWhenArtistNotFoundThenEntityNotFoundExceptionIsThrown() {
        // Given an artist id and artist dto
        var id = 10;
        var artistDTO = new ArtistDTO();
        // When the artist is not found in the artistRepository
        when(artistRepository.findById(10)).then(new ReturnsEmptyValues());
        // Then an exception is thrown
        EntityNotFoundException exception = assertThrows("Expected updateArtist to throw, but it didn't",
        EntityNotFoundException.class, () -> musicService.updateArtist(id, artistDTO));

        var expectedMessage = ENTITY_NOT_FOUND + id;
        var actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void givenArtistIdAndArtistDTOWhenUpdateThenReturnsArtist() {
        // Given an artist id and artist dto
        var id = 10;
        var artistDTO = new ArtistDTO();
        // When the artist is found in the songRepository
        when(artistRepository.findById(10)).thenReturn(Optional.of(new Artist()));
        when(artistRepository.save(any(Artist.class))).thenReturn(new Artist());
        // Then an artist is returned
        var artist = musicService.updateArtist(id, artistDTO);
        assertNotNull(artist);
    }

    @Test
    void givenArtistAndArtistDTOWhenUpdateThenArtistUpdated() {
        // Given an artist and artist dto
        var artist = Artist.builder().name("name1").build();
        var artistDTO = new ArtistDTO();
        // When the artistDTO contains partial updates
        artistDTO.setGenre("genre");
        // Then an updated artist is returned
        musicService.transferArtist(artistDTO, artist);
        assertEquals("name1", artist.getName());
        assertEquals("genre", artist.getGenre());
    }


}